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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import com.example.data.AnkitVerseRepository
import com.example.model.ChatConversation
import com.example.model.ChatMessage
import com.example.model.User
import com.example.ui.effects.RainbowTouchContainer
import com.example.ui.glass.iosGlass
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.ObsidianDark

@Composable
fun MessagesScreen(
  repository: AnkitVerseRepository,
  conversations: List<ChatConversation>,
  activeMessages: List<ChatMessage>,
  currentUser: User,
  onBack: () -> Unit
) {
  var selectedConversation by remember { mutableStateOf<ChatConversation?>(null) }

  if (selectedConversation != null) {
    ChatDetailScreen(
      conversation = selectedConversation!!,
      messages = activeMessages,
      onBack = { selectedConversation = null },
      onSendMessage = { text, isDisappearing, isVoice ->
        repository.sendChatMessage(text, isDisappearing = isDisappearing, isVoiceNote = isVoice)
      }
    )
  } else {
    InboxListScreen(
      conversations = conversations,
      onSelectConversation = { selectedConversation = it },
      onBack = onBack
    )
  }
}

@Composable
fun InboxListScreen(
  conversations: List<ChatConversation>,
  onSelectConversation: (ChatConversation) -> Unit,
  onBack: () -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianDark)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top Glass Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Direct Messages",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
          )
        }

        IconButton(onClick = {}) {
          Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = NeonPink)
        }
      }

      // Search
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search friends & creators...", fontSize = 13.sp) },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.5f)) },
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = NeonPink,
            unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
            focusedContainerColor = Color(0x301E2338),
            unfocusedContainerColor = Color(0x301E2338)
          ),
          singleLine = true,
          shape = RoundedCornerShape(18.dp),
          modifier = Modifier.fillMaxWidth()
        )
      }

      // Active Friends Row
      Text(
        text = "ACTIVE NOW",
        color = Color.White.copy(alpha = 0.5f),
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
      )

      LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(conversations) { conv ->
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onSelectConversation(conv) }
          ) {
            Box(modifier = Modifier.size(54.dp)) {
              if (conv.participant.avatarRes != null) {
                Image(
                  painter = painterResource(id = conv.participant.avatarRes),
                  contentDescription = null,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(2.dp, NeonPink, CircleShape)
                )
              } else {
                Box(
                  modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(NeonBlue, NeonPurple))),
                  contentAlignment = Alignment.Center
                ) {
                  Text(conv.participant.name.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                }
              }

              if (conv.isOnline) {
                Box(
                  modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(NeonGreen)
                    .border(2.dp, ObsidianDark, CircleShape)
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = conv.participant.username,
              color = Color.White.copy(alpha = 0.8f),
              fontSize = 11.sp,
              maxLines = 1
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Messages List
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 90.dp)
      ) {
        items(conversations) { conv ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onSelectConversation(conv) }
              .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(modifier = Modifier.size(52.dp)) {
                if (conv.participant.avatarRes != null) {
                  Image(
                    painter = painterResource(id = conv.participant.avatarRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                      .fillMaxSize()
                      .clip(CircleShape)
                  )
                } else {
                  Box(
                    modifier = Modifier
                      .fillMaxSize()
                      .clip(CircleShape)
                      .background(Brush.linearGradient(listOf(NeonPink, NeonPurple))),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(conv.participant.name.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                  }
                }
              }

              Spacer(modifier = Modifier.width(14.dp))

              Column {
                Text(
                  text = conv.participant.name,
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = conv.lastMessage,
                  color = if (conv.unreadCount > 0) Color.White else Color.White.copy(alpha = 0.6f),
                  fontSize = 13.sp,
                  maxLines = 1,
                  fontWeight = if (conv.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal
                )
              }
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(conv.timestamp, color = Color.White.copy(alpha = 0.45f), fontSize = 11.sp)
              if (conv.unreadCount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                  modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(NeonPink),
                  contentAlignment = Alignment.Center
                ) {
                  Text("${conv.unreadCount}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun ChatDetailScreen(
  conversation: ChatConversation,
  messages: List<ChatMessage>,
  onBack: () -> Unit,
  onSendMessage: (String, Boolean, Boolean) -> Unit
) {
  var messageInput by remember { mutableStateOf("") }
  var isDisappearingMode by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianDark)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Chat Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 12.dp)
          .iosGlass(RoundedCornerShape(20.dp))
          .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          if (conversation.participant.avatarRes != null) {
            Image(
              painter = painterResource(id = conversation.participant.avatarRes),
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(conversation.participant.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("Active now", color = NeonGreen, fontSize = 11.sp)
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Disappearing Message Toggle Button
          IconButton(onClick = { isDisappearingMode = !isDisappearingMode }) {
            Icon(
              Icons.Default.Timer,
              contentDescription = "Disappearing Mode",
              tint = if (isDisappearingMode) NeonAmber else Color.White.copy(alpha = 0.6f)
            )
          }
          IconButton(onClick = {}) {
            Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = Color.White)
          }
        }
      }

      if (isDisappearingMode) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x33FFB800))
            .padding(vertical = 4.dp),
          contentAlignment = Alignment.Center
        ) {
          Text("⏳ Disappearing Messages ON (vanishes after reading)", color = NeonAmber, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
      }

      // Messages Scroll
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(messages) { msg ->
          val isMe = msg.senderId == "me"
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
          ) {
            Box(
              modifier = Modifier
                .clip(
                  RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = if (isMe) 18.dp else 4.dp,
                    bottomEnd = if (isMe) 4.dp else 18.dp
                  )
                )
                .background(
                  if (isMe) Brush.horizontalGradient(listOf(NeonPink, NeonPurple))
                  else Brush.linearGradient(listOf(Color(0xFF23283E), Color(0xFF1B2032)))
                )
                .border(
                  1.dp,
                  if (isMe) Color.White.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.12f),
                  RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
              if (msg.isVoiceNote) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Voice Note (${msg.voiceDurationSec}s)", color = Color.White, fontSize = 13.sp)
                }
              } else {
                Text(
                  text = msg.text,
                  color = Color.White,
                  fontSize = 14.sp,
                  lineHeight = 20.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(msg.timestamp, color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
              if (isMe) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.DoneAll, contentDescription = "Seen", tint = NeonBlue, modifier = Modifier.size(12.dp))
              }
            }
          }
        }
      }

      // Bottom Message Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 10.dp)
          .iosGlass(RoundedCornerShape(26.dp))
          .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = messageInput,
          onValueChange = { messageInput = it },
          placeholder = { Text("Message...", fontSize = 14.sp) },
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
          ),
          modifier = Modifier.weight(1f),
          singleLine = true
        )

        // Voice note quick action
        IconButton(
          onClick = {
            onSendMessage("Voice Note recorded in Ankit Studio", isDisappearingMode, true)
          }
        ) {
          Icon(Icons.Default.Mic, contentDescription = "Voice", tint = NeonBlue)
        }

        // Send Button with Rainbow Particle Touch
        RainbowTouchContainer(
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Brush.horizontalGradient(listOf(NeonPink, NeonPurple))),
          onClick = {
            if (messageInput.isNotBlank()) {
              onSendMessage(messageInput, isDisappearingMode, false)
              messageInput = ""
            }
          }
        ) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
          }
        }
      }
    }
  }
}
