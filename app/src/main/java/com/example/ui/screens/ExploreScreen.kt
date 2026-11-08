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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Post
import com.example.model.Reel
import com.example.ui.effects.RainbowTouchContainer
import com.example.ui.glass.iosGlass
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.ObsidianDark

data class ExploreItem(
  val id: String,
  val title: String,
  val category: String,
  val isVideo: Boolean = false,
  val likes: String,
  val imageRes: Int? = null,
  val gradient: List<Color> = listOf(NeonPink, NeonPurple)
)

@Composable
fun ExploreScreen(
  posts: List<Post>,
  reels: List<Reel>,
  onReelClick: (Reel) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("All") }
  var previewItem by remember { mutableStateOf<ExploreItem?>(null) }

  val categories = listOf("All", "Viral Reels", "Cinematic", "500k Club", "Tech", "Fashion", "Music")

  val exploreItems = remember {
    listOf(
      ExploreItem("e1", "AnkitVerse Launch Keynote", "500k Club", true, "124K", R.drawable.ankit_creator),
      ExploreItem("e2", "Tokyo Night Grade in CapCut", "Cinematic", true, "89K", null, listOf(NeonBlue, NeonPurple)),
      ExploreItem("e3", "Satarangi Smoke Touch Engine", "Tech", false, "45K", null, listOf(NeonPink, NeonAmber)),
      ExploreItem("e4", "Creator Studio Monetization $4.8K", "500k Club", false, "78K", R.drawable.app_icon_asset),
      ExploreItem("e5", "Sunset Cinematic LUT", "Cinematic", true, "62K", null, listOf(Color(0xFFFF5E62), Color(0xFFFF9966))),
      ExploreItem("e6", "Cyberpunk Neon Visuals", "Tech", true, "91K", null, listOf(Color(0xFF00F2FE), Color(0xFF4FACFE))),
      ExploreItem("e7", "Beat drop editing breakdown", "Music", true, "39K", null, listOf(Color(0xFF654EA3), Color(0xFFEAAFC8))),
      ExploreItem("e8", "Fashion Week 4K Reel", "Fashion", true, "115K", null, listOf(Color(0xFF7F00FF), Color(0xFFE100FF)))
    )
  }

  val filteredItems = exploreItems.filter {
    (selectedCategory == "All" || it.category == selectedCategory) &&
      (searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true))
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianDark)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Search Header
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search hashtags, creators, or sounds...", fontSize = 13.sp) },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonPink) },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Close, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
              }
            }
          },
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = NeonPink,
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedContainerColor = Color(0x331E2338),
            unfocusedContainerColor = Color(0x331E2338)
          ),
          singleLine = true,
          shape = RoundedCornerShape(20.dp),
          modifier = Modifier.fillMaxWidth()
        )
      }

      // Horizontal Category Pills
      LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(categories) { cat ->
          val isSelected = cat == selectedCategory
          RainbowTouchContainer(
            modifier = Modifier
              .clip(RoundedCornerShape(16.dp))
              .background(
                if (isSelected) Brush.horizontalGradient(listOf(NeonPink, NeonPurple))
                else Brush.linearGradient(listOf(Color.White.copy(alpha = 0.1f), Color.White.copy(alpha = 0.1f)))
              )
              .border(1.dp, if (isSelected) Color.White.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
              .padding(horizontal = 14.dp, vertical = 6.dp),
            onClick = { selectedCategory = cat }
          ) {
            Text(
              text = cat,
              color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              fontSize = 12.sp
            )
          }
        }
      }

      // Staggered Discovery Grid
      LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 100.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        items(filteredItems, key = { it.id }) { item ->
          Box(
            modifier = Modifier
              .height(130.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(Brush.linearGradient(item.gradient))
              .clickable { previewItem = item }
          ) {
            if (item.imageRes != null) {
              Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )
            }

            // Dark bottom vignette
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(
                  Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                  )
                )
            )

            // Video indicator
            if (item.isVideo) {
              Box(
                modifier = Modifier
                  .align(Alignment.TopEnd)
                  .padding(6.dp)
                  .clip(CircleShape)
                  .background(Color.Black.copy(alpha = 0.5f))
                  .padding(4.dp)
              ) {
                Icon(Icons.Default.Videocam, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
              }
            }

            // Likes badge
            Row(
              modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
              Spacer(modifier = Modifier.width(2.dp))
              Text(item.likes, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Detail Preview Modal
    previewItem?.let { item ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = 0.8f))
          .clickable { previewItem = null }
          .padding(24.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF191D30))
            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
            .padding(20.dp)
            .clickable(enabled = false) {},
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(item.category, color = NeonPink, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            IconButton(onClick = { previewItem = null }) {
              Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(220.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(Brush.linearGradient(item.gradient)),
            contentAlignment = Alignment.Center
          ) {
            if (item.imageRes != null) {
              Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )
            }
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(item.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
          Text("${item.likes} Views & Engagements", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)

          Spacer(modifier = Modifier.height(16.dp))

          RainbowTouchContainer(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Brush.horizontalGradient(listOf(NeonPink, NeonPurple)))
              .padding(vertical = 12.dp),
            onClick = { previewItem = null }
          ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
              Text("Watch Full Video", color = Color.White, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}
