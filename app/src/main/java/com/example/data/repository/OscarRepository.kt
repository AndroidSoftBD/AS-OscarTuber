package com.example.data.repository

import android.content.Context
import com.example.data.local.CommentEntity
import com.example.data.local.OscarDatabase
import com.example.data.local.VideoEntity
import com.example.data.model.CommentItem
import com.example.data.model.CreatorStats
import com.example.data.model.NotificationItem
import com.example.data.model.NotificationType
import com.example.data.model.UserProfile
import com.example.data.model.VideoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class OscarRepository(context: Context) {
    private val db = OscarDatabase.getDatabase(context)
    private val dao = db.oscarDao()
    private val scope = CoroutineScope(Dispatchers.IO)

    // Current User Profile state
    private val _currentUser = MutableStateFlow(UserProfile())
    val currentUser = _currentUser.asStateFlow()

    // Creator Analytics state
    private val _creatorStats = MutableStateFlow(CreatorStats())
    val creatorStats = _creatorStats.asStateFlow()

    // Notification Feed state
    private val _notifications = MutableStateFlow(SampleData.sampleNotifications)
    val notifications = _notifications.asStateFlow()

    // Cloud Sync State
    private val _isCloudSyncing = MutableStateFlow(false)
    val isCloudSyncing = _isCloudSyncing.asStateFlow()

    private val _lastCloudSyncTime = MutableStateFlow("Synced just now")
    val lastCloudSyncTime = _lastCloudSyncTime.asStateFlow()

    // Network / Offline Simulator toggle
    private val _isOfflineOnlyMode = MutableStateFlow(false)
    val isOfflineOnlyMode = _isOfflineOnlyMode.asStateFlow()

    init {
        scope.launch {
            seedDatabaseIfEmpty()
        }
    }

    suspend fun ensureInitialized() {
        seedDatabaseIfEmpty()
    }

    private suspend fun seedDatabaseIfEmpty() {
        val all = dao.getVideoById(SampleData.initialVideos.first().id)
        if (all == null) {
            val entities = SampleData.initialVideos.map { VideoEntity.fromDomain(it) }
            dao.insertVideos(entities)

            val commentEntities = SampleData.initialComments.map { CommentEntity.fromDomain(it) }
            dao.insertComments(commentEntities)
        }
    }

    // Flows
    val allVideosFlow: Flow<List<VideoItem>> = dao.getAllVideosFlow().map { list ->
        list.map { it.toDomain() }
    }

    val offlineVideosFlow: Flow<List<VideoItem>> = dao.getOfflineVideosFlow().map { list ->
        list.map { it.toDomain() }
    }

    val watchLaterFlow: Flow<List<VideoItem>> = dao.getWatchLaterVideosFlow().map { list ->
        list.map { it.toDomain() }
    }

    val watchHistoryFlow: Flow<List<VideoItem>> = dao.getWatchHistoryFlow().map { list ->
        list.map { it.toDomain() }
    }

    val likedVideosFlow: Flow<List<VideoItem>> = dao.getLikedVideosFlow().map { list ->
        list.map { it.toDomain() }
    }

    val userUploadsFlow: Flow<List<VideoItem>> = dao.getUserUploadsFlow().map { list ->
        list.map { it.toDomain() }
    }

    val shortsFlow: Flow<List<VideoItem>> = dao.getShortsFlow().map { list ->
        list.map { it.toDomain() }
    }

    fun getCommentsForVideo(videoId: String): Flow<List<CommentItem>> {
        return dao.getCommentsForVideoFlow(videoId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getVideoById(id: String): VideoItem? {
        return dao.getVideoById(id)?.toDomain()
    }

    // Interactions
    suspend fun setOfflineSaveStatus(videoId: String, isSaved: Boolean) {
        dao.updateOfflineStatus(videoId, isSaved)
    }

    fun toggleOfflineSave(videoId: String, currentStatus: Boolean) {
        scope.launch {
            val newStatus = !currentStatus
            dao.updateOfflineStatus(videoId, newStatus)
            if (newStatus) {
                // Add notification
                addSystemNotification(
                    title = "Offline Video Downloaded",
                    body = "Video is now stored in internal memory and playable without internet.",
                    type = NotificationType.DOWNLOAD_COMPLETE
                )
            }
        }
    }

    fun toggleWatchLater(videoId: String, currentStatus: Boolean) {
        scope.launch {
            dao.updateWatchLater(videoId, !currentStatus)
        }
    }

    fun toggleLike(videoId: String, currentStatus: Boolean) {
        scope.launch {
            val newStatus = !currentStatus
            val delta = if (newStatus) 1 else -1
            dao.updateLiked(videoId, newStatus, delta)
        }
    }

    fun toggleWepe(channelName: String, currentStatus: Boolean) {
        scope.launch {
            val newStatus = !currentStatus
            dao.updateWepedStatus(channelName, newStatus)
            if (newStatus) {
                addSystemNotification(
                    title = "Channel WEPED! 🔔",
                    body = "You are now a WEPER of $channelName. You will receive notifications for new drops.",
                    type = NotificationType.WEPER
                )
            }
        }
    }

    fun recordWatchHistory(videoId: String, progressSec: Int) {
        scope.launch {
            dao.recordWatchHistory(videoId, System.currentTimeMillis(), progressSec)
        }
    }

    fun clearHistory() {
        scope.launch {
            dao.clearHistory()
        }
    }

    fun addComment(videoId: String, text: String) {
        if (text.isBlank()) return
        scope.launch {
            val user = _currentUser.value
            val comment = CommentEntity(
                id = UUID.randomUUID().toString(),
                videoId = videoId,
                authorName = user.displayName,
                authorAvatar = user.avatarUrl,
                text = text.trim(),
                timestamp = "Just now",
                likes = 0,
                isLikedByMe = false,
                isCreatorPinned = false
            )
            dao.insertComment(comment)
        }
    }

    fun toggleCommentLike(commentId: String, currentStatus: Boolean) {
        scope.launch {
            val newStatus = !currentStatus
            val delta = if (newStatus) 1 else -1
            dao.toggleCommentLike(commentId, newStatus, delta)
        }
    }

    fun uploadVideo(
        title: String,
        category: String,
        description: String,
        tags: List<String>,
        thumbnailUrl: String,
        durationSec: Int = 180,
        isShort: Boolean = false
    ) {
        scope.launch {
            val user = _currentUser.value
            val newVideo = VideoItem(
                id = "up_" + UUID.randomUUID().toString().take(8),
                title = title.ifBlank { "Untitled Oscar Video" },
                channelName = user.displayName,
                channelAvatar = user.avatarUrl,
                channelWepers = "${user.wepersCount / 1000}K Wepers",
                isWeped = true,
                thumbnailUrl = thumbnailUrl.ifBlank { "https://images.unsplash.com/photo-1574717024653-61fd2cf4d44d?w=800" },
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                durationSec = durationSec,
                viewsCount = 1,
                uploadDate = "Just now",
                category = category,
                description = description,
                tags = tags,
                likesCount = 0,
                isLiked = false,
                isDisliked = false,
                isWatchLater = false,
                isOfflineSaved = true, // Creators always have their uploads offline available!
                offlineFileSizeMb = 24.5,
                isUserUpload = true,
                isShort = isShort
            )
            dao.insertVideo(VideoEntity.fromDomain(newVideo))

            // Update user total uploads
            _currentUser.value = user.copy(totalUploads = user.totalUploads + 1)

            // Update creator stats
            val stats = _creatorStats.value
            _creatorStats.value = stats.copy(
                totalViews = stats.totalViews + 1,
                watchHours = stats.watchHours + 0.1
            )

            addSystemNotification(
                title = "Video Upload Published! 🚀",
                body = "Your video '$title' is live on OscarTuber and synced with cloud.",
                type = NotificationType.UPLOAD
            )
        }
    }

    fun deleteVideo(videoId: String) {
        scope.launch {
            dao.deleteVideoById(videoId)
        }
    }

    fun toggleOfflineOnlyMode() {
        _isOfflineOnlyMode.value = !_isOfflineOnlyMode.value
    }

    fun performCloudSync() {
        scope.launch {
            _isCloudSyncing.value = true
            kotlinx.coroutines.delay(1200) // Simulated secure cloud handshake
            val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            _lastCloudSyncTime.value = "Synced at $time"
            _isCloudSyncing.value = false
            addSystemNotification(
                title = "OscarCloud Sync Successful ☁️",
                body = "Watch history, subscriptions, offline media tokens, and playlists synced across sessions.",
                type = NotificationType.TRENDING
            )
        }
    }

    fun updateUserProfile(name: String, handle: String, bio: String) {
        _currentUser.value = _currentUser.value.copy(
            displayName = name,
            channelHandle = if (handle.startsWith("@")) handle else "@$handle",
            bio = bio
        )
    }

    fun login(email: String, name: String) {
        _currentUser.value = _currentUser.value.copy(
            email = email,
            displayName = name,
            channelHandle = "@" + name.lowercase().replace(" ", ""),
            isLoggedIn = true
        )
    }

    fun logout() {
        _currentUser.value = _currentUser.value.copy(
            isLoggedIn = false
        )
    }

    fun markNotificationsAsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    private fun addSystemNotification(title: String, body: String, type: NotificationType) {
        val newNotification = NotificationItem(
            id = UUID.randomUUID().toString(),
            title = title,
            body = body,
            timeAgo = "Just now",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100",
            isRead = false,
            type = type
        )
        _notifications.value = listOf(newNotification) + _notifications.value
    }
}
