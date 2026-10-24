package com.example.data.repository

import com.example.data.model.CommentItem
import com.example.data.model.NotificationItem
import com.example.data.model.NotificationType
import com.example.data.model.VideoItem

object SampleData {
    val initialVideos = listOf(
        VideoItem(
            id = "v1",
            title = "Oscar Cinema Masterclass: How Hollywood Lights Impossible Scenes",
            channelName = "CineSphere Pro",
            channelAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
            channelWepers = "1.8M Wepers",
            isWeped = true,
            thumbnailUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=800",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            durationSec = 742,
            viewsCount = 1420000,
            uploadDate = "3 hours ago",
            category = "Cinema",
            description = "Explore Academy Award-winning cinematography techniques, anamorphic lens flares, and volumetric lighting used in modern blockbusters. Downloaded and ready for offline masterclass study.",
            tags = listOf("Cinematography", "Lighting", "Filmmaking", "Hollywood", "Oscar"),
            likesCount = 89000,
            isLiked = true,
            isOfflineSaved = true,
            offlineFileSizeMb = 145.2,
            watchProgressSec = 120,
            lastWatchedTimestamp = System.currentTimeMillis() - 3600000
        ),
        VideoItem(
            id = "v2",
            title = "Quantum Computing Explained to a 10-Year-Old (With Lego)",
            channelName = "DeepTech Frontier",
            channelAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            channelWepers = "940K Wepers",
            isWeped = false,
            thumbnailUrl = "https://images.unsplash.com/photo-1635070041078-e363dbe005cb?w=800",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            durationSec = 524,
            viewsCount = 680000,
            uploadDate = "Yesterday",
            category = "Tech",
            description = "Qubits, entanglement, and superposition demonstrated with simple visual models. Discover how quantum supremacy will disrupt cryptography and medicine.",
            tags = listOf("Quantum", "Physics", "Computing", "Future Tech"),
            likesCount = 47000,
            isOfflineSaved = false,
            offlineFileSizeMb = 98.4
        ),
        VideoItem(
            id = "v3",
            title = "Building a Photorealistic Open World Game in Unreal Engine 5.5",
            channelName = "PixelSmith Studios",
            channelAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150",
            channelWepers = "2.4M Wepers",
            isWeped = true,
            thumbnailUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            durationSec = 1120,
            viewsCount = 2890000,
            uploadDate = "2 days ago",
            category = "Gaming",
            description = "Step-by-step procedural foliage generation, Nanite mesh optimization, and Lumen dynamic global illumination test on RTX 5090.",
            tags = listOf("UnrealEngine", "GameDev", "C++", "RTX", "3D"),
            likesCount = 210000,
            isOfflineSaved = true,
            offlineFileSizeMb = 210.0,
            isWatchLater = true
        ),
        VideoItem(
            id = "v4",
            title = "Synthwave Chill Beats for Deep Focus & Midnight Coding [24/7]",
            channelName = "Aesthetic Frequencies",
            channelAvatar = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=150",
            channelWepers = "3.1M Wepers",
            isWeped = false,
            thumbnailUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            durationSec = 3600,
            viewsCount = 4500000,
            uploadDate = "Streamed live",
            category = "Music",
            description = "Relaxing analog synthesizer soundscapes, retro 80s drums, and warm tape saturation designed to help you focus and study without distractions.",
            tags = listOf("Synthwave", "Lofi", "CodingMusic", "Focus"),
            likesCount = 380000,
            isOfflineSaved = false,
            offlineFileSizeMb = 450.0
        ),
        VideoItem(
            id = "v5",
            title = "Complete Jetpack Compose Mastery: Architecture, Canvas & State",
            channelName = "Android Architect",
            channelAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
            channelWepers = "410K Wepers",
            isWeped = true,
            thumbnailUrl = "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            durationSec = 1840,
            viewsCount = 312000,
            uploadDate = "4 days ago",
            category = "Coding",
            description = "Deep dive into Compose compiler recomposition skip logic, derivedStateOf, custom DrawScope modifiers, and M3 design system implementation.",
            tags = listOf("Android", "Kotlin", "Compose", "MobileDev"),
            likesCount = 28400,
            isOfflineSaved = true,
            offlineFileSizeMb = 315.6,
            isWatchLater = true,
            watchProgressSec = 450,
            lastWatchedTimestamp = System.currentTimeMillis() - 7200000
        ),
        VideoItem(
            id = "v6",
            title = "The Secret Chemistry Behind Specialty Espresso Extraction",
            channelName = "Craft & Roast",
            channelAvatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150",
            channelWepers = "890K Wepers",
            isWeped = false,
            thumbnailUrl = "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=800",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
            durationSec = 612,
            viewsCount = 590000,
            uploadDate = "1 week ago",
            category = "Originals",
            description = "Water hardness TDS, grind distribution bimodal peaks, and pressure profiling curves explained for barista enthusiasts.",
            tags = listOf("Coffee", "Espresso", "Science", "Food"),
            likesCount = 52000,
            isOfflineSaved = false,
            offlineFileSizeMb = 110.0
        ),
        // Shorts
        VideoItem(
            id = "s1",
            title = "Wait till the end! 3D Motion blur trick in 15 seconds 🤯",
            channelName = "CineSphere Pro",
            channelAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
            channelWepers = "1.8M Wepers",
            thumbnailUrl = "https://images.unsplash.com/photo-1574717024653-61fd2cf4d44d?w=600",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            durationSec = 28,
            viewsCount = 1840000,
            uploadDate = "Today",
            category = "Shorts",
            description = "Quick VFX trick using shutter angle and directional blur.",
            likesCount = 195000,
            isLiked = true,
            isShort = true
        ),
        VideoItem(
            id = "s2",
            title = "Kotlin Coroutine Flow vs RxJava in 30 seconds!",
            channelName = "Android Architect",
            channelAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
            channelWepers = "410K Wepers",
            thumbnailUrl = "https://images.unsplash.com/photo-1607799279861-4dd421887fb3?w=600",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4",
            durationSec = 34,
            viewsCount = 890000,
            uploadDate = "Yesterday",
            category = "Shorts",
            description = "Why Kotlin StateFlow won the reactive architecture battle.",
            likesCount = 74000,
            isShort = true
        ),
        VideoItem(
            id = "s3",
            title = "RTX 5090 vs Real Life City at Midnight! Can you tell?",
            channelName = "PixelSmith Studios",
            channelAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150",
            channelWepers = "2.4M Wepers",
            thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            durationSec = 45,
            viewsCount = 3400000,
            uploadDate = "3 days ago",
            category = "Shorts",
            description = "Ray tracing reflections benchmark comparison.",
            likesCount = 420000,
            isShort = true
        )
    )

