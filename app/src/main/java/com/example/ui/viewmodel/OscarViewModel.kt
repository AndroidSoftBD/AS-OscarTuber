package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CommentItem
import com.example.data.model.CreatorStats
import com.example.data.model.NotificationItem
import com.example.data.model.UserProfile
import com.example.data.model.VideoEditConfig
import com.example.data.model.VideoFilter
import com.example.data.model.VideoItem
import com.example.data.repository.OscarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenDestination {
    HOME,
    SHORTS,
    CREATOR_STUDIO,
    OFFLINE_DOWNLOADS,
    LIBRARY
}

class OscarViewModel(application: Application) : AndroidViewModel(application) {
    val repository = OscarRepository(application.applicationContext)

    // Navigation state
    private val _currentScreen = MutableStateFlow(ScreenDestination.HOME)
    val currentScreen = _currentScreen.asStateFlow()

    // Active playing video & player state
    private val _activeVideo = MutableStateFlow<VideoItem?>(null)
    val activeVideo = _activeVideo.asStateFlow()

    private val _isPlaying = MutableStateFlow(true)
    val isPlaying = _isPlaying.asStateFlow()

    private val _playbackPositionSec = MutableStateFlow(0)
    val playbackPositionSec = _playbackPositionSec.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed = _playbackSpeed.asStateFlow()

    private val _selectedQuality = MutableStateFlow("1080p60 HD")
    val selectedQuality = _selectedQuality.asStateFlow()

    private val _isMiniPlayer = MutableStateFlow(false)
    val isMiniPlayer = _isMiniPlayer.asStateFlow()

    private val _isFullscreen = MutableStateFlow(false)
    val isFullscreen = _isFullscreen.asStateFlow()

    // Search & Filtering
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive = _isSearchActive.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    // Dialogs & Sheets
    private val _showAuthDialog = MutableStateFlow(false)
    val showAuthDialog = _showAuthDialog.asStateFlow()

    private val _showNotificationsDialog = MutableStateFlow(false)
    val showNotificationsDialog = _showNotificationsDialog.asStateFlow()

    private val _showGitHubAboutDialog = MutableStateFlow(false)
    val showGitHubAboutDialog = _showGitHubAboutDialog.asStateFlow()

    private val _showUploadSheet = MutableStateFlow(false)
    val showUploadSheet = _showUploadSheet.asStateFlow()

    private val _showVideoEditor = MutableStateFlow(false)
    val showVideoEditor = _showVideoEditor.asStateFlow()

    // Video Editing Configuration
    private val _editConfig = MutableStateFlow(VideoEditConfig())
    val editConfig = _editConfig.asStateFlow()

    // Repository Flows
    val allVideos: StateFlow<List<VideoItem>> = repository.allVideosFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val offlineVideos: StateFlow<List<VideoItem>> = repository.offlineVideosFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val watchLaterVideos: StateFlow<List<VideoItem>> = repository.watchLaterFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val watchHistory: StateFlow<List<VideoItem>> = repository.watchHistoryFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val likedVideos: StateFlow<List<VideoItem>> = repository.likedVideosFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val userUploads: StateFlow<List<VideoItem>> = repository.userUploadsFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val shortsList: StateFlow<List<VideoItem>> = repository.shortsFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val currentUser: StateFlow<UserProfile> = repository.currentUser
    val creatorStats: StateFlow<CreatorStats> = repository.creatorStats
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
    val isCloudSyncing: StateFlow<Boolean> = repository.isCloudSyncing
    val lastCloudSyncTime: StateFlow<String> = repository.lastCloudSyncTime
    val isOfflineOnlyMode: StateFlow<Boolean> = repository.isOfflineOnlyMode

    // Comments for active video
    private val _activeComments = MutableStateFlow<List<CommentItem>>(emptyList())
    val activeComments = _activeComments.asStateFlow()

