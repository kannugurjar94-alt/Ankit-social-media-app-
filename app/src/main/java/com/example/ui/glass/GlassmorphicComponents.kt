package com.example.ui.glass

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.effects.RainbowTouchContainer
import com.example.ui.theme.GlassBorderLight
import com.example.ui.theme.GlassSurfaceDark
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple

/**
 * Reusable iOS Frosted Glass container modifier.
 */
fun Modifier.iosGlass(
  shape: RoundedCornerShape = RoundedCornerShape(22.dp),
  backgroundColor: Color = Color(0x381E2338),
  borderColor: Color = GlassBorderLight,
  borderWidth: Dp = 1.dp
): Modifier = this
  .clip(shape)
  .background(backgroundColor)
  .border(borderWidth, borderColor, shape)

/**
 * An iOS Glass Button integrated with the Satarangi Rainbow touch particle effect.
 */
@Composable
fun IOSGlassButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  gradient: Brush = Brush.horizontalGradient(listOf(NeonPink, NeonPurple)),
  contentColor: Color = Color.White
) {
  RainbowTouchContainer(
    modifier = modifier
      .clip(RoundedCornerShape(18.dp))
      .background(gradient)
      .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(18.dp)),
    onClick = onClick
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 14.dp, horizontal = 20.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (icon != null) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = contentColor,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
      }
      Text(
        text = text,
        color = contentColor,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.3.sp
      )
    }
  }
}

/**
 * An iOS Control Center sheet displaying frosted tiles, sliders, and toggles.
 */
