package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.VideoEditConfig
import com.example.data.model.VideoFilter
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceHighlight
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.OscarEmerald
import com.example.ui.theme.OscarGold
import com.example.ui.theme.OscarRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoEditorScreen(
    initialTitle: String = "My Oscar Cinematic Masterpiece",
    config: VideoEditConfig,
    onUpdateFilter: (VideoFilter) -> Unit,
    onUpdateTrim: (Float, Float) -> Unit,
    onUpdateSpeed: (Float) -> Unit,
    onUpdateVolume: (Float) -> Unit,
    onUpdateWatermark: (String) -> Unit,
    onExportAndPublish: (title: String, category: String, description: String, tags: List<String>, thumbnail: String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(initialTitle) }
    var category by remember { mutableStateOf("Cinema") }
    var tagsInput by remember { mutableStateOf("Filmmaking, 4K, OscarTuber, Masterclass") }
    var description by remember { mutableStateOf("Edited and graded exclusively using OscarTuber Studio video tools.") }
    var isPreviewPlaying by remember { mutableStateOf(true) }
    var watermarkText by remember { mutableStateOf(config.watermarkText) }

    var trimRange by remember { mutableStateOf(0f..180f) }

    // Color filter matrices for preview rendering
    val colorFilter: ColorFilter? = when (config.filterType) {
        VideoFilter.NORMAL -> null
        VideoFilter.CINEMA_WARM -> ColorFilter.colorMatrix(
            ColorMatrix(
                floatArrayOf(
                    1.2f, 0f, 0f, 0f, 20f,
                    0f, 1.05f, 0f, 0f, 10f,
                    0f, 0f, 0.85f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        )
        VideoFilter.OSCAR_GOLD -> ColorFilter.colorMatrix(
            ColorMatrix(
                floatArrayOf(
                    1.3f, 0f, 0f, 0f, 35f,
                    0f, 1.2f, 0f, 0f, 25f,
                    0f, 0f, 0.6f, 0f, -10f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        )
        VideoFilter.NOIR_MONO -> ColorFilter.colorMatrix(
            ColorMatrix().apply { setToSaturation(0f) }
        )
        VideoFilter.CYBER_NEON -> ColorFilter.colorMatrix(
            ColorMatrix(
                floatArrayOf(
                    1.4f, 0f, 0f, 0f, 10f,
                    0f, 0.9f, 0f, 0f, 0f,
                    0f, 0f, 1.5f, 0f, 30f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        )
        VideoFilter.VINTAGE_SEPIA -> ColorFilter.colorMatrix(
            ColorMatrix(
                floatArrayOf(
                    0.393f, 0.769f, 0.189f, 0f, 0f,
                    0.349f, 0.686f, 0.168f, 0f, 0f,
                    0.272f, 0.534f, 0.131f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkSurface)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
            .testTag("video_editor_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Oscar Video Studio",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Realtime Video Editor & Color Lab",
                        fontSize = 11.sp,
                        color = OscarGold
                    )
                }
            }

            Button(
                onClick = {
                    val tagList = tagsInput.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    onExportAndPublish(
                        title,
                        category,
                        description,
                        tagList,
                        "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=800"
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = OscarRed),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.testTag("export_publish_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Publish", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        // Live Video Canvas Preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(Color.Black)
                .clickable { isPreviewPlaying = !isPreviewPlaying }
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=800",
                contentDescription = "Editor Preview",
                contentScale = ContentScale.Crop,
                colorFilter = colorFilter,
                modifier = Modifier.fillMaxSize()
            )

            // Live Watermark Overlay
            if (watermarkText.isNotBlank()) {
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "⚡ $watermarkText",
                        color = OscarGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Filter Badge top left
            Surface(
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            ) {
                Text(
                    text = "LUT: ${config.filterType.displayName} • ${config.playbackSpeed}x",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        // Trimming Tool
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ContentCut,
                            contentDescription = null,
                            tint = OscarRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Duration Trimming",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                    val trimmedSeconds = (trimRange.endInclusive - trimRange.start).toInt()
                    Text(
                        text = "Trimmed: %d:%02d".format(trimmedSeconds / 60, trimmedSeconds % 60),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OscarGold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                RangeSlider(
                    value = trimRange,
                    onValueChange = {
                        trimRange = it
                        onUpdateTrim(it.start, it.endInclusive)
                    },
                    valueRange = 0f..180f,
                    colors = SliderDefaults.colors(
                        thumbColor = OscarRed,
                        activeTrackColor = OscarRed,
                        inactiveTrackColor = DarkSurfaceHighlight
                    ),
                    modifier = Modifier.testTag("trim_range_slider")
                )
            }
        }

        // Cinematic Color Grading Filters
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ColorLens,
                        contentDescription = null,
                        tint = OscarGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Cinema Color Grading & LUTs",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VideoFilter.values().forEach { filter ->
                        val isSelected = config.filterType == filter
                        Surface(
                            color = if (isSelected) OscarGold.copy(alpha = 0.25f) else DarkSurfaceHighlight,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) OscarGold else DarkBorder
                            ),
                            modifier = Modifier
                                .clickable { onUpdateFilter(filter) }
                                .testTag("filter_opt_${filter.name}")
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = filter.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) OscarGold else Color.White
                                )
                                Text(
                                    text = filter.description,
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Audio & Playback Speed Controls
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Audio Volume
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (config.audioVolume == 0f) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = OscarEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Audio Track Volume",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "${(config.audioVolume * 100).toInt()}%",
                        fontSize = 12.sp,
                        color = OscarEmerald,
                        fontWeight = FontWeight.Bold
                    )
                }

                Slider(
                    value = config.audioVolume,
                    onValueChange = { onUpdateVolume(it) },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = OscarEmerald,
                        activeTrackColor = OscarEmerald
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Playback Speed
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = OscarRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Clip Speed: ${config.playbackSpeed}x",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0.5f, 1.0f, 1.5f, 2.0f).forEach { spd ->
                        val isSel = config.playbackSpeed == spd
                        Surface(
                            color = if (isSel) OscarRed else DarkSurfaceHighlight,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .clickable { onUpdateSpeed(spd) }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${spd}x",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Metadata & Publishing Inputs
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Publishing Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Video Title") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OscarRed,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = watermarkText,
                    onValueChange = {
                        watermarkText = it
                        onUpdateWatermark(it)
                    },
                    label = { Text("Watermark / Overlay Text") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OscarGold,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = tagsInput,
                    onValueChange = { tagsInput = it },
                    label = { Text("Tags (comma separated)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OscarRed,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
