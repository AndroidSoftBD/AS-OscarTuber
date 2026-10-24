package com.example.data.model

data class VideoItem(
    val id: String,
    val title: String,
    val channelName: String,
    val channelAvatar: String,
    val channelWepers: String,
    val isWeped: Boolean = false,
    val thumbnailUrl: String,
    val videoUrl: String,
    val durationSec: Int = 180,
    val viewsCount: Long = 120000,
    val uploadDate: String = "2 hours ago",
    val category: String = "Tech",
    val description: String = "",
    val tags: List<String> = emptyList(),
    val likesCount: Long = 8500,
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val isWatchLater: Boolean = false,
    val isOfflineSaved: Boolean = false,
    val offlineFileSizeMb: Double = 42.5,
    val watchProgressSec: Int = 0,
    val lastWatchedTimestamp: Long = 0L,
    val isUserUpload: Boolean = false,
    val isShort: Boolean = false
) {
    val formattedDuration: String
        get() {
            val minutes = durationSec / 60
            val seconds = durationSec % 60
            return "%d:%02d".format(minutes, seconds)
        }

    val formattedViews: String
        get() {
            return when {
                viewsCount >= 1_000_000 -> "%.1fM views".format(viewsCount / 1_000_000.0)
                viewsCount >= 1_000 -> "%dK views".format(viewsCount / 1000)
                else -> "$viewsCount views"
            }
        }
}

data class CommentItem(
    val id: String,
    val videoId: String,
    val authorName: String,
    val authorAvatar: String,
    val text: String,
    val timestamp: String = "Just now",
    val likes: Int = 0,
    val isLikedByMe: Boolean = false,
    val isCreatorPinned: Boolean = false
)

data class UserProfile(
    val id: String = "user_default",
    val displayName: String = "Alex Rivera",
    val channelHandle: String = "@alexcreates",
    val email: String = "alex.rivera@oscartuber.com",
    val avatarUrl: String = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
    val wepersCount: Int = 14200,
    val totalUploads: Int = 8,
    val isLoggedIn: Boolean = true,
    val bio: String = "Filmmaker, visual storyteller & tech explorer on OscarTuber."
)

data class CreatorStats(
    val totalViews: Long = 348200,
    val totalWepers: Int = 14200,
    val watchHours: Double = 1850.4,
    val estimatedRevenue: Double = 1248.50,
    val monthlyGrowthRate: Double = 18.5,
    val recentDaysViews: List<Pair<String, Int>> = listOf(
        "Mon" to 1420,
        "Tue" to 2180,
        "Wed" to 1950,
        "Thu" to 3100,
        "Fri" to 4250,
        "Sat" to 5800,
        "Sun" to 4900
    )
)

data class NotificationItem(
    val id: String,
    val title: String,
    val body: String,
    val timeAgo: String,
    val avatarUrl: String,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.WEPER
)

enum class NotificationType {
    WEPER,
    UPLOAD,
    COMMENT,
    TRENDING,
    DOWNLOAD_COMPLETE
}

data class VideoEditConfig(
    val trimStartMs: Float = 0f,
    val trimEndMs: Float = 100f,
    val filterType: VideoFilter = VideoFilter.CINEMA_WARM,
    val playbackSpeed: Float = 1.0f,
    val audioVolume: Float = 1.0f,
    val isMuted: Boolean = false,
    val watermarkText: String = "OscarTuber Studio"
)

enum class VideoFilter(val displayName: String, val description: String) {
    NORMAL("Normal", "Original natural colors"),
    CINEMA_WARM("Cinema Warm", "Rich filmic warmth & amber glow"),
    OSCAR_GOLD("Oscar Gold", "Golden prestige cinema grading"),
    NOIR_MONO("Noir Mono", "High-contrast vintage black & white"),
    CYBER_NEON("Cyber Neon", "Vibrant saturated cyberpunk hues"),
    VINTAGE_SEPIA("Vintage Sepia", "Classic retro 1970s film roll")
}
