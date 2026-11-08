package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.model.Comment
import com.example.model.Post
import com.example.model.Story
import com.example.model.User
import com.example.ui.effects.RainbowTouchContainer
import com.example.ui.glass.iosGlass
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.ObsidianDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  repository: AnkitVerseRepository,
  posts: List<Post>,
  stories: List<Story>,
  currentUser: User,
  onOpenDMs: () -> Unit,
  onOpenControlCenter: () -> Unit,
  onOpenVideoStudio: () -> Unit,
  onNavigateToMonetization: () -> Unit
) {
  var activeStoryIndex by remember { mutableStateOf<Int?>(null) }
  var commentSheetPost by remember { mutableStateOf<Post?>(null) }
  var shareSheetPost by remember { mutableStateOf<Post?>(null) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianDark)
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 100.dp)
    ) {
      // Top Glass Navigation Bar
      item {
        TopHomeGlassBar(
          onOpenDMs = onOpenDMs,
          onOpenControlCenter = onOpenControlCenter,
          onOpenVideoStudio = onOpenVideoStudio,
          onNavigateToMonetization = onNavigateToMonetization
        )
      }

      // Horizontal Stories Tray
      item {
        StoriesTray(
          stories = stories,
          currentUser = currentUser,
          onStoryClick = { index ->
            activeStoryIndex = index
            repository.markStoryViewed(stories[index].id)
          },
          onAddStory = onOpenVideoStudio
        )
        Spacer(modifier = Modifier.height(8.dp))
      }

      // Vertical Posts Feed
      items(posts, key = { it.id }) { post ->
        PostCard(
          post = post,
          onToggleLike = { repository.toggleLikePost(post.id) },
          onToggleSave = { repository.toggleSavePost(post.id) },
          onCommentClick = { commentSheetPost = post },
          onShareClick = { shareSheetPost = post }
        )
        Spacer(modifier = Modifier.height(16.dp))
      }
    }

    // Full-screen Story Viewer Overlay
    activeStoryIndex?.let { index ->
      StoryViewerDialog(
        story = stories[index],
        onDismiss = { activeStoryIndex = null },
        onNext = {
          if (index < stories.size - 1) {
            activeStoryIndex = index + 1
            repository.markStoryViewed(stories[index + 1].id)
          } else {
            activeStoryIndex = null
          }
        },
        onPrev = {
          if (index > 0) activeStoryIndex = index - 1 else activeStoryIndex = null
        }
      )
    }

    // Comments Modal Bottom Sheet
    if (commentSheetPost != null) {
      ModalBottomSheet(
        onDismissRequest = { commentSheetPost = null },
        sheetState = sheetState,
        containerColor = Color(0xFF141724),
        contentColor = Color.White
      ) {
        CommentsSheetContent(
          post = commentSheetPost!!,
          repository = repository,
          onClose = { commentSheetPost = null }
        )
      }
    }

    // Share Modal Bottom Sheet
    if (shareSheetPost != null) {
      ModalBottomSheet(
        onDismissRequest = { shareSheetPost = null },
        sheetState = sheetState,
        containerColor = Color(0xFF141724),
        contentColor = Color.White
      ) {
        ShareSheetContent(
          post = shareSheetPost!!,
          onClose = { shareSheetPost = null }
        )
      }
    }
  }
}

