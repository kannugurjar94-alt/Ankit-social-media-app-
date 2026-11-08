package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.data.AnkitVerseRepository
import com.example.ui.effects.RainbowTouchContainer
import com.example.ui.glass.IOSControlCenterDialog
import com.example.ui.glass.iosGlass
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CreatorMonetizationScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MessagesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ReelsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.VideoEditorScreen
import com.example.ui.theme.AnkitVerseTheme
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.ObsidianDark

enum class Screen {
  Splash,
  Auth,
  Home,
  Explore,
  VideoStudio,
  Reels,
  Profile,
  Messages,
  Monetization
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val repository = AnkitVerseRepository()

    setContent {
      AnkitVerseTheme {
        AnkitVerseApp(repository = repository)
      }
    }
  }
}

@Composable
fun AnkitVerseApp(repository: AnkitVerseRepository) {
  var currentScreen by remember { mutableStateOf(Screen.Splash) }
  var previousScreen by remember { mutableStateOf(Screen.Home) }
  var showControlCenter by remember { mutableStateOf(false) }

  val currentUser by repository.currentUser.collectAsState()
  val isAuthenticated by repository.isAuthenticated.collectAsState()
  val posts by repository.posts.collectAsState()
  val stories by repository.stories.collectAsState()
  val reels by repository.reels.collectAsState()
  val conversations by repository.conversations.collectAsState()
  val activeMessages by repository.activeMessages.collectAsState()
  val earnings by repository.creatorEarnings.collectAsState()

  // Handle hardware / predictive back navigation across screens
  BackHandler(enabled = currentScreen != Screen.Home && currentScreen != Screen.Splash && currentScreen != Screen.Auth) {
    currentScreen = when (currentScreen) {
      Screen.Messages -> Screen.Home
      Screen.Monetization -> previousScreen
      Screen.VideoStudio -> previousScreen
      else -> Screen.Home
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = ObsidianDark,
    contentWindowInsets = WindowInsets(0, 0, 0, 0)
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      // Screen Router with smooth fade transitions
      AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "screen_router"
      ) { target ->
        when (target) {
          Screen.Splash -> {
            SplashScreen(
              onSplashFinished = {
                currentScreen = if (isAuthenticated) Screen.Home else Screen.Auth
              }
            )
          }

          Screen.Auth -> {
            AuthScreen(
              repository = repository,
              onLoginSuccess = {
                currentScreen = Screen.Home
              }
            )
          }

          Screen.Home -> {
            HomeScreen(
              repository = repository,
              posts = posts,
              stories = stories,
              currentUser = currentUser,
              onOpenDMs = {
                previousScreen = Screen.Home
                currentScreen = Screen.Messages
              },
              onOpenControlCenter = { showControlCenter = true },
              onOpenVideoStudio = {
                previousScreen = Screen.Home
                currentScreen = Screen.VideoStudio
              },
              onNavigateToMonetization = {
                previousScreen = Screen.Home
                currentScreen = Screen.Monetization
              }
            )
          }

          Screen.Explore -> {
            ExploreScreen(
              posts = posts,
              reels = reels,
              onReelClick = { currentScreen = Screen.Reels }
            )
          }

          Screen.VideoStudio -> {
            VideoEditorScreen(
              repository = repository,
              onClose = { currentScreen = previousScreen },
              onExportSuccess = { currentScreen = Screen.Home }
            )
          }

          Screen.Reels -> {
            ReelsScreen(
              repository = repository,
              reels = reels,
              onOpenVideoStudio = {
                previousScreen = Screen.Reels
                currentScreen = Screen.VideoStudio
              }
            )
          }

          Screen.Profile -> {
            ProfileScreen(
              repository = repository,
              currentUser = currentUser,
              posts = posts,
              reels = reels,
              onOpenControlCenter = { showControlCenter = true },
              onOpenMonetization = {
                previousScreen = Screen.Profile
                currentScreen = Screen.Monetization
              },
              onLogout = {
                repository.logout()
                currentScreen = Screen.Auth
              }
            )
          }

          Screen.Messages -> {
            MessagesScreen(
              repository = repository,
              conversations = conversations,
              activeMessages = activeMessages,
              currentUser = currentUser,
              onBack = { currentScreen = Screen.Home }
            )
          }

          Screen.Monetization -> {
            CreatorMonetizationScreen(
              repository = repository,
              earnings = earnings,
              onBack = { currentScreen = previousScreen }
            )
          }
        }
      }

      // Floating iOS Glass Bottom Navigation Bar (visible on primary screens)
      if (currentScreen in listOf(Screen.Home, Screen.Explore, Screen.Reels, Screen.Profile)) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
          IOSGlassBottomBar(
            currentScreen = currentScreen,
            onSelectScreen = { screen ->
              if (screen == Screen.VideoStudio) {
                previousScreen = currentScreen
              }
              currentScreen = screen
            },
            currentUserAvatar = currentUser.avatarRes
          )
        }
      }

      // iOS Control Center Glass Panel
      IOSControlCenterDialog(
        isOpen = showControlCenter,
        onDismiss = { showControlCenter = false },
        onOpenVideoStudio = {
          previousScreen = currentScreen
          currentScreen = Screen.VideoStudio
        },
        onOpenMonetization = {
          previousScreen = currentScreen
          currentScreen = Screen.Monetization
        }
      )
    }
  }
}