    val initialComments = listOf(
        CommentItem(
            id = "c1",
            videoId = "v1",
            authorName = "Jordan Spielberg",
            authorAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100",
            text = "The breakdown of soft key rim ratio around 04:15 is pure gold! Best cinema tutorial on OscarTuber.",
            timestamp = "1 hour ago",
            likes = 342,
            isLikedByMe = true,
            isCreatorPinned = true
        ),
        CommentItem(
            id = "c2",
            videoId = "v1",
            authorName = "Elena Rostova",
            authorAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100",
            text = "Offline download worked like a charm on my flight! Watched the whole lighting workshop with zero lag.",
            timestamp = "2 hours ago",
            likes = 129,
            isLikedByMe = false
        ),
        CommentItem(
            id = "c3",
            videoId = "v1",
            authorName = "Marcus Tech",
            authorAvatar = "https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=100",
            text = "Just clicked WEPE on this channel! Content quality is unmatched.",
            timestamp = "5 hours ago",
            likes = 84,
            isLikedByMe = true
        )
    )

    val sampleNotifications = listOf(
        NotificationItem(
            id = "n1",
            title = "New WEPER Alert! 🎉",
            body = "Sarah Walker and 14 others just clicked WEPE on your creator channel.",
            timeAgo = "10m ago",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100",
            isRead = false,
            type = NotificationType.WEPER
        ),
        NotificationItem(
            id = "n2",
            title = "Offline Download Ready 💾",
            body = "'Oscar Cinema Masterclass' was successfully saved to your offline device storage.",
            timeAgo = "1h ago",
            avatarUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=100",
            isRead = false,
            type = NotificationType.DOWNLOAD_COMPLETE
        ),
        NotificationItem(
            id = "n3",
            title = "PixelSmith Studios Uploaded 🎬",
            body = "Watch 'Building a Photorealistic Open World Game in Unreal Engine 5.5' now.",
            timeAgo = "2h ago",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100",
            isRead = true,
            type = NotificationType.UPLOAD
        ),
        NotificationItem(
            id = "n4",
            title = "New Comment on your video 💬",
            body = "Jordan Spielberg: 'The breakdown around 04:15 is pure gold!'",
            timeAgo = "3h ago",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100",
            isRead = true,
            type = NotificationType.COMMENT
        )
    )
}
