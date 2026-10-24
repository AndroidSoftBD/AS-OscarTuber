package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbDownOffAlt
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.CommentItem
import com.example.data.model.UserProfile
import com.example.data.model.VideoItem
import com.example.ui.components.OscarPlayer
import com.example.ui.components.VideoCard
import com.example.ui.components.WeperButton
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceHighlight
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.OscarEmerald
import com.example.ui.theme.OscarGold
import com.example.ui.theme.OscarRed

@Composable
fun VideoDetailScreen(
    video: VideoItem,
    isPlaying: Boolean,
    playbackPositionSec: Int,
    playbackSpeed: Float,
    selectedQuality: String,
    isFullscreen: Boolean,
    isOfflineOnly: Boolean,
    currentUser: UserProfile,
    comments: List<CommentItem>,
    relatedVideos: List<VideoItem>,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onSkipForward10: () -> Unit,
    onSkipBackward10: () -> Unit,
    onSetPlaybackSpeed: (Float) -> Unit,
    onSetQuality: (String) -> Unit,
    onToggleFullscreen: () -> Unit,
    onMinimize: () -> Unit,
    onClose: () -> Unit,
    onToggleLike: () -> Unit,
    onToggleWatchLater: () -> Unit,
    onToggleOffline: () -> Unit,
    onToggleWepe: () -> Unit,
    onPostComment: (String) -> Unit,
    onToggleCommentLike: (String, Boolean) -> Unit,
    onSelectRelatedVideo: (VideoItem) -> Unit,
    onOpenVideoEditor: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDescriptionExpanded by remember { mutableStateOf(false) }
    var newCommentText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkSurface)
    ) {
        // Video Player Container
        OscarPlayer(
            video = video,
            isPlaying = isPlaying,
            playbackPositionSec = playbackPositionSec,
            playbackSpeed = playbackSpeed,
            selectedQuality = selectedQuality,
            isFullscreen = isFullscreen,
            isOfflineOnly = isOfflineOnly,
            onTogglePlayPause = onTogglePlayPause,
            onSeekTo = onSeekTo,
            onSkipForward10 = onSkipForward10,
            onSkipBackward10 = onSkipBackward10,
            onSetPlaybackSpeed = onSetPlaybackSpeed,
            onSetQuality = onSetQuality,
            onToggleFullscreen = onToggleFullscreen,
            onMinimize = onMinimize,
            onClose = onClose
        )

        if (!isFullscreen) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("video_detail_scroll_list"),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Video Title
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = video.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${video.formattedViews} • ${video.uploadDate} • #${video.category}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Channel Header & WEPER Button
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = video.channelAvatar,
                            contentDescription = video.channelName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = video.channelName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = OscarGold,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Text(
                                text = video.channelWepers,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Prominent WEPER button
                        WeperButton(
                            isWeped = video.isWeped,
                            onToggleWepe = onToggleWepe
                        )
                    }
                }

                // Action Bar (Like, Dislike, Offline Download, Watch Later, Remix/Edit)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Like Button Pill
                        Surface(
                            color = DarkSurfaceVariant,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .clickable { onToggleLike() }
                                .testTag("detail_like_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (video.isLiked) Icons.Default.ThumbUp else Icons.Default.ThumbUpOffAlt,
                                    contentDescription = "Like",
                                    tint = if (video.isLiked) OscarRed else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${video.likesCount / 1000}K",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (video.isLiked) OscarRed else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Offline Download Button
                        Surface(
                            color = if (video.isOfflineSaved) OscarEmerald.copy(alpha = 0.2f) else DarkSurfaceVariant,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .clickable { onToggleOffline() }
                                .testTag("detail_offline_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (video.isOfflineSaved) Icons.Default.DownloadDone else Icons.Default.FileDownload,
                                    contentDescription = "Offline Download",
                                    tint = if (video.isOfflineSaved) OscarEmerald else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (video.isOfflineSaved) "Saved" else "Offline",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (video.isOfflineSaved) OscarEmerald else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Watch Later Button
                        Surface(
                            color = if (video.isWatchLater) OscarGold.copy(alpha = 0.2f) else DarkSurfaceVariant,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .clickable { onToggleWatchLater() }
                                .testTag("detail_watch_later_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WatchLater,
                                    contentDescription = "Watch Later",
                                    tint = if (video.isWatchLater) OscarGold else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (video.isWatchLater) "Saved" else "Later",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (video.isWatchLater) OscarGold else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Video Editor / Remix Studio
                        Surface(
                            color = DarkSurfaceVariant,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .clickable { onOpenVideoEditor() }
                                .testTag("detail_remix_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoFixHigh,
                                    contentDescription = "Edit Video",
                                    tint = OscarGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Remix",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OscarGold
                                )
                            }
                        }
                    }
                }

                // Collapsible Description
                item {
                    Surface(
                        color = DarkSurfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Description & Tags",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = OscarGold
                                )
                                Icon(
                                    imageVector = if (isDescriptionExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = video.description,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = if (isDescriptionExpanded) 20 else 2
                            )

                            if (video.tags.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    video.tags.take(4).forEach { tag ->
                                        Text(
                                            text = "#$tag",
                                            fontSize = 11.sp,
                                            color = OscarRed,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Realtime Comments Section Header
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Comments",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${comments.size}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Add Comment input row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = currentUser.avatarUrl,
                                contentDescription = currentUser.displayName,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = newCommentText,
                                onValueChange = { newCommentText = it },
                                placeholder = { Text("Add a comment...", fontSize = 12.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(
                                    onSend = {
                                        if (newCommentText.isNotBlank()) {
                                            onPostComment(newCommentText)
                                            newCommentText = ""
                                        }
                                    }
                                ),
                                trailingIcon = {
                                    if (newCommentText.isNotBlank()) {
                                        IconButton(
                                            onClick = {
                                                onPostComment(newCommentText)
                                                newCommentText = ""
                                            },
                                            modifier = Modifier.testTag("send_comment_btn")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Send,
                                                contentDescription = "Post comment",
                                                tint = OscarRed
                                            )
                                        }
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = OscarRed,
                                    unfocusedBorderColor = DarkBorder,
                                    focusedContainerColor = DarkSurfaceVariant,
                                    unfocusedContainerColor = DarkSurfaceVariant
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("comment_input_field")
                            )
                        }
                    }
                }

                // Comments List
                items(comments, key = { it.id }) { comment ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            AsyncImage(
                                model = comment.authorAvatar,
                                contentDescription = comment.authorName,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = comment.authorName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = comment.timestamp,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (comment.isCreatorPinned) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(OscarGold.copy(alpha = 0.2f))
                                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PushPin,
                                                contentDescription = "Pinned",
                                                tint = OscarGold,
                                                modifier = Modifier.size(10.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = "PINNED",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = OscarGold
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = comment.text,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 16.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable {
                                            onToggleCommentLike(comment.id, comment.isLikedByMe)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (comment.isLikedByMe) Icons.Default.ThumbUp else Icons.Default.ThumbUpOffAlt,
                                            contentDescription = "Like comment",
                                            tint = if (comment.isLikedByMe) OscarRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        if (comment.likes > 0) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = comment.likes.toString(),
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Reply",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                // Related Videos divider
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = DarkBorder, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Related Videos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                // Related videos
                items(relatedVideos.filter { it.id != video.id }, key = { "rel_${it.id}" }) { rel ->
                    VideoCard(
                        video = rel,
                        onClick = { onSelectRelatedVideo(rel) },
                        onToggleOffline = { /* toggle */ },
                        onToggleWatchLater = { /* toggle */ },
                        onToggleWepe = { /* toggle */ }
                    )
                }
            }
        }
    }
}
