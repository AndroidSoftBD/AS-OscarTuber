package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AuthDialog
import com.example.ui.components.GitHubAboutDialog
import com.example.ui.components.MiniPlayerBar
import com.example.ui.components.NotificationsDialog
import com.example.ui.components.OscarBottomNav
import com.example.ui.components.OscarTopBar
import com.example.ui.components.UploadVideoDialog
import com.example.ui.screens.CreatorStudioScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.OfflineDownloadsScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.ShortsScreen
import com.example.ui.screens.VideoDetailScreen
import com.example.ui.screens.VideoEditorScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.OscarTuberTheme
import com.example.ui.viewmodel.OscarViewModel
import com.example.ui.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OscarTuberTheme {
                OscarTuberApp()
            }
        }
    }
}

@Composable
fun OscarTuberApp(viewModel: OscarViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val activeVideo by viewModel.activeVideo.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val playbackPositionSec by viewModel.playbackPositionSec.collectAsStateWithLifecycle()
    val playbackSpeed by viewModel.playbackSpeed.collectAsStateWithLifecycle()
    val selectedQuality by viewModel.selectedQuality.collectAsStateWithLifecycle()
    val isMiniPlayer by viewModel.isMiniPlayer.collectAsStateWithLifecycle()
    val isFullscreen by viewModel.isFullscreen.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearchActive by viewModel.isSearchActive.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    val filteredFeed by viewModel.filteredFeed.collectAsStateWithLifecycle()
    val allVideos by viewModel.allVideos.collectAsStateWithLifecycle()
    val offlineVideos by viewModel.offlineVideos.collectAsStateWithLifecycle()
    val watchLaterVideos by viewModel.watchLaterVideos.collectAsStateWithLifecycle()
    val watchHistory by viewModel.watchHistory.collectAsStateWithLifecycle()
    val likedVideos by viewModel.likedVideos.collectAsStateWithLifecycle()
    val userUploads by viewModel.userUploads.collectAsStateWithLifecycle()
    val shortsList by viewModel.shortsList.collectAsStateWithLifecycle()

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val creatorStats by viewModel.creatorStats.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val isCloudSyncing by viewModel.isCloudSyncing.collectAsStateWithLifecycle()
    val lastCloudSyncTime by viewModel.lastCloudSyncTime.collectAsStateWithLifecycle()
    val isOfflineOnlyMode by viewModel.isOfflineOnlyMode.collectAsStateWithLifecycle()

    val activeComments by viewModel.activeComments.collectAsStateWithLifecycle()
    val editConfig by viewModel.editConfig.collectAsStateWithLifecycle()

    val showAuthDialog by viewModel.showAuthDialog.collectAsStateWithLifecycle()
    val showNotificationsDialog by viewModel.showNotificationsDialog.collectAsStateWithLifecycle()
    val showGitHubAboutDialog by viewModel.showGitHubAboutDialog.collectAsStateWithLifecycle()
    val showUploadSheet by viewModel.showUploadSheet.collectAsStateWithLifecycle()
    val showVideoEditor by viewModel.showVideoEditor.collectAsStateWithLifecycle()

    val unreadNotifications = notifications.count { !it.isRead }

    // System back navigation handling
    BackHandler(
        enabled = showVideoEditor ||
                isSearchActive ||
                (activeVideo != null && !isMiniPlayer) ||
                currentScreen != ScreenDestination.HOME
    ) {
        when {
            showVideoEditor -> viewModel.setShowVideoEditor(false)
            isSearchActive -> viewModel.setSearchActive(false)
            activeVideo != null && !isMiniPlayer -> viewModel.toggleMiniPlayer()
            currentScreen != ScreenDestination.HOME -> viewModel.selectScreen(ScreenDestination.HOME)
        }
    }

    val isFullPlayerActive = activeVideo != null && !isMiniPlayer

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        topBar = {
            if (!isFullPlayerActive && !isSearchActive && !showVideoEditor) {
                OscarTopBar(
                    user = currentUser,
                    unreadNotificationCount = unreadNotifications,
                    isCloudSyncing = isCloudSyncing,
                    isOfflineOnlyMode = isOfflineOnlyMode,
                    onToggleOfflineOnly = { viewModel.toggleOfflineOnlyMode() },
                    onTriggerCloudSync = { viewModel.triggerCloudSync() },
                    onSearchClick = { viewModel.setSearchActive(true) },
                    onNotificationsClick = { viewModel.setShowNotificationsDialog(true) },
                    onGitHubClick = { viewModel.setShowGitHubAboutDialog(true) },
                    onProfileClick = { viewModel.setShowAuthDialog(true) }
                )
            }
        },
        bottomBar = {
            if (!isFullPlayerActive && !showVideoEditor) {
                Column {
                    // Miniplayer dock above navigation bar
                    if (isMiniPlayer && activeVideo != null) {
                        MiniPlayerBar(
                            video = activeVideo!!,
                            isPlaying = isPlaying,
                            playbackPositionSec = playbackPositionSec,
                            onExpand = { viewModel.toggleMiniPlayer() },
                            onTogglePlayPause = { viewModel.togglePlayPause() },
                            onClose = { viewModel.closeActiveVideo() }
                        )
                    }

                    OscarBottomNav(
                        currentScreen = currentScreen,
                        onSelectScreen = { viewModel.selectScreen(it) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                // Video Editor Studio
                showVideoEditor -> {
                    VideoEditorScreen(
                        config = editConfig,
                        onUpdateFilter = { viewModel.updateEditorFilter(it) },
                        onUpdateTrim = { s, e -> viewModel.updateEditorTrim(s, e) },
                        onUpdateSpeed = { viewModel.updateEditorSpeed(it) },
                        onUpdateVolume = { viewModel.updateEditorVolume(it) },
                        onUpdateWatermark = { viewModel.updateEditorWatermark(it) },
                        onExportAndPublish = { title, cat, desc, tags, thumb ->
                            viewModel.publishUploadedVideo(title, cat, desc, tags, thumb, isShort = false)
                        },
                        onClose = { viewModel.setShowVideoEditor(false) }
                    )
                }

                // Search Screen
                isSearchActive -> {
                    SearchScreen(
                        searchQuery = searchQuery,
                        searchResults = filteredFeed,
                        onQueryChange = { viewModel.setSearchQuery(it) },
                        onVideoClick = {
                            viewModel.openVideo(it)
                            viewModel.setSearchActive(false)
                        },
                        onBack = { viewModel.setSearchActive(false) }
                    )
                }

                // Video Detail & Playback Screen
                isFullPlayerActive -> {
                    VideoDetailScreen(
                        video = activeVideo!!,
                        isPlaying = isPlaying,
                        playbackPositionSec = playbackPositionSec,
                        playbackSpeed = playbackSpeed,
                        selectedQuality = selectedQuality,
                        isFullscreen = isFullscreen,
                        isOfflineOnly = isOfflineOnlyMode,
                        currentUser = currentUser,
                        comments = activeComments,
                        relatedVideos = allVideos,
                        onTogglePlayPause = { viewModel.togglePlayPause() },
                        onSeekTo = { viewModel.seekTo(it) },
                        onSkipForward10 = { viewModel.skipForward10() },
                        onSkipBackward10 = { viewModel.skipBackward10() },
                        onSetPlaybackSpeed = { viewModel.setPlaybackSpeed(it) },
                        onSetQuality = { viewModel.setQuality(it) },
                        onToggleFullscreen = { viewModel.toggleFullscreen() },
                        onMinimize = { viewModel.toggleMiniPlayer() },
                        onClose = { viewModel.closeActiveVideo() },
                        onToggleLike = { viewModel.toggleLike(activeVideo!!) },
                        onToggleWatchLater = { viewModel.toggleWatchLater(activeVideo!!) },
                        onToggleOffline = { viewModel.toggleOfflineSave(activeVideo!!) },
                        onToggleWepe = { viewModel.toggleWepe(activeVideo!!.channelName, activeVideo!!.isWeped) },
                        onPostComment = { viewModel.postComment(it) },
                        onToggleCommentLike = { id, liked -> viewModel.toggleCommentLike(id, liked) },
                        onSelectRelatedVideo = { viewModel.openVideo(it) },
                        onOpenVideoEditor = { viewModel.setShowVideoEditor(true) }
                    )
                }

                // Primary Bottom Tabs
                else -> {
                    when (currentScreen) {
                        ScreenDestination.HOME -> {
                            HomeScreen(
                                videos = filteredFeed,
                                offlineVideosCount = offlineVideos.size,
                                selectedCategory = selectedCategory,
                                isOfflineOnly = isOfflineOnlyMode,
                                onSelectCategory = { viewModel.selectCategory(it) },
                                onVideoClick = { viewModel.openVideo(it) },
                                onToggleOffline = { viewModel.toggleOfflineSave(it) },
                                onToggleWatchLater = { viewModel.toggleWatchLater(it) },
                                onToggleWepe = { name, weped -> viewModel.toggleWepe(name, weped) },
                                onNavigateToDownloads = { viewModel.selectScreen(ScreenDestination.OFFLINE_DOWNLOADS) }
                            )
                        }

                        ScreenDestination.SHORTS -> {
                            ShortsScreen(
                                shorts = shortsList,
                                onToggleLike = { viewModel.toggleLike(it) },
                                onToggleSaveShort = { viewModel.toggleWatchLater(it) },
                                onToggleWepe = { name, weped -> viewModel.toggleWepe(name, weped) },
                                onOpenComments = { viewModel.openVideo(it) },
                                onCreateShortClick = { viewModel.setShowUploadSheet(true) }
                            )
                        }

                        ScreenDestination.CREATOR_STUDIO -> {
                            CreatorStudioScreen(
                                user = currentUser,
                                stats = creatorStats,
                                userUploads = userUploads,
                                onOpenUploadSheet = { viewModel.setShowUploadSheet(true) },
                                onOpenVideoEditor = { viewModel.setShowVideoEditor(true) },
                                onPlayVideo = { viewModel.openVideo(it) },
                                onDeleteUpload = { viewModel.deleteUpload(it.id) }
                            )
                        }

                        ScreenDestination.OFFLINE_DOWNLOADS -> {
                            OfflineDownloadsScreen(
                                offlineVideos = offlineVideos,
                                isOfflineOnly = isOfflineOnlyMode,
                                onToggleOfflineOnly = { viewModel.toggleOfflineOnlyMode() },
                                onPlayVideo = { viewModel.openVideo(it) },
                                onDeleteDownload = { viewModel.toggleOfflineSave(it) },
                                onNavigateToHome = { viewModel.selectScreen(ScreenDestination.HOME) }
                            )
                        }

                        ScreenDestination.LIBRARY -> {
                            LibraryScreen(
                                user = currentUser,
                                historyVideos = watchHistory,
                                watchLaterVideos = watchLaterVideos,
                                likedVideos = likedVideos,
                                offlineVideos = offlineVideos,
                                userUploads = userUploads,
                                isCloudSyncing = isCloudSyncing,
                                lastCloudSyncTime = lastCloudSyncTime,
                                onTriggerCloudSync = { viewModel.triggerCloudSync() },
                                onClearHistory = { viewModel.clearHistory() },
                                onPlayVideo = { viewModel.openVideo(it) },
                                onNavigateToDownloads = { viewModel.selectScreen(ScreenDestination.OFFLINE_DOWNLOADS) },
                                onNavigateToStudio = { viewModel.selectScreen(ScreenDestination.CREATOR_STUDIO) },
                                onOpenAuth = { viewModel.setShowAuthDialog(true) },
                                onOpenGitHubAbout = { viewModel.setShowGitHubAboutDialog(true) }
                            )
                        }
                    }
                }
            }

            // Dialogs
            if (showAuthDialog) {
                AuthDialog(
                    user = currentUser,
                    onDismiss = { viewModel.setShowAuthDialog(false) },
                    onLogin = { email, name -> viewModel.repository.login(email, name) },
                    onLogout = { viewModel.repository.logout() }
                )
            }

            if (showNotificationsDialog) {
                NotificationsDialog(
                    notifications = notifications,
                    onDismiss = { viewModel.setShowNotificationsDialog(false) }
                )
            }

            if (showGitHubAboutDialog) {
                GitHubAboutDialog(
                    onDismiss = { viewModel.setShowGitHubAboutDialog(false) }
                )
            }

            if (showUploadSheet) {
                UploadVideoDialog(
                    onDismiss = { viewModel.setShowUploadSheet(false) },
                    onPublish = { title, cat, desc, tags, thumb, isShort ->
                        viewModel.publishUploadedVideo(title, cat, desc, tags, thumb, isShort)
                    }
                )
            }
        }
    }
}