@Composable
fun TopHomeGlassBar(
  onOpenDMs: () -> Unit,
  onOpenControlCenter: () -> Unit,
  onOpenVideoStudio: () -> Unit,
  onNavigateToMonetization: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp)
      .iosGlass(RoundedCornerShape(22.dp))
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Brand Logo & Crown
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.clickable { onNavigateToMonetization() }
    ) {
      Image(
        painter = painterResource(id = R.drawable.app_icon_asset),
        contentDescription = "Logo",
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "AnkitVerse",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        letterSpacing = 0.5.sp
      )
      Spacer(modifier = Modifier.width(6.dp))
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(Brush.horizontalGradient(listOf(NeonAmber, NeonPink)))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text("500k VIP", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
      }
    }

    // Actions: Create (+), iOS Control Center, DMs
    Row(
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Create / Video Studio
      IconButton(onClick = onOpenVideoStudio) {
        Icon(Icons.Default.Add, contentDescription = "Create", tint = Color.White, modifier = Modifier.size(24.dp))
      }

      // iOS Control Center Toggle
      IconButton(onClick = onOpenControlCenter) {
        Icon(Icons.Default.Tune, contentDescription = "Control Center", tint = NeonBlue, modifier = Modifier.size(22.dp))
      }

      // DMs Inbox with unread count
      IconButton(onClick = onOpenDMs) {
        BadgedBox(
          badge = {
            Badge(
              containerColor = NeonPink,
              contentColor = Color.White
            ) {
              Text("1")
            }
          }
        ) {
          Icon(Icons.Default.Send, contentDescription = "Messages", tint = Color.White, modifier = Modifier.size(22.dp))
        }
      }
    }
  }
}

@Composable
fun StoriesTray(
  stories: List<Story>,
  currentUser: User,
  onStoryClick: (Int) -> Unit,
  onAddStory: () -> Unit
) {
  LazyRow(
    modifier = Modifier.fillMaxWidth(),
    contentPadding = PaddingValues(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Current User Add Story
    item {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onAddStory() }
      ) {
        Box(
          modifier = Modifier.size(68.dp),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.drawable.ankit_creator),
            contentDescription = "Your Story",
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .size(60.dp)
              .clip(CircleShape)
              .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape)
          )
          Box(
            modifier = Modifier
              .size(22.dp)
              .align(Alignment.BottomEnd)
              .clip(CircleShape)
              .background(NeonPink)
              .border(1.5.dp, Color.Black, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text("Your Story", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
      }
    }

    // Friends' Stories
    items(stories.indices.toList()) { index ->
      val story = stories[index]
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onStoryClick(index) }
      ) {
        Box(
          modifier = Modifier
            .size(68.dp)
            .clip(CircleShape)
            .background(
              if (!story.isViewed) {
                Brush.sweepGradient(listOf(NeonPink, NeonPurple, NeonAmber, NeonPink))
              } else {
                Brush.linearGradient(listOf(Color.White.copy(alpha = 0.2f), Color.White.copy(alpha = 0.2f)))
              }
            )
            .padding(2.5.dp),
          contentAlignment = Alignment.Center
        ) {
          if (story.author.avatarRes != null) {
            Image(
              painter = painterResource(id = story.author.avatarRes),
              contentDescription = story.author.name,
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(2.dp, Color.Black, CircleShape)
            )
          } else {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(NeonBlue, NeonPurple))),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = story.author.name.take(1),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              )
            }
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = story.author.username,
          color = Color.White.copy(alpha = 0.85f),
          fontSize = 11.sp,
          maxLines = 1
        )
      }
    }
  }
}

