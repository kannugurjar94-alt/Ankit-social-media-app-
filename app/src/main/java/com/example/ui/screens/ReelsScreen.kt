package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AnkitVerseRepository
import com.example.model.Reel
import com.example.ui.effects.RainbowTouchContainer
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReelsScreen(
  repository: AnkitVerseRepository,
  reels: List<Reel>,
  onOpenVideoStudio: () -> Unit
) {
  val pagerState = rememberPagerState(pageCount = { reels.size })
  var isMuted by remember { mutableStateOf(false) }
  var showRemixDialog by remember { mutableStateOf(false) }
  var selectedReelForComments by remember { mutableStateOf<Reel?>(null) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black)
  ) {
    VerticalPager(
      state = pagerState,
      modifier = Modifier.fillMaxSize()
    ) { page ->
      val reel = reels[page]
      ReelItem(
        reel = reel,
        isMuted = isMuted,
        onToggleMute = { isMuted = !isMuted },
        onToggleLike = { repository.toggleLikeReel(reel.id) },
        onOpenComments = { selectedReelForComments = reel },
        onRemix = { showRemixDialog = true }
      )
    }

    // Top Header: "Reels" label & Camera Shortcut
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 20.dp)
        .align(Alignment.TopCenter),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Reels",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
      )

      RainbowTouchContainer(
        modifier = Modifier
          .clip(CircleShape)
          .background(Color.White.copy(alpha = 0.15f))
          .padding(8.dp),
        onClick = onOpenVideoStudio
      ) {
        Icon(
          Icons.Default.AutoAwesome,
          contentDescription = "Studio",
          tint = Color.White,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    // Remix Modal Dialog
    if (showRemixDialog) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = 0.7f))
          .clickable { showRemixDialog = false },
        contentAlignment = Alignment.Center
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth(0.85f)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1B1E30))
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(Icons.Default.GraphicEq, contentDescription = null, tint = NeonPink, modifier = Modifier.size(44.dp))
          Spacer(modifier = Modifier.height(12.dp))
          Text("Remix with CapCut Studio", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            "Use this audio & video track to create a duet or sequence inside AnkitVerse Studio.",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 13.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Spacer(modifier = Modifier.height(20.dp))
          RainbowTouchContainer(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Brush.horizontalGradient(listOf(NeonPink, NeonPurple)))
              .padding(vertical = 12.dp),
            onClick = {
              showRemixDialog = false
              onOpenVideoStudio()
            }
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.Center
            ) {
              Text("Open In Studio", color = Color.White, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Comments Sheet
    if (selectedReelForComments != null) {
      ModalBottomSheet(
        onDismissRequest = { selectedReelForComments = null },
        sheetState = sheetState,
        containerColor = Color(0xFF141724),
        contentColor = Color.White
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Text("Comments (${selectedReelForComments!!.commentsCount})", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
          Spacer(modifier = Modifier.height(14.dp))
          Text("Top Comment by @ankit: Keep creating fire content! 🔥", color = Color.White.copy(alpha = 0.9f))
          Spacer(modifier = Modifier.height(8.dp))
          Text("@elena_cinematic: The pacing is elite 👏", color = Color.White.copy(alpha = 0.8f))
          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}

@Composable
fun ReelItem(
  reel: Reel,
  isMuted: Boolean,
  onToggleMute: () -> Unit,
  onToggleLike: () -> Unit,
  onOpenComments: () -> Unit,
  onRemix: () -> Unit
) {
  var videoProgress by remember { mutableFloatStateOf(0f) }
  var showHeartBurst by remember { mutableStateOf(false) }
  val heartScale = remember { Animatable(0f) }
  val discRotation = remember { Animatable(0f) }
  val scope = rememberCoroutineScope()

  // Vinyl record rotation loop
  LaunchedEffect(Unit) {
    discRotation.animateTo(
      targetValue = 360f,
      animationSpec = infiniteRepeatable(
        animation = tween(4000, easing = LinearEasing),
        repeatMode = RepeatMode.Restart
      )
    )
  }

  // Simulated auto-play loop
  LaunchedEffect(reel.id) {
    videoProgress = 0f
    while (true) {
      delay(50)
      videoProgress += 0.007f
      if (videoProgress >= 1f) videoProgress = 0f
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .pointerInput(Unit) {
        detectTapGestures(
          onDoubleTap = {
            onToggleLike()
            showHeartBurst = true
            scope.launch {
              heartScale.snapTo(0.2f)
              heartScale.animateTo(1.5f, tween(250, easing = FastOutSlowInEasing))
              heartScale.animateTo(1.0f, tween(150))
              delay(350)
              showHeartBurst = false
            }
          },
          onTap = { onToggleMute() }
        )
      }
  ) {
    // Media / Background
    if (reel.mediaRes != null) {
      Image(
        painter = painterResource(id = reel.mediaRes),
        contentDescription = reel.caption,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    } else {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              listOf(Color(0xFF2C0E4B), Color(0xFF130924), Color(0xFF000000))
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.MusicNote, contentDescription = null, tint = NeonPink, modifier = Modifier.size(54.dp))
          Spacer(modifier = Modifier.height(10.dp))
          Text(reel.audioTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      }
    }

    // Double Tap Heart Burst
    if (showHeartBurst) {
      Icon(
        Icons.Default.Favorite,
        contentDescription = null,
        tint = NeonPink,
        modifier = Modifier
          .size(110.dp)
          .align(Alignment.Center)
          .scale(heartScale.value)
      )
    }

    // Audio Mute/Unmute Indicator badge
    Box(
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(top = 70.dp, end = 16.dp)
        .clip(CircleShape)
        .background(Color.Black.copy(alpha = 0.5f))
        .padding(8.dp)
    ) {
      Icon(
        imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier.size(18.dp)
      )
    }

    // Right Action Sidebar (Like, Comment, Share, Remix, Vinyl Disc)
    Column(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 14.dp, bottom = 100.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Like
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RainbowTouchContainer(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.45f)),
          onClick = onToggleLike
        ) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
              imageVector = if (reel.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
              contentDescription = "Like",
              tint = if (reel.isLiked) NeonPink else Color.White,
              modifier = Modifier.size(26.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text("${reel.likesCount}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }

      // Comment
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RainbowTouchContainer(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.45f)),
          onClick = onOpenComments
        ) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Comments", tint = Color.White, modifier = Modifier.size(24.dp))
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text("${reel.commentsCount}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }

      // Share
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RainbowTouchContainer(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.45f)),
          onClick = {}
        ) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.NearMe, contentDescription = "Share", tint = Color.White, modifier = Modifier.size(24.dp))
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text("${reel.sharesCount}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }

      // Remix
      RainbowTouchContainer(
        modifier = Modifier
          .size(46.dp)
          .clip(CircleShape)
          .background(Color.Black.copy(alpha = 0.45f)),
        onClick = onRemix
      ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          Icon(Icons.Default.GraphicEq, contentDescription = "Remix", tint = NeonAmber, modifier = Modifier.size(24.dp))
        }
      }

      // Rotating Vinyl Disc
      Box(
        modifier = Modifier
          .size(44.dp)
          .rotate(discRotation.value)
          .clip(CircleShape)
          .background(Color(0xFF1E1E28))
          .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Box(
          modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(NeonPink)
        )
      }
    }

    // Bottom Left Information Overlay
    Column(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(start = 16.dp, end = 80.dp, bottom = 100.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        if (reel.author.avatarRes != null) {
          Image(
            painter = painterResource(id = reel.author.avatarRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .border(1.5.dp, Color.White, CircleShape)
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(reel.author.username, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
          Text("Follow", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = reel.caption,
        color = Color.White,
        fontSize = 13.sp,
        maxLines = 2,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(Color.Black.copy(alpha = 0.4f))
          .padding(horizontal = 10.dp, vertical = 4.dp)
      ) {
        Icon(Icons.Default.MusicNote, contentDescription = null, tint = NeonPink, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("${reel.audioTitle} • ${reel.audioArtist}", color = Color.White, fontSize = 11.sp)
      }
    }

    // Bottom Progress Bar
    LinearProgressIndicator(
      progress = { videoProgress },
      modifier = Modifier
        .fillMaxWidth()
        .height(2.dp)
        .align(Alignment.BottomCenter)
        .padding(bottom = 78.dp),
      color = NeonPink,
      trackColor = Color.Transparent
    )
  }
}
