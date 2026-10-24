package com.example.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material.icons.filled.VideoCameraFront
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.DownloadDone
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.SlowMotionVideo
import androidx.compose.material.icons.outlined.VideoCameraFront
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.OscarGold
import com.example.ui.theme.OscarRed
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun OscarBottomNav(
    currentScreen: ScreenDestination,
    onSelectScreen: (ScreenDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = DarkSurface,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .testTag("oscar_bottom_navigation")
    ) {
        // Home
        NavigationBarItem(
            selected = currentScreen == ScreenDestination.HOME,
            onClick = { onSelectScreen(ScreenDestination.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == ScreenDestination.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text(
                    text = "Home",
                    fontSize = 10.sp,
                    fontWeight = if (currentScreen == ScreenDestination.HOME) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OscarRed,
                selectedTextColor = OscarRed,
                indicatorColor = OscarRed.copy(alpha = 0.15f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("nav_tab_home")
        )

        // Shorts
        NavigationBarItem(
            selected = currentScreen == ScreenDestination.SHORTS,
            onClick = { onSelectScreen(ScreenDestination.SHORTS) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == ScreenDestination.SHORTS) Icons.Filled.SlowMotionVideo else Icons.Outlined.SlowMotionVideo,
                    contentDescription = "Shorts"
                )
            },
            label = {
                Text(
                    text = "Shorts",
                    fontSize = 10.sp,
                    fontWeight = if (currentScreen == ScreenDestination.SHORTS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OscarRed,
                selectedTextColor = OscarRed,
                indicatorColor = OscarRed.copy(alpha = 0.15f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("nav_tab_shorts")
        )

        // Creator Studio
        NavigationBarItem(
            selected = currentScreen == ScreenDestination.CREATOR_STUDIO,
            onClick = { onSelectScreen(ScreenDestination.CREATOR_STUDIO) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == ScreenDestination.CREATOR_STUDIO) Icons.Filled.VideoCameraFront else Icons.Outlined.VideoCameraFront,
                    contentDescription = "Creator Studio"
                )
            },
            label = {
                Text(
                    text = "Studio",
                    fontSize = 10.sp,
                    fontWeight = if (currentScreen == ScreenDestination.CREATOR_STUDIO) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OscarGold,
                selectedTextColor = OscarGold,
                indicatorColor = OscarGold.copy(alpha = 0.15f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("nav_tab_studio")
        )

        // Offline Downloads
        NavigationBarItem(
            selected = currentScreen == ScreenDestination.OFFLINE_DOWNLOADS,
            onClick = { onSelectScreen(ScreenDestination.OFFLINE_DOWNLOADS) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == ScreenDestination.OFFLINE_DOWNLOADS) Icons.Filled.DownloadDone else Icons.Outlined.DownloadDone,
                    contentDescription = "Offline Downloads"
                )
            },
            label = {
                Text(
                    text = "Offline",
                    fontSize = 10.sp,
                    fontWeight = if (currentScreen == ScreenDestination.OFFLINE_DOWNLOADS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF00C853),
                selectedTextColor = Color(0xFF00C853),
                indicatorColor = Color(0xFF00C853).copy(alpha = 0.15f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("nav_tab_offline")
        )

        // Library
        NavigationBarItem(
            selected = currentScreen == ScreenDestination.LIBRARY,
            onClick = { onSelectScreen(ScreenDestination.LIBRARY) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == ScreenDestination.LIBRARY) Icons.Filled.VideoLibrary else Icons.Outlined.VideoLibrary,
                    contentDescription = "Library"
                )
            },
            label = {
                Text(
                    text = "Library",
                    fontSize = 10.sp,
                    fontWeight = if (currentScreen == ScreenDestination.LIBRARY) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OscarRed,
                selectedTextColor = OscarRed,
                indicatorColor = OscarRed.copy(alpha = 0.15f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("nav_tab_library")
        )
    }
}
