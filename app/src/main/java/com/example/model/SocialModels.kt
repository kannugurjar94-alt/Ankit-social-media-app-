package com.example.model

data class User(
  val id: String,
  val name: String,
  val username: String,
  val avatarRes: Int? = null,
  val avatarUrl: String? = null,
  val bio: String,
  val followersCount: Int,
  val followingCount: Int,
  val postsCount: Int,
  val isVerified: Boolean = false,
  val totalViews: Long = 0,
  val isMonetized: Boolean = false,
  val website: String = "ankitverse.io",
  val age: Int = 18
)

data class Post(
  val id: String,
  val author: User,
  val imageRes: Int? = null,
  val imageUrl: String? = null,
  val caption: String,
  val likesCount: Int,
  val commentsCount: Int,
  val sharesCount: Int,
  val isLiked: Boolean = false,
  val isSaved: Boolean = false,
  val timestamp: String,
  val musicTrack: String? = null,
  val tags: List<String> = emptyList()
)

data class Story(
  val id: String,
  val author: User,
  val mediaRes: Int? = null,
  val mediaUrl: String? = null,
  val textOverlay: String = "",
  val timestamp: String,
  val isViewed: Boolean = false
)

data class Reel(
  val id: String,
  val author: User,
  val mediaRes: Int? = null,
  val mediaUrl: String? = null,
  val caption: String,
  val likesCount: Int,
  val commentsCount: Int,
  val sharesCount: Int,
  val audioTitle: String,
  val audioArtist: String,
  val isLiked: Boolean = false,
  val tags: List<String> = emptyList()
)

data class Comment(
  val id: String,
  val author: User,
  val text: String,
  val timestamp: String,
  val likesCount: Int = 0,
  val isLiked: Boolean = false
)

data class ChatMessage(
  val id: String,
  val senderId: String,
  val senderName: String,
  val text: String,
  val timestamp: String,
  val isVoiceNote: Boolean = false,
  val voiceDurationSec: Int = 0,
  val isDisappearing: Boolean = false,
  val isSeen: Boolean = true
)

data class ChatConversation(
  val id: String,
  val participant: User,
  val lastMessage: String,
  val timestamp: String,
  val unreadCount: Int = 0,
  val isGroup: Boolean = false,
  val isOnline: Boolean = true
)

data class VideoClip(
  val id: String,
  val name: String,
  val durationSec: Float,
  val startTrimSec: Float,
  val endTrimSec: Float,
  val speed: Float = 1.0f,
  val filterName: String = "Cinematic"
)

data class VideoProject(
  val id: String,
  val title: String,
  val clips: List<VideoClip>,
  val selectedFilter: String = "Cyberpunk",
  val filterIntensity: Float = 0.85f,
  val effectName: String = "Glitch",
  val transitionName: String = "Whip Pan",
  val musicTrack: String = "Neon Pulse (Original Mix)",
  val dynamicCaptions: Boolean = true,
  val resolution: String = "1080p 60FPS"
)

data class PayoutRecord(
  val id: String,
  val date: String,
  val amount: Double,
  val method: String,
  val status: String
)

data class CreatorEarnings(
  val totalViews: Long,
  val threshold: Long = 500000L,
  val isUnlocked: Boolean,
  val monthlyRevenue: Double,
  val rpm: Double,
  val availableBalance: Double,
  val payoutHistory: List<PayoutRecord>
)
