package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AnkitVerseRepository
import com.example.model.Post
import com.example.model.Reel
import com.example.model.User
import com.example.ui.effects.RainbowTouchContainer
import com.example.ui.glass.iosGlass
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.ObsidianDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
  repository: AnkitVerseRepository,
  currentUser: User,
  posts: List<Post>,
  reels: List<Reel>,
  onOpenControlCenter: () -> Unit,
  onOpenMonetization: () -> Unit,
  onLogout: () -> Unit = {}
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Posts, 1: Reels, 2: Saved
  var showEditProfileSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val highlights = listOf(
    "🎬 Studio" to NeonPink,
    "💰 500k Club" to NeonAmber,
    "🚀 Tech" to NeonBlue,
    "✈️ Travel" to NeonGreen,
    "📸 Moments" to NeonPurple
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianDark)
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 100.dp)
    ) {
      // Top Header Row
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = currentUser.username,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp
            )
            if (currentUser.isVerified) {
              Spacer(modifier = Modifier.width(6.dp))
              Icon(Icons.Default.Star, contentDescription = "Verified", tint = NeonAmber, modifier = Modifier.size(18.dp))
            }
          }

          IconButton(onClick = onOpenControlCenter) {
            Icon(Icons.Default.Tune, contentDescription = "Control Center", tint = NeonBlue)
          }
        }
      }

      // Profile Header: Avatar & Stats
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          // Large Creator Avatar with glowing ring
          Box(
            modifier = Modifier
              .size(86.dp)
              .clip(CircleShape)
              .background(Brush.sweepGradient(listOf(NeonPink, NeonPurple, NeonAmber, NeonPink)))
              .padding(3.dp)
          ) {
            if (currentUser.avatarRes != null) {
              Image(
                painter = painterResource(id = currentUser.avatarRes),
                contentDescription = currentUser.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .fillMaxSize()
                  .clip(CircleShape)
                  .border(2.dp, ObsidianDark, CircleShape)
              )
            } else {
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .clip(CircleShape)
                  .background(Brush.linearGradient(listOf(NeonBlue, NeonPurple))),
                contentAlignment = Alignment.Center
              ) {
                Text(currentUser.name.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
              }
            }
          }

          // Stats: Posts, Followers, Following
          Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            ProfileStatItem(label = "Posts", value = "${posts.size}")
            ProfileStatItem(label = "Followers", value = "524.8K")
            ProfileStatItem(label = "Following", value = "${currentUser.followingCount}")
          }
        }
      }

      // Bio & Links
      item {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
          Text(currentUser.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = currentUser.bio,
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 13.sp,
            lineHeight = 18.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "🔗 ${currentUser.website}",
            color = NeonBlue,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      // Action Buttons (Edit Profile, Share Profile)
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          RainbowTouchContainer(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(Color.White.copy(alpha = 0.12f))
              .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
              .padding(vertical = 10.dp),
            onClick = { showEditProfileSheet = true }
          ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
              Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Edit Profile", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
          }

          RainbowTouchContainer(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(Color.White.copy(alpha = 0.12f))
              .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
              .padding(vertical = 10.dp),
            onClick = {}
          ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
              Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Share Profile", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
          }
        }
      }

      // 500k Creator Monetization Program Promo Card
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
              Brush.horizontalGradient(
                listOf(Color(0xFF2E1903), Color(0xFF422108), Color(0xFF1E1303))
              )
            )
            .border(1.dp, NeonAmber.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
            .clickable { onOpenMonetization() }
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(Brush.linearGradient(listOf(NeonAmber, Color(0xFFFF6600)))),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text("500k Creator Program", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                  Spacer(modifier = Modifier.width(6.dp))
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(NeonGreen)
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text("UNLOCKED", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                  }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text("Earned $4,850.20 this month • Instant Payouts", color = NeonAmber, fontSize = 12.sp)
              }
            }

            Text("View ➔", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
      }

      // Highlights Tray
      item {
        LazyRow(
          modifier = Modifier.fillMaxWidth(),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          items(highlights) { (name, color) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Box(
                modifier = Modifier
                  .size(62.dp)
                  .clip(CircleShape)
                  .background(Color.White.copy(alpha = 0.08f))
                  .border(1.5.dp, color, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Box(
                  modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.3f))
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(name, color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
            }
          }
        }
      }

      // Tab switcher: Posts, Reels, Saved
      item {
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = Color.Transparent,
          contentColor = NeonPink,
          indicator = { tabPositions ->
            SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = NeonPink
            )
          }
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            icon = { Icon(Icons.Default.GridView, contentDescription = "Posts") }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            icon = { Icon(Icons.Default.Videocam, contentDescription = "Reels") }
          )
          Tab(
            selected = selectedTab == 2,
            onClick = { selectedTab = 2 },
            icon = { Icon(Icons.Default.Bookmark, contentDescription = "Saved") }
          )
        }
      }

      // Media Grid Content
      item {
        Box(modifier = Modifier.height(350.dp)) {
          when (selectedTab) {
            0 -> {
              LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
              ) {
                items(posts) { post ->
                  Box(
                    modifier = Modifier
                      .height(115.dp)
                      .background(Brush.linearGradient(listOf(Color(0xFF251B35), Color(0xFF140D26))))
                  ) {
                    if (post.imageRes != null) {
                      Image(
                        painter = painterResource(id = post.imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                      )
                    }
                  }
                }
              }
            }
            1 -> {
              LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
              ) {
                items(reels) { reel ->
                  Box(
                    modifier = Modifier
                      .height(140.dp)
                      .background(Brush.linearGradient(listOf(NeonPurple, NeonPink))),
                    contentAlignment = Alignment.BottomStart
                  ) {
                    if (reel.mediaRes != null) {
                      Image(
                        painter = painterResource(id = reel.mediaRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                      )
                    }
                    Text(
                      text = "${reel.likesCount}",
                      color = Color.White,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(6.dp)
                    )
                  }
                }
              }
            }
            2 -> {
              Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Saved Collections & Bookmarks", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
              }
            }
          }
        }
      }
    }

    // Edit Profile Modal Sheet
    if (showEditProfileSheet) {
      ModalBottomSheet(
        onDismissRequest = { showEditProfileSheet = false },
        sheetState = sheetState,
        containerColor = Color(0xFF161928),
        contentColor = Color.White
      ) {
        var editName by remember { mutableStateOf(currentUser.name) }
        var editBio by remember { mutableStateOf(currentUser.bio) }
        var editWebsite by remember { mutableStateOf(currentUser.website) }

        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Text("Edit Profile", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = editName,
            onValueChange = { editName = it },
            label = { Text("Display Name") },
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedBorderColor = NeonPink
            ),
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = editBio,
            onValueChange = { editBio = it },
            label = { Text("Bio") },
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedBorderColor = NeonPink
            ),
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = editWebsite,
            onValueChange = { editWebsite = it },
            label = { Text("Website / Portfolio") },
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedBorderColor = NeonPink
            ),
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(20.dp))

          RainbowTouchContainer(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Brush.horizontalGradient(listOf(NeonPink, NeonPurple)))
              .padding(vertical = 12.dp),
            onClick = {
              repository.updateProfile(editName, editBio, editWebsite)
              showEditProfileSheet = false
            }
          ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
              Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Switch Account / Log Out
          RainbowTouchContainer(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Color.White.copy(alpha = 0.08f))
              .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
              .padding(vertical = 12.dp),
            onClick = {
              showEditProfileSheet = false
              onLogout()
            }
          ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
              Text("Switch Account / Log Out", color = Color(0xFFFF5577), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
          }
        }
      }
    }
  }
}

@Composable
fun ProfileStatItem(label: String, value: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    Text(text = label, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
  }
}
