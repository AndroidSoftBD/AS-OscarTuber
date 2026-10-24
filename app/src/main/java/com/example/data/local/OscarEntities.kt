package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.CommentItem
import com.example.data.model.VideoItem

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val channelName: String,
    val channelAvatar: String,
    val channelWepers: String,
    val isWeped: Boolean = false,
    val thumbnailUrl: String,
    val videoUrl: String,
    val durationSec: Int = 180,
    val viewsCount: Long = 100000,
    val uploadDate: String = "Today",
    val category: String = "Tech",
    val description: String = "",
    val tagsRaw: String = "",
    val likesCount: Long = 5000,
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val isWatchLater: Boolean = false,
    val isOfflineSaved: Boolean = false,
    val offlineFileSizeMb: Double = 35.0,
    val watchProgressSec: Int = 0,
    val lastWatchedTimestamp: Long = 0L,
    val isUserUpload: Boolean = false,
    val isShort: Boolean = false
) {
    fun toDomain(): VideoItem = VideoItem(
        id = id,
        title = title,
        channelName = channelName,
        channelAvatar = channelAvatar,
        channelWepers = channelWepers,
        isWeped = isWeped,
        thumbnailUrl = thumbnailUrl,
        videoUrl = videoUrl,
        durationSec = durationSec,
        viewsCount = viewsCount,
        uploadDate = uploadDate,
        category = category,
        description = description,
        tags = if (tagsRaw.isBlank()) emptyList() else tagsRaw.split(","),
        likesCount = likesCount,
        isLiked = isLiked,
        isDisliked = isDisliked,
        isWatchLater = isWatchLater,
        isOfflineSaved = isOfflineSaved,
        offlineFileSizeMb = offlineFileSizeMb,
        watchProgressSec = watchProgressSec,
        lastWatchedTimestamp = lastWatchedTimestamp,
        isUserUpload = isUserUpload,
        isShort = isShort
    )

    companion object {
        fun fromDomain(item: VideoItem): VideoEntity = VideoEntity(
            id = item.id,
            title = item.title,
            channelName = item.channelName,
            channelAvatar = item.channelAvatar,
            channelWepers = item.channelWepers,
            isWeped = item.isWeped,
            thumbnailUrl = item.thumbnailUrl,
            videoUrl = item.videoUrl,
            durationSec = item.durationSec,
            viewsCount = item.viewsCount,
            uploadDate = item.uploadDate,
            category = item.category,
            description = item.description,
            tagsRaw = item.tags.joinToString(","),
            likesCount = item.likesCount,
            isLiked = item.isLiked,
            isDisliked = item.isDisliked,
            isWatchLater = item.isWatchLater,
            isOfflineSaved = item.isOfflineSaved,
            offlineFileSizeMb = item.offlineFileSizeMb,
            watchProgressSec = item.watchProgressSec,
            lastWatchedTimestamp = item.lastWatchedTimestamp,
            isUserUpload = item.isUserUpload,
            isShort = item.isShort
        )
    }
}

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey val id: String,
    val videoId: String,
    val authorName: String,
    val authorAvatar: String,
    val text: String,
    val timestamp: String,
    val likes: Int = 0,
    val isLikedByMe: Boolean = false,
    val isCreatorPinned: Boolean = false
) {
    fun toDomain(): CommentItem = CommentItem(
        id = id,
        videoId = videoId,
        authorName = authorName,
        authorAvatar = authorAvatar,
        text = text,
        timestamp = timestamp,
        likes = likes,
        isLikedByMe = isLikedByMe,
        isCreatorPinned = isCreatorPinned
    )

    companion object {
        fun fromDomain(item: CommentItem): CommentEntity = CommentEntity(
            id = item.id,
            videoId = item.videoId,
            authorName = item.authorName,
            authorAvatar = item.authorAvatar,
            text = item.text,
            timestamp = item.timestamp,
            likes = item.likes,
            isLikedByMe = item.isLikedByMe,
            isCreatorPinned = item.isCreatorPinned
        )
    }
}
