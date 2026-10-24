package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OscarDao {
    @Query("SELECT * FROM videos ORDER BY lastWatchedTimestamp DESC, id ASC")
    fun getAllVideosFlow(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE id = :id LIMIT 1")
    suspend fun getVideoById(id: String): VideoEntity?

    @Query("SELECT * FROM videos WHERE isOfflineSaved = 1 ORDER BY id ASC")
    fun getOfflineVideosFlow(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE isWatchLater = 1 ORDER BY id ASC")
    fun getWatchLaterVideosFlow(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE lastWatchedTimestamp > 0 ORDER BY lastWatchedTimestamp DESC")
    fun getWatchHistoryFlow(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE isLiked = 1 ORDER BY id ASC")
    fun getLikedVideosFlow(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE isUserUpload = 1 ORDER BY id DESC")
    fun getUserUploadsFlow(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE isShort = 1 ORDER BY id ASC")
    fun getShortsFlow(): Flow<List<VideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity)

    @Update
    suspend fun updateVideo(video: VideoEntity)

    @Query("DELETE FROM videos WHERE id = :id")
    suspend fun deleteVideoById(id: String)

    @Query("UPDATE videos SET isOfflineSaved = :isSaved WHERE id = :id")
    suspend fun updateOfflineStatus(id: String, isSaved: Boolean)

    @Query("UPDATE videos SET isWatchLater = :isWatchLater WHERE id = :id")
    suspend fun updateWatchLater(id: String, isWatchLater: Boolean)

    @Query("UPDATE videos SET isLiked = :isLiked, likesCount = likesCount + :likeDelta WHERE id = :id")
    suspend fun updateLiked(id: String, isLiked: Boolean, likeDelta: Int)

    @Query("UPDATE videos SET isWeped = :isWeped WHERE channelName = :channelName")
    suspend fun updateWepedStatus(channelName: String, isWeped: Boolean)

    @Query("UPDATE videos SET lastWatchedTimestamp = :timestamp, watchProgressSec = :progressSec, viewsCount = viewsCount + 1 WHERE id = :id")
    suspend fun recordWatchHistory(id: String, timestamp: Long, progressSec: Int)

    @Query("UPDATE videos SET lastWatchedTimestamp = 0, watchProgressSec = 0")
    suspend fun clearHistory()

    // Comments
    @Query("SELECT * FROM comments WHERE videoId = :videoId ORDER BY isCreatorPinned DESC, id DESC")
    fun getCommentsForVideoFlow(videoId: String): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    @Query("UPDATE comments SET isLikedByMe = :isLiked, likes = likes + :delta WHERE id = :commentId")
    suspend fun toggleCommentLike(commentId: String, isLiked: Boolean, delta: Int)
}