@Composable
fun IOSGlassBottomBar(
  currentScreen: Screen,
  onSelectScreen: (Screen) -> Unit,
  currentUserAvatar: Int?
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(64.dp)
      .iosGlass(
        shape = RoundedCornerShape(32.dp),
        backgroundColor = Color(0xD0171B2B),
        borderColor = Color.White.copy(alpha = 0.22f)
      )
      .padding(horizontal = 14.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Home Tab
    RainbowTouchContainer(
      modifier = Modifier
        .size(44.dp)
        .clip(CircleShape),
      onClick = { onSelectScreen(Screen.Home) }
    ) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Icon(
          imageVector = if (currentScreen == Screen.Home) Icons.Default.Home else Icons.Outlined.Home,
          contentDescription = "Home",
          tint = if (currentScreen == Screen.Home) NeonPink else Color.White.copy(alpha = 0.65f),
          modifier = Modifier.size(26.dp)
        )
      }
    }

    // Explore Tab
    RainbowTouchContainer(
      modifier = Modifier
        .size(44.dp)
        .clip(CircleShape),
      onClick = { onSelectScreen(Screen.Explore) }
    ) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Icon(
          imageVector = if (currentScreen == Screen.Explore) Icons.Default.Search else Icons.Outlined.Search,
          contentDescription = "Explore",
          tint = if (currentScreen == Screen.Explore) NeonPink else Color.White.copy(alpha = 0.65f),
          modifier = Modifier.size(26.dp)
        )
      }
    }

    // Video Studio Action Center Button (+)
    RainbowTouchContainer(
      modifier = Modifier
        .size(46.dp)
        .clip(CircleShape)
        .background(Brush.linearGradient(listOf(NeonPink, NeonPurple)))
        .border(1.5.dp, Color.White.copy(alpha = 0.4f), CircleShape),
      onClick = { onSelectScreen(Screen.VideoStudio) }
    ) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Studio",
          tint = Color.White,
          modifier = Modifier.size(26.dp)
        )
      }
    }

    // Reels Tab
    RainbowTouchContainer(
      modifier = Modifier
        .size(44.dp)
        .clip(CircleShape),
      onClick = { onSelectScreen(Screen.Reels) }
    ) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Icon(
          imageVector = if (currentScreen == Screen.Reels) Icons.Default.Videocam else Icons.Outlined.Videocam,
          contentDescription = "Reels",
          tint = if (currentScreen == Screen.Reels) NeonPink else Color.White.copy(alpha = 0.65f),
          modifier = Modifier.size(26.dp)
        )
      }
    }

    // Profile Tab
    RainbowTouchContainer(
      modifier = Modifier
        .size(44.dp)
        .clip(CircleShape),
      onClick = { onSelectScreen(Screen.Profile) }
    ) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (currentUserAvatar != null) {
          Image(
            painter = painterResource(id = currentUserAvatar),
            contentDescription = "Profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .border(
                1.5.dp,
                if (currentScreen == Screen.Profile) NeonPink else Color.White.copy(alpha = 0.5f),
                CircleShape
              )
          )
        } else {
          Icon(
            imageVector = if (currentScreen == Screen.Profile) Icons.Default.Person else Icons.Outlined.Person,
            contentDescription = "Profile",
            tint = if (currentScreen == Screen.Profile) NeonPink else Color.White.copy(alpha = 0.65f),
            modifier = Modifier.size(26.dp)
          )
        }
      }
    }
  }
}