@Composable
fun IOSControlCenterDialog(
  isOpen: Boolean,
  onDismiss: () -> Unit,
  onOpenVideoStudio: () -> Unit,
  onOpenMonetization: () -> Unit
) {
  var wifiEnabled by remember { mutableStateOf(true) }
  var bluetoothEnabled by remember { mutableStateOf(true) }
  var airplaneMode by remember { mutableStateOf(false) }
  var darkMode by remember { mutableStateOf(true) }
  var dndEnabled by remember { mutableStateOf(false) }
  var flashlight by remember { mutableStateOf(false) }
  var brightness by remember { mutableFloatStateOf(0.85f) }
  var volume by remember { mutableFloatStateOf(0.70f) }

  AnimatedVisibility(
    visible = isOpen,
    enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
    exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 })
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.75f))
        .clickable { onDismiss() }
        .padding(16.dp),
      contentAlignment = Alignment.TopCenter
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(32.dp))
          .background(Color(0xEE161928))
          .border(1.5.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(32.dp))
          .padding(20.dp)
          .clickable(enabled = false) { /* prevent dismiss */ }
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(NeonGreen)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "AnkitVerse Control Center",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White.copy(alpha = 0.8f))
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // First Row: Network Cluster + Media Player
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Connectivity 2x2 Glass Tile
          Box(
            modifier = Modifier
              .weight(1f)
              .height(150.dp)
              .iosGlass(RoundedCornerShape(24.dp))
              .padding(12.dp)
          ) {
            Column(
              modifier = Modifier.fillMaxSize(),
              verticalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
              ) {
                ControlCenterIconTile(
                  icon = Icons.Default.Wifi,
                  active = wifiEnabled,
                  activeColor = NeonBlue,
                  onClick = { wifiEnabled = !wifiEnabled }
                )
                ControlCenterIconTile(
                  icon = Icons.Default.Bluetooth,
                  active = bluetoothEnabled,
                  activeColor = NeonPurple,
                  onClick = { bluetoothEnabled = !bluetoothEnabled }
                )
              }
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
              ) {
                ControlCenterIconTile(
                  icon = Icons.Default.AirplanemodeActive,
                  active = airplaneMode,
                  activeColor = NeonAmber,
                  onClick = { airplaneMode = !airplaneMode }
                )
                ControlCenterIconTile(
                  icon = Icons.Default.DarkMode,
                  active = darkMode,
                  activeColor = NeonPink,
                  onClick = { darkMode = !darkMode }
                )
              }
            }
          }

          // Now Playing / Media Tile
          Box(
            modifier = Modifier
              .weight(1f)
              .height(150.dp)
              .iosGlass(RoundedCornerShape(24.dp))
              .padding(14.dp)
          ) {
            Column(
              modifier = Modifier.fillMaxSize(),
              verticalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "NOW PLAYING",
                  color = Color.White.copy(alpha = 0.5f),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp
                )
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = NeonPink, modifier = Modifier.size(16.dp))
              }

              Column {
                Text("Ankit - Neon Pulse", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text("Trending Audio #1", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
              }

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Second Row: Brightness & Volume Sliders
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Brightness Slider
          Box(
            modifier = Modifier
              .weight(1f)
              .height(140.dp)
              .iosGlass(RoundedCornerShape(24.dp))
              .padding(14.dp)
          ) {
            Column(
              modifier = Modifier.fillMaxSize(),
              verticalArrangement = Arrangement.SpaceBetween,
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(Icons.Default.BrightnessHigh, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
              Slider(
                value = brightness,
                onValueChange = { brightness = it },
                colors = SliderDefaults.colors(
                  thumbColor = Color.White,
                  activeTrackColor = Color.White.copy(alpha = 0.9f),
                  inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                ),
                modifier = Modifier.fillMaxWidth()
              )
              Text("${(brightness * 100).toInt()}%", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
            }
          }

          // Volume Slider
          Box(
            modifier = Modifier
              .weight(1f)
              .height(140.dp)
              .iosGlass(RoundedCornerShape(24.dp))
              .padding(14.dp)
          ) {
            Column(
              modifier = Modifier.fillMaxSize(),
              verticalArrangement = Arrangement.SpaceBetween,
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
              Slider(
                value = volume,
                onValueChange = { volume = it },
                colors = SliderDefaults.colors(
                  thumbColor = NeonBlue,
                  activeTrackColor = NeonBlue,
                  inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                ),
                modifier = Modifier.fillMaxWidth()
              )
              Text("${(volume * 100).toInt()}%", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Third Row: Quick Creator & Video Studio Launchers
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Video Studio Button
          RainbowTouchContainer(
            modifier = Modifier
              .weight(1f)
              .height(72.dp)
              .iosGlass(
                shape = RoundedCornerShape(20.dp),
                backgroundColor = Color(0x356366F1),
                borderColor = Color.White.copy(alpha = 0.3f)
              )
              .padding(12.dp),
            onClick = {
              onDismiss()
              onOpenVideoStudio()
            }
          ) {
            Row(
              modifier = Modifier.fillMaxSize(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(Brush.linearGradient(listOf(NeonPink, NeonPurple))),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Videocam, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text("CapCut Studio", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Edit & Export", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
              }
            }
          }

          // Monetization Button
          RainbowTouchContainer(
            modifier = Modifier
              .weight(1f)
              .height(72.dp)
              .iosGlass(
                shape = RoundedCornerShape(20.dp),
                backgroundColor = Color(0x3510B981),
                borderColor = Color.White.copy(alpha = 0.3f)
              )
              .padding(12.dp),
            onClick = {
              onDismiss()
              onOpenMonetization()
            }
          ) {
            Row(
              modifier = Modifier.fillMaxSize(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(Brush.linearGradient(listOf(NeonAmber, NeonGreen))),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text("Monetization", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("500k Unlocked", color = NeonGreen, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun ControlCenterIconTile(
  icon: ImageVector,
  active: Boolean,
  activeColor: Color,
  onClick: () -> Unit
) {
  RainbowTouchContainer(
    modifier = Modifier
      .size(48.dp)
      .clip(CircleShape)
      .background(if (active) activeColor else Color.White.copy(alpha = 0.12f)),
    onClick = onClick
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (active) Color.White else Color.White.copy(alpha = 0.6f),
        modifier = Modifier.size(22.dp)
      )
    }
  }
}