    // Filtered Feed combining category + search query + offline mode
    val filteredFeed: StateFlow<List<VideoItem>> = combine(
        allVideos,
        offlineVideos,
        _searchQuery,
        _selectedCategory,
        isOfflineOnlyMode
    ) { all, offline, query, category, offlineOnly ->
        val baseList = if (offlineOnly) offline else all
        baseList.filter { video ->
            val matchesQuery = query.isBlank() ||
                    video.title.contains(query, ignoreCase = true) ||
                    video.channelName.contains(query, ignoreCase = true) ||
                    video.tags.any { it.contains(query, ignoreCase = true) }

            val matchesCategory = when (category) {
                "All" -> !video.isShort
                "Shorts" -> video.isShort
                else -> video.category.equals(category, ignoreCase = true)
            }
            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectScreen(screen: ScreenDestination) {
        _currentScreen.value = screen
        _isSearchActive.value = false
    }

    fun openVideo(video: VideoItem) {
        _activeVideo.value = video
        _isPlaying.value = true
        _playbackPositionSec.value = video.watchProgressSec
        _isMiniPlayer.value = false
        // Track in history
        repository.recordWatchHistory(video.id, video.watchProgressSec)
        // Load comments
        loadCommentsFor(video.id)
    }

    fun closeActiveVideo() {
        _activeVideo.value = null
        _isMiniPlayer.value = false
    }

    fun toggleMiniPlayer() {
        _isMiniPlayer.value = !_isMiniPlayer.value
    }

    fun toggleFullscreen() {
        _isFullscreen.value = !_isFullscreen.value
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
    }

    fun seekTo(seconds: Int) {
        _playbackPositionSec.value = seconds
        _activeVideo.value?.let {
            repository.recordWatchHistory(it.id, seconds)
        }
    }

    fun skipForward10() {
        val current = _playbackPositionSec.value
        val duration = _activeVideo.value?.durationSec ?: 1000
        _playbackPositionSec.value = (current + 10).coerceAtMost(duration)
    }

    fun skipBackward10() {
        val current = _playbackPositionSec.value
        _playbackPositionSec.value = (current - 10).coerceAtLeast(0)
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }

    fun setQuality(quality: String) {
        _selectedQuality.value = quality
    }

    private fun loadCommentsFor(videoId: String) {
        viewModelScope.launch {
            repository.getCommentsForVideo(videoId).collect {
                _activeComments.value = it
            }
        }
    }

    fun postComment(text: String) {
        _activeVideo.value?.let { video ->
            repository.addComment(video.id, text)
        }
    }

    fun toggleCommentLike(commentId: String, currentStatus: Boolean) {
        repository.toggleCommentLike(commentId, currentStatus)
    }

    fun toggleLike(video: VideoItem) {
        repository.toggleLike(video.id, video.isLiked)
        if (_activeVideo.value?.id == video.id) {
            _activeVideo.value = _activeVideo.value?.copy(
                isLiked = !video.isLiked,
                likesCount = video.likesCount + if (!video.isLiked) 1 else -1
            )
        }
    }

    fun toggleWatchLater(video: VideoItem) {
        repository.toggleWatchLater(video.id, video.isWatchLater)
        if (_activeVideo.value?.id == video.id) {
            _activeVideo.value = _activeVideo.value?.copy(isWatchLater = !video.isWatchLater)
        }
    }

    fun toggleOfflineSave(video: VideoItem) {
        repository.toggleOfflineSave(video.id, video.isOfflineSaved)
        if (_activeVideo.value?.id == video.id) {
            _activeVideo.value = _activeVideo.value?.copy(isOfflineSaved = !video.isOfflineSaved)
        }
    }

    fun toggleWepe(channelName: String, currentStatus: Boolean) {
        repository.toggleWepe(channelName, currentStatus)
        if (_activeVideo.value?.channelName == channelName) {
            _activeVideo.value = _activeVideo.value?.copy(isWeped = !currentStatus)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun clearHistory() {
        repository.clearHistory()
    }

    fun triggerCloudSync() {
        repository.performCloudSync()
    }

    fun toggleOfflineOnlyMode() {
        repository.toggleOfflineOnlyMode()
    }

    // Dialog controls
    fun setShowAuthDialog(show: Boolean) { _showAuthDialog.value = show }
    fun setShowNotificationsDialog(show: Boolean) {
        _showNotificationsDialog.value = show
        if (show) repository.markNotificationsAsRead()
    }
    fun setShowGitHubAboutDialog(show: Boolean) { _showGitHubAboutDialog.value = show }
    fun setShowUploadSheet(show: Boolean) { _showUploadSheet.value = show }
    fun setShowVideoEditor(show: Boolean) { _showVideoEditor.value = show }

    // Video Editor updates
    fun updateEditorFilter(filter: VideoFilter) {
        _editConfig.value = _editConfig.value.copy(filterType = filter)
    }

    fun updateEditorTrim(start: Float, end: Float) {
        _editConfig.value = _editConfig.value.copy(trimStartMs = start, trimEndMs = end)
    }

    fun updateEditorSpeed(speed: Float) {
        _editConfig.value = _editConfig.value.copy(playbackSpeed = speed)
    }

    fun updateEditorVolume(vol: Float) {
        _editConfig.value = _editConfig.value.copy(audioVolume = vol, isMuted = vol == 0f)
    }

    fun updateEditorWatermark(watermark: String) {
        _editConfig.value = _editConfig.value.copy(watermarkText = watermark)
    }

    fun publishUploadedVideo(
        title: String,
        category: String,
        description: String,
        tags: List<String>,
        thumbnailUrl: String,
        isShort: Boolean = false
    ) {
        repository.uploadVideo(
            title = title,
            category = category,
            description = description,
            tags = tags,
            thumbnailUrl = thumbnailUrl,
            durationSec = 180,
            isShort = isShort
        )
        _showUploadSheet.value = false
        _showVideoEditor.value = false
        // Navigate to Creator Studio so user immediately sees their uploaded video
        _currentScreen.value = ScreenDestination.CREATOR_STUDIO
    }

    fun deleteUpload(videoId: String) {
        repository.deleteVideo(videoId)
    }
}
