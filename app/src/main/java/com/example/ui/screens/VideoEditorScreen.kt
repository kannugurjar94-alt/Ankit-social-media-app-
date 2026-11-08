package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AnkitVerseRepository
import com.example.model.VideoClip
import com.example.model.VideoProject
import com.example.ui.effects.RainbowTouchContainer
import com.example.ui.glass.iosGlass
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.ObsidianDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VideoEditorScreen(
  repository: AnkitVerseRepository,
  onClose: () -> Unit,
  onExportSuccess: () -> Unit
) {
  val project = repository.videoProject.value

  var isPlaying by remember { mutableStateOf(true) }
  var scrubberTime by remember { mutableFloatStateOf(2.4f) }
  var totalDuration by remember { mutableFloatStateOf(10.5f) }
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Edit, 1: Filters, 2: FX, 3: Audio, 4: Text

  var showExportModal by remember { mutableStateOf(false) }
  var exportCaption by remember { mutableStateOf("Created with AnkitVerse CapCut Studio! 🎬🔥 #NextGen") }
  var isExporting by remember { mutableStateOf(false) }
  var exportProgress by remember { mutableFloatStateOf(0f) }

  // Available Color Grading Presets
  val filterPresets = listOf(
    "Cinematic Teal" to listOf(Color(0xFF003844), Color(0xFFFFB100)),
    "Cyberpunk" to listOf(NeonPink, NeonBlue),
    "Vintage 35mm" to listOf(Color(0xFFD4A373), Color(0xFFCCD5AE)),
    "Emerald Matrix" to listOf(Color(0xFF0D3B2E), NeonGreen),
    "Golden Hour" to listOf(NeonAmber, Color(0xFFFF6B6B)),
    "B&W Noir" to listOf(Color.Black, Color.White),
    "Vaporwave" to listOf(NeonPurple, NeonPink)
  )

  // Visual Effects Presets
  val fxPresets = listOf("Glitch", "Lens Flare", "Camera Shake", "RGB Split", "Film Grain", "Cyber Glow")

  // Transitions
  val transitionPresets = listOf("Whip Pan", "Zoom In", "Dissolve", "Crossfade", "Light Leak")

  // Music Library
  val musicLibrary = listOf(
    "Neon Pulse (Original Mix)",
    "Tokyo Drift Melodic",
    "Lofi Sunset Chill",
    "Hyperdrive Bass Boost",
    "Bollywood Synthwave"
  )

  // Simulated Playback Scrubber Loop
  LaunchedEffect(isPlaying) {
    if (isPlaying) {
      while (true) {
        delay(50)
        scrubberTime += 0.05f
        if (scrubberTime >= totalDuration) scrubberTime = 0f
      }
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianDark)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top Studio Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp)
          .iosGlass(RoundedCornerShape(20.dp))
          .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onClose) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close", tint = Color.White)
          }
          Spacer(modifier = Modifier.width(6.dp))
          Column {
            Text("CapCut Studio", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(project.resolution, color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
          }
        }

        // Export Button with Rainbow Smoke Particle Touch
        RainbowTouchContainer(
          modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Brush.horizontalGradient(listOf(NeonPink, NeonPurple)))
            .padding(horizontal = 16.dp, vertical = 8.dp),
          onClick = { showExportModal = true }
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Publish, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Export 4K", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
      }

      // Video Viewport & Filter Preview (16:9 vertical preview)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(240.dp)
          .padding(horizontal = 14.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(Color.Black)
          .border(1.5.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
      ) {
        // Video Preview Background with active Filter Color Grade tint
        Image(
          painter = painterResource(id = R.drawable.ankit_creator),
          contentDescription = "Viewport Preview",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        // Live Color Grading Overlay Tint
        val currentPreset = filterPresets.find { it.first == project.selectedFilter }?.second ?: listOf(NeonPink, NeonBlue)
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.linearGradient(
                colors = listOf(
                  currentPreset.first().copy(alpha = project.filterIntensity * 0.45f),
                  currentPreset.last().copy(alpha = project.filterIntensity * 0.45f)
                )
              )
            )
        )

        // Visual Effect Overlay (e.g. Glitch scanlines)
        if (project.effectName == "Glitch") {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  listOf(Color.Transparent, Color(0x3300D2FF), Color.Transparent, Color(0x33FF2A85))
                )
              )
          )
        }

        // Dynamic Animated Subtitles
        if (project.dynamicCaptions) {
          Box(
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .padding(bottom = 24.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Color.Black.copy(alpha = 0.7f))
              .padding(horizontal = 14.dp, vertical = 6.dp)
          ) {
            Text(
              text = if (scrubberTime < 3.5f) "✨ AnkitVerse CapCut Studio" else if (scrubberTime < 7f) "⚡ Frame-Accurate Multitrack" else "🚀 Export Directly to Feed!",
              color = NeonAmber,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 14.sp,
              letterSpacing = 0.5.sp
            )
          }
        }

        // Play/Pause Overlay Toggle
        IconButton(
          onClick = { isPlaying = !isPlaying },
          modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.5f))
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = "Play/Pause",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
          )
        }

        // Active Audio Chip
        Row(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.MusicNote, contentDescription = null, tint = NeonPink, modifier = Modifier.size(12.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(project.musicTrack, color = Color.White, fontSize = 10.sp, maxLines = 1)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Frame-Accurate Multitrack Timeline
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp)
          .iosGlass(RoundedCornerShape(18.dp))
          .padding(12.dp)
      ) {
        // Timecode Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = String.format("00:%04.1f / 00:%04.1f", scrubberTime, totalDuration),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Transition: ${project.transitionName}", color = NeonAmber, fontSize = 11.sp)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Visual Timeline Scrubber
        Slider(
          value = scrubberTime,
          onValueChange = {
            scrubberTime = it
            isPlaying = false
          },
          valueRange = 0f..totalDuration,
          colors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = NeonPink,
            inactiveTrackColor = Color.White.copy(alpha = 0.2f)
          ),
          modifier = Modifier.fillMaxWidth()
        )

        // Multitrack Layers
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          // Video Track Clips
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(38.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color.White.copy(alpha = 0.08f))
              .padding(2.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
          ) {
            project.clips.forEachIndexed { idx, clip ->
              Box(
                modifier = Modifier
                  .weight(clip.durationSec)
                  .fillMaxHeight()
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (idx % 2 == 0) Color(0xFF6366F1) else Color(0xFF8B5CF6))
                  .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                  .padding(horizontal = 6.dp),
                contentAlignment = Alignment.CenterStart
              ) {
                Text(
                  text = "${clip.name} (${clip.speed}x)",
                  color = Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1
                )
              }
            }
          }

          // Audio Track
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(26.dp)
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0xFF064E3B))
              .border(1.dp, NeonGreen.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
              .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.GraphicEq, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("AUDIO: ${project.musicTrack}", color = Color.White, fontSize = 10.sp, maxLines = 1)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Studio Toolbar Tab Navigation
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(Color.White.copy(alpha = 0.08f))
          .padding(4.dp)
      ) {
        listOf("Edit", "Filters", "Effects", "Audio", "Captions").forEachIndexed { index, title ->
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(if (selectedTab == index) NeonPink else Color.Transparent)
              .clickable { selectedTab = index }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = title,
              color = if (selectedTab == index) Color.White else Color.White.copy(alpha = 0.6f),
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Tab Content Body
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 14.dp)
      ) {
        when (selectedTab) {
          0 -> {
            // Edit Tools: Split, Trim, Speed, Duplicate
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                // Split Tool
                RainbowTouchContainer(
                  modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .iosGlass(RoundedCornerShape(14.dp))
                    .padding(8.dp),
                  onClick = { repository.splitCurrentClip() }
                ) {
                  Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                  ) {
                    Icon(Icons.Default.CallSplit, contentDescription = null, tint = NeonBlue)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Split Clip", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                  }
                }

                // Duplicate Tool
                RainbowTouchContainer(
                  modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .iosGlass(RoundedCornerShape(14.dp))
                    .padding(8.dp),
                  onClick = { repository.splitCurrentClip() }
                ) {
                  Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                  ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, tint = NeonPink)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Duplicate", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                  }
                }
              }

              // Speed selector
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .iosGlass(RoundedCornerShape(14.dp))
                  .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Speed Curve", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  listOf(0.5f, 1.0f, 1.5f, 2.0f).forEach { spd ->
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (project.clips.firstOrNull()?.speed == spd) NeonAmber else Color.White.copy(alpha = 0.12f))
                        .clickable { repository.updateClipSpeed(spd) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                      Text("${spd}x", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                }
              }
            }
          }

          1 -> {
            // Filters & Color Grading Intensity
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Filter Intensity: ${(project.filterIntensity * 100).toInt()}%", color = Color.White, fontSize = 12.sp)
                Slider(
                  value = project.filterIntensity,
                  onValueChange = { repository.updateFilter(project.selectedFilter, it) },
                  colors = SliderDefaults.colors(thumbColor = NeonPink, activeTrackColor = NeonPink),
                  modifier = Modifier.width(180.dp)
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filterPresets) { (name, colors) ->
                  val isSelected = project.selectedFilter == name
                  Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { repository.updateFilter(name, project.filterIntensity) }
                  ) {
                    Box(
                      modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Brush.linearGradient(colors))
                        .border(2.dp, if (isSelected) Color.White else Color.Transparent, RoundedCornerShape(14.dp)),
                      contentAlignment = Alignment.Center
                    ) {
                      if (isSelected) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                      }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(name, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                  }
                }
              }
            }
          }

          2 -> {
            // Effects & Transitions
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              Text("Visual Effects", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
              LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(fxPresets) { fx ->
                  val isSelected = project.effectName == fx
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(12.dp))
                      .background(if (isSelected) NeonPurple else Color.White.copy(alpha = 0.12f))
                      .clickable { repository.updateEffect(fx) }
                      .padding(horizontal = 14.dp, vertical = 8.dp)
                  ) {
                    Text(fx, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }

              Spacer(modifier = Modifier.height(6.dp))

              Text("Scene Transitions", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
              LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(transitionPresets) { tr ->
                  val isSelected = project.transitionName == tr
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(12.dp))
                      .background(if (isSelected) NeonBlue else Color.White.copy(alpha = 0.12f))
                      .clickable { repository.updateTransition(tr) }
                      .padding(horizontal = 14.dp, vertical = 8.dp)
                  ) {
                    Text(tr, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          }

          3 -> {
            // Audio Controls & Music Library
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              Text("Audio Library", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
              LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(musicLibrary) { track ->
                  val isSelected = project.musicTrack == track
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(12.dp))
                      .background(if (isSelected) NeonGreen else Color.White.copy(alpha = 0.12f))
                      .clickable { repository.updateMusicTrack(track) }
                      .padding(horizontal = 12.dp, vertical = 8.dp)
                  ) {
                    Text(track, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }

              Spacer(modifier = Modifier.height(6.dp))

              // Voiceover & Noise Reduction Row
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .iosGlass(RoundedCornerShape(14.dp))
                  .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Mic, contentDescription = null, tint = NeonAmber)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Voiceover & Studio Mic", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(NeonPink)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                  Text("Record", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }

          4 -> {
            // Dynamic Auto-Captions & Typography
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .iosGlass(RoundedCornerShape(14.dp))
                  .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text("Dynamic Auto-Captions", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                  Text("Animated word-by-word subtitles", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                }
                Switch(
                  checked = project.dynamicCaptions,
                  onCheckedChange = { repository.toggleCaptions() },
                  colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = NeonPink)
                )
              }

              // Sticker Overlay
              Text("Overlay Stickers", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                listOf("🔥 VIRAL", "👑 ANKIT", "🎬 4K", "⚡ 60FPS", "🚀 VIP").forEach { badge ->
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(10.dp))
                      .background(Color.White.copy(alpha = 0.12f))
                      .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                      .padding(horizontal = 10.dp, vertical = 6.dp)
                  ) {
                    Text(badge, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          }
        }
      }
    }

    // Export Modal Dialog
    if (showExportModal) {
      val scope = rememberCoroutineScope()
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = 0.85f))
          .padding(20.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xFF161928))
            .border(1.5.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(26.dp))
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Export CapCut Reel", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            IconButton(onClick = { if (!isExporting) showExportModal = false }) {
              Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Resolution Specs
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("RESOLUTION", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
              Text("4K Ultra HD", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("FRAMERATE", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
              Text("60 FPS", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("WATERMARK", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
              Text("None (Clean)", color = NeonAmber, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = exportCaption,
            onValueChange = { exportCaption = it },
            label = { Text("Feed Caption") },
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedBorderColor = NeonPink,
              unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
            ),
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(20.dp))

          if (isExporting) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("Rendering 4K Multitrack... ${(exportProgress * 100).toInt()}%", color = Color.White, fontSize = 13.sp)
              Spacer(modifier = Modifier.height(8.dp))
              LinearProgressIndicator(
                progress = { exportProgress },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(8.dp)
                  .clip(RoundedCornerShape(4.dp)),
                color = NeonPink,
                trackColor = Color.White.copy(alpha = 0.15f)
              )
            }
          } else {
            // Action Button: Post Directly to Feed
            RainbowTouchContainer(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.horizontalGradient(listOf(NeonPink, NeonPurple)))
                .padding(vertical = 14.dp),
              onClick = {
                isExporting = true
                scope.launch {
                  for (i in 1..20) {
                    delay(50)
                    exportProgress = i / 20f
                  }
                  repository.exportVideoToFeed(exportCaption)
                  delay(200)
                  isExporting = false
                  showExportModal = false
                  onExportSuccess()
                }
              }
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.Publish, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Render & Post to AnkitVerse Feed", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
              }
            }
          }
        }
      }
    }
  }
}