@Composable
fun PostCard(
  post: Post,
  onToggleLike: () -> Unit,
  onToggleSave: () -> Unit,
  onCommentClick: () -> Unit,
  onShareClick: () -> Unit
) {
  var showDoubleTapHeart by remember { mutableStateOf(false) }
  val heartScale = remember { Animatable(0f) }
  val scope = rememberCoroutineScope()

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp)
      .iosGlass(RoundedCornerShape(26.dp))
      .padding(bottom = 14.dp)
  ) {
    // Post Author Row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        if (post.author.avatarRes != null) {
          Image(
            painter = painterResource(id = post.author.avatarRes),
            contentDescription = post.author.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .border(1.5.dp, NeonPink, CircleShape)
          )
        } else {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(Brush.linearGradient(listOf(NeonBlue, NeonPurple))),
            contentAlignment = Alignment.Center
          ) {
            Text(post.author.name.take(1), color = Color.White, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = post.author.username,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
            if (post.author.isVerified) {
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                Icons.Default.Star,
                contentDescription = "Verified",
                tint = NeonAmber,
                modifier = Modifier.size(14.dp)
              )
            }
          }
          Text(post.timestamp, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        }
      }

      IconButton(onClick = {}) {
        Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = Color.White.copy(alpha = 0.7f))
      }
    }

    // Post Media Container with Double-Tap to Like
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(340.dp)
        .clip(RoundedCornerShape(18.dp))
        .background(Color.Black)
        .pointerInput(Unit) {
          detectTapGestures(
            onDoubleTap = {
              onToggleLike()
              showDoubleTapHeart = true
              scope.launch {
                heartScale.snapTo(0.2f)
                heartScale.animateTo(1.4f, tween(300, easing = FastOutSlowInEasing))
                heartScale.animateTo(1.0f, tween(150))
                delay(400)
                showDoubleTapHeart = false
              }
            }
          )
        },
      contentAlignment = Alignment.Center
    ) {
      if (post.imageRes != null) {
        Image(
          painter = painterResource(id = post.imageRes),
          contentDescription = "Post Media",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      } else {
        // High-fidelity aesthetic visual for non-asset posts
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.radialGradient(
                colors = listOf(
                  Color(0xFF3B185F),
                  Color(0xFF2A0845),
                  Color(0xFF0F0C20)
                )
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.MusicNote, contentDescription = null, tint = NeonPink, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = post.musicTrack ?: "AnkitVerse Studio Session",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
            Text(
              text = "Rendered in CapCut Suite 🎬",
              color = Color.White.copy(alpha = 0.6f),
              fontSize = 12.sp
            )
          }
        }
      }

      // Animated Double-Tap Heart
      if (showDoubleTapHeart) {
        Icon(
          Icons.Default.Favorite,
          contentDescription = null,
          tint = NeonPink,
          modifier = Modifier
            .size(90.dp)
            .scale(heartScale.value)
        )
      }
    }

    // Interactive Action Row (Like, Comment, Share, Bookmark)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        // Like Button with Rainbow Smoke Particle Touch
        RainbowTouchContainer(
          modifier = Modifier
            .clip(CircleShape)
            .padding(6.dp),
          onClick = onToggleLike
        ) {
          Icon(
            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Like",
            tint = if (post.isLiked) NeonPink else Color.White,
            modifier = Modifier.size(26.dp)
          )
        }

        // Comment Button
        RainbowTouchContainer(
          modifier = Modifier
            .clip(CircleShape)
            .padding(6.dp),
          onClick = onCommentClick
        ) {
          Icon(
            imageVector = Icons.Default.ChatBubbleOutline,
            contentDescription = "Comment",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
        }

        // Share Button
        RainbowTouchContainer(
          modifier = Modifier
            .clip(CircleShape)
            .padding(6.dp),
          onClick = onShareClick
        ) {
          Icon(
            imageVector = Icons.Default.NearMe,
            contentDescription = "Share",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
        }
      }

      // Bookmark Button
      RainbowTouchContainer(
        modifier = Modifier
          .clip(CircleShape)
          .padding(6.dp),
        onClick = onToggleSave
      ) {
        Icon(
          imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
          contentDescription = "Save",
          tint = if (post.isSaved) NeonAmber else Color.White,
          modifier = Modifier.size(26.dp)
        )
      }
    }

    // Likes count & Caption
    Column(modifier = Modifier.padding(horizontal = 14.dp)) {
      Text(
        text = "${post.likesCount} likes",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
      )

      Spacer(modifier = Modifier.height(4.dp))

      Row(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "${post.author.username} ",
          color = Color.White,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
        Text(
          text = post.caption,
          color = Color.White.copy(alpha = 0.9f),
          fontSize = 13.sp,
          lineHeight = 18.sp
        )
      }

      if (post.musicTrack != null) {
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Icon(Icons.Default.MusicNote, contentDescription = null, tint = NeonPink, modifier = Modifier.size(13.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(post.musicTrack, color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "View all ${post.commentsCount} comments",
        color = Color.White.copy(alpha = 0.5f),
        fontSize = 12.sp,
        modifier = Modifier.clickable { onCommentClick() }
      )
    }
  }
}

@Composable
fun StoryViewerDialog(
  story: Story,
  onDismiss: () -> Unit,
  onNext: () -> Unit,
  onPrev: () -> Unit
) {
  var progress by remember { mutableStateOf(0f) }

  LaunchedEffect(story.id) {
    progress = 0f
    val duration = 4000L
    val step = 40L
    for (i in 0 until (duration / step)) {
      delay(step)
      progress += (step.toFloat() / duration)
    }
    onNext()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black)
      .pointerInput(Unit) {
        detectTapGestures(
          onTap = { offset ->
            if (offset.x < size.width * 0.35f) onPrev() else onNext()
          }
        )
      }
  ) {
    // Story Media
    if (story.mediaRes != null) {
      Image(
        painter = painterResource(id = story.mediaRes),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    } else {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Brush.verticalGradient(listOf(NeonPurple, NeonPink, ObsidianDark)))
      )
    }

    // Top Header & Progress Bar
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .align(Alignment.TopCenter)
    ) {
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(3.dp)
          .clip(RoundedCornerShape(2.dp)),
        color = Color.White,
        trackColor = Color.White.copy(alpha = 0.3f)
      )

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (story.author.avatarRes != null) {
            Image(
              painter = painterResource(id = story.author.avatarRes),
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(story.author.username, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(story.timestamp, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }

        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }
      }
    }

    // Story Text Overlay
    if (story.textOverlay.isNotBlank()) {
      Box(
        modifier = Modifier
          .align(Alignment.Center)
          .padding(32.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(Color.Black.copy(alpha = 0.65f))
          .padding(16.dp)
      ) {
        Text(
          text = story.textOverlay,
          color = Color.White,
          fontWeight = FontWeight.SemiBold,
          fontSize = 17.sp
        )
      }
    }
  }
}

