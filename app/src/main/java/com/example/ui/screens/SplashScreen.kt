package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.ObsidianDark
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  onSplashFinished: () -> Unit
) {
  val scaleAnim = remember { Animatable(0.75f) }
  val alphaAnim = remember { Animatable(0f) }
  val pulseAnim = remember { Animatable(1f) }

  LaunchedEffect(Unit) {
    alphaAnim.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))
    scaleAnim.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))
  }

  LaunchedEffect(Unit) {
    pulseAnim.animateTo(
      targetValue = 1.08f,
      animationSpec = infiniteRepeatable(
        animation = tween(1200, easing = FastOutSlowInEasing),
        repeatMode = RepeatMode.Reverse
      )
    )
  }

  LaunchedEffect(Unit) {
    // 2.5 second splash display followed by smooth transition
    delay(2500)
    onSplashFinished()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            ObsidianDark,
            Color(0xFF0F0E1A),
            Color(0xFF150D24),
            ObsidianDark
          )
        )
      ),
    contentAlignment = Alignment.Center
  ) {
    Column(
      modifier = Modifier
        .scale(scaleAnim.value)
        .alpha(alphaAnim.value)
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // Developer / Creator Photo Container
      Box(
        modifier = Modifier
          .size(140.dp)
          .scale(pulseAnim.value),
        contentAlignment = Alignment.Center
      ) {
        // Glowing Neon Gradient Halo Ring
        Box(
          modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(
              Brush.sweepGradient(
                listOf(NeonPink, NeonPurple, NeonBlue, NeonAmber, NeonPink)
              )
            )
        )

        // Creator Photo
        Image(
          painter = painterResource(id = R.drawable.ankit_creator),
          contentDescription = "Created by Ankit",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(126.dp)
            .clip(CircleShape)
            .border(3.dp, Color.White, CircleShape)
        )

        // Crown Badge on Photo
        Box(
          modifier = Modifier
            .size(36.dp)
            .align(Alignment.TopEnd)
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(NeonAmber, Color(0xFFFF8800))))
            .border(1.5.dp, Color.White, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Verified Creator",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Creator Identification Tag
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .background(Color.White.copy(alpha = 0.12f))
          .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
          .padding(horizontal = 16.dp, vertical = 6.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(NeonAmber)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Created by Ankit",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // App Brand Title
      Text(
        text = "AnkitVerse",
        color = Color.White,
        fontSize = 38.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.sp
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "The Next-Gen Social & Creative Studio",
        color = Color.White.copy(alpha = 0.7f),
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium
      )

      Spacer(modifier = Modifier.height(48.dp))

      // Sleek loading progress
      CircularProgressIndicator(
        modifier = Modifier.size(28.dp),
        color = NeonPink,
        strokeWidth = 2.5.dp
      )
    }
  }
}
