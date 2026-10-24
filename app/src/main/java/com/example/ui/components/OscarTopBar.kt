package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.UserProfile
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.OscarEmerald
import com.example.ui.theme.OscarGold
import com.example.ui.theme.OscarRed

@Composable
fun OscarTopBar(
    user: UserProfile,
    unreadNotificationCount: Int,
    isCloudSyncing: Boolean,
    isOfflineOnlyMode: Boolean,
    onToggleOfflineOnly: () -> Unit,
    onTriggerCloudSync: () -> Unit,
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onGitHubClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sync_spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Surface(
        color = DarkSurface,
        tonalElevation = 3.dp,
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onProfileClick() }
                    .padding(4.dp)
                    .testTag("app_branding_header")
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(OscarRed, Color(0xFF88001B))
                            )
                        )
                        .border(1.dp, OscarGold.copy(alpha = 0.6f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▶",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Oscar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp,
                            color = Color.White
                        )
                        Text(
                            text = "Tuber",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp,
                            color = OscarRed
                        )
                    }
                    Text(
                        text = if (isOfflineOnlyMode) "● OFFLINE WATCH" else "● LIVE & CLOUD",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isOfflineOnlyMode) OscarGold else OscarEmerald,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Offline Mode Simulator Toggle Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isOfflineOnlyMode) OscarGold.copy(alpha = 0.2f) else DarkSurfaceVariant)
                    .border(
                        1.dp,
                        if (isOfflineOnlyMode) OscarGold else DarkBorder,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onToggleOfflineOnly() }
                    .padding(horizontal = 8.dp, vertical = 5.dp)
                    .testTag("offline_toggle_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isOfflineOnlyMode) Icons.Default.WifiOff else Icons.Default.Wifi,
                        contentDescription = "Toggle Network Mode",
                        tint = if (isOfflineOnlyMode) OscarGold else OscarEmerald,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isOfflineOnlyMode) "Offline" else "Online",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isOfflineOnlyMode) OscarGold else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Cloud Sync Button
            IconButton(
                onClick = onTriggerCloudSync,
                modifier = Modifier
                    .size(38.dp)
                    .testTag("cloud_sync_button")
            ) {
                Icon(
                    imageVector = if (isCloudSyncing) Icons.Default.CloudSync else Icons.Default.CloudDone,
                    contentDescription = "Cloud Sync",
                    tint = if (isCloudSyncing) OscarGold else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = if (isCloudSyncing) Modifier.rotate(rotation) else Modifier
                )
            }

            // Search Button
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(38.dp)
                    .testTag("search_action_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search videos",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            // Notification Center with Badge
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier
                    .size(38.dp)
                    .testTag("notifications_button")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadNotificationCount > 0) {
                            Badge(
                                containerColor = OscarRed,
                                contentColor = Color.White
                            ) {
                                Text(unreadNotificationCount.toString(), fontSize = 10.sp)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // GitHub & Platform download info
            IconButton(
                onClick = onGitHubClick,
                modifier = Modifier
                    .size(38.dp)
                    .testTag("github_about_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = "GitHub & App Downloads",
                    tint = OscarGold
                )
            }

            // User Profile Avatar
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = "User Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, OscarRed, CircleShape)
                    .clickable { onProfileClick() }
                    .testTag("profile_avatar_button")
            )
        }
    }
}