@Composable
fun CommentsSheetContent(
  post: Post,
  repository: AnkitVerseRepository,
  onClose: () -> Unit
) {
  var commentText by remember { mutableStateOf("") }
  val comments = repository.comments.value

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp)
  ) {
    Text(
      text = "Comments (${post.commentsCount + comments.size})",
      color = Color.White,
      fontWeight = FontWeight.Bold,
      fontSize = 16.sp
    )

    Spacer(modifier = Modifier.height(14.dp))

    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .height(300.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(comments) { c ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(NeonPurple),
              contentAlignment = Alignment.Center
            ) {
              Text(c.author.name.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(c.author.username, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
              Text(c.text, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
            }
          }
          Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Comment Input
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = commentText,
        onValueChange = { commentText = it },
        placeholder = { Text("Add a comment as ankit...") },
        modifier = Modifier.weight(1f),
        colors = OutlinedTextFieldDefaults.colors(
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White,
          focusedBorderColor = NeonPink,
          unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
        ),
        singleLine = true
      )

      Spacer(modifier = Modifier.width(8.dp))

      RainbowTouchContainer(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(Brush.horizontalGradient(listOf(NeonPink, NeonPurple)))
          .padding(horizontal = 16.dp, vertical = 14.dp),
        onClick = {
          if (commentText.isNotBlank()) {
            repository.addComment(commentText)
            commentText = ""
          }
        }
      ) {
        Text("Post", color = Color.White, fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
fun ShareSheetContent(
  post: Post,
  onClose: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(20.dp)
  ) {
    Text("Share Post", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(16.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceAround
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClose() }) {
        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(NeonPink),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text("Direct Message", color = Color.White, fontSize = 11.sp)
      }

      Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClose() }) {
        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(NeonBlue),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.NearMe, contentDescription = null, tint = Color.White)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text("Add to Story", color = Color.White, fontSize = 11.sp)
      }

      Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClose() }) {
        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(NeonAmber),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Bookmark, contentDescription = null, tint = Color.White)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text("Copy Link", color = Color.White, fontSize = 11.sp)
      }
    }
    Spacer(modifier = Modifier.height(20.dp))
  }
}
