package com.example.data

import com.example.R
import com.example.model.ChatConversation
import com.example.model.ChatMessage
import com.example.model.Comment
import com.example.model.CreatorEarnings
import com.example.model.PayoutRecord
import com.example.model.Post
import com.example.model.Reel
import com.example.model.Story
import com.example.model.User
import com.example.model.VideoClip
import com.example.model.VideoProject

object DummyData {
  // Main Developer / Creator Ankit
  val ankitUser = User(
    id = "user_ankit",
    name = "Ankit Gurjar",
    username = "ankit",
    avatarRes = R.drawable.ankit_creator,
    bio = "Creator & Architect of AnkitVerse ✨ | Filmmaker & AI Pioneer 🎬 | 500K+ Community 🚀",
    followersCount = 524850,
    followingCount = 184,
    postsCount = 48,
    isVerified = true,
    totalViews = 524850L,
    isMonetized = true,
    website = "ankitverse.io/creator"
  )

  val elenaUser = User(
    id = "user_elena",
    name = "Elena Rostova",
    username = "elena_cinematic",
    avatarRes = null,
    bio = "Colorist & 4K Travel Cinematographer 🎥 | Sony Alpha Ambassador",
    followersCount = 312000,
    followingCount = 420,
    postsCount = 89,
    isVerified = true,
    totalViews = 410000L,
    isMonetized = false
  )

  val neoUser = User(
    id = "user_neo",
    name = "Neo Tech Labs",
    username = "neotech",
    avatarRes = null,
    bio = "Pushing the boundaries of mobile tech & future gadgets ⚡",
    followersCount = 740000,
    followingCount = 92,
    postsCount = 135,
    isVerified = true,
    totalViews = 1850000L,
    isMonetized = true
  )

  val aaryaUser = User(
    id = "user_aarya",
    name = "Aarya Sharma",
    username = "aaryavibes",
    avatarRes = null,
    bio = "Music producer, beat maker & nocturnal creative 🎧✨",
    followersCount = 189000,
    followingCount = 310,
    postsCount = 62,
    isVerified = false,
    totalViews = 240000L,
    isMonetized = false
  )

  // Stories
  val initialStories = listOf(
    Story(
      id = "story_ankit",
      author = ankitUser,
      mediaRes = R.drawable.ankit_creator,
      textOverlay = "Welcome to AnkitVerse! 🚀 Built with love & passion",
      timestamp = "Just now",
      isViewed = false
    ),
    Story(
      id = "story_elena",
      author = elenaUser,
      mediaRes = null,
      textOverlay = "Golden hour grading session in Tokyo 🌅",
      timestamp = "2h ago",
      isViewed = false
    ),
    Story(
      id = "story_neo",
      author = neoUser,
      mediaRes = null,
      textOverlay = "Testing 4K 120FPS CapCut editing workflow 🔥",
      timestamp = "4h ago",
      isViewed = true
    ),
    Story(
      id = "story_aarya",
      author = aaryaUser,
      mediaRes = null,
      textOverlay = "New synth drop available in audio library 🎵",
      timestamp = "6h ago",
      isViewed = true
    )
  )

  // Feed Posts
  val initialPosts = listOf(
    Post(
      id = "post_ankit_1",
      author = ankitUser,
      imageRes = R.drawable.ankit_creator,
      caption = "Thrilled to unveil AnkitVerse! 🔥 Combining Instagram's social energy, CapCut's cutting-edge timeline studio, and our revolutionary 500k Creator Monetization program. What feature are you trying first?",
      likesCount = 42890,
      commentsCount = 1840,
      sharesCount = 5900,
      isLiked = true,
      isSaved = true,
      timestamp = "15m ago",
      musicTrack = "Ankit - Neon Pulse (Original)",
      tags = listOf("#AnkitVerse", "#CreatorEconomy", "#NextGen", "#VideoEditing")
    ),
    Post(
      id = "post_2",
      author = elenaUser,
      imageRes = null,
      caption = "Deep teal and orange color grade on 10-bit LOG footage. Rendered seamlessly inside the in-app CapCut editor suite! 🎥✨ Swipe for breakdown.",
      likesCount = 18450,
      commentsCount = 420,
      sharesCount = 1120,
      isLiked = false,
      isSaved = false,
      timestamp = "3h ago",
      musicTrack = "Lofi Sunset Chill",
      tags = listOf("#Cinematography", "#CapCutStudio", "#ColorGrading")
    ),
    Post(
      id = "post_3",
      author = neoUser,
      imageRes = null,
      caption = "The Satarangi Rainbow touch particle engine running at buttery 60 FPS! 🌈 Touch any button to feel the responsive particle feedback. Absolute design perfection.",
      likesCount = 33200,
      commentsCount = 980,
      sharesCount = 2840,
      isLiked = true,
      isSaved = false,
      timestamp = "5h ago",
      musicTrack = "Cybernetic Dreams",
      tags = listOf("#UIUX", "#Glassmorphism", "#InteractiveDesign")
    )
  )

  // Reels
  val initialReels = listOf(
    Reel(
      id = "reel_1",
      author = ankitUser,
      mediaRes = R.drawable.ankit_creator,
      caption = "Behind the scenes building the next-gen social & creative ecosystem! 💻✨ #AnkitVerse #CreatorEconomy",
      likesCount = 89400,
      commentsCount = 3120,
      sharesCount = 14200,
      audioTitle = "Neon Pulse (Ankit Studio Mix)",
      audioArtist = "Ankit Gurjar",
      isLiked = true,
      tags = listOf("#CreatorLife", "#TechVibe", "#Coding")
    ),
    Reel(
      id = "reel_2",
      author = elenaUser,
      mediaRes = null,
      caption = "How I edit cinematic travel reels in 60 seconds with multitrack timeline & whip pan transitions 🎬✈️",
      likesCount = 65200,
      commentsCount = 1480,
      sharesCount = 9200,
      audioTitle = "Tokyo Drift Melodic",
      audioArtist = "Elena Audio",
      isLiked = false,
      tags = listOf("#TravelReels", "#CapCut", "#EditingTutorial")
    ),
    Reel(
      id = "reel_3",
      author = aaryaUser,
      mediaRes = null,
      caption = "When the bass drops just as the video glitch effect hits 🔊⚡ Try this audio in your next reel!",
      likesCount = 48100,
      commentsCount = 890,
      sharesCount = 5400,
      audioTitle = "Midnight Bass Boost",
      audioArtist = "Aarya Beats",
      isLiked = true,
      tags = listOf("#Beats", "#GlitchFX", "#ViralAudio")
    )
  )

  // Comments
  val sampleComments = listOf(
    Comment(
      id = "c1",
      author = aaryaUser,
      text = "The UI glassmorphism is stunning Ankit! Best social app of the decade 🔥",
      timestamp = "10m ago",
      likesCount = 142,
      isLiked = true
    ),
    Comment(
      id = "c2",
      author = elenaUser,
      text = "The video studio timeline is ridiculously fast. No lag even on heavy filters! 👏",
      timestamp = "8m ago",
      likesCount = 89,
      isLiked = false
    ),
    Comment(
      id = "c3",
      author = neoUser,
      text = "Monetization starting at 500k views is a total game changer for independent creators 💰",
      timestamp = "5m ago",
      likesCount = 210,
      isLiked = true
    )
  )

  // Direct Messages
  val initialConversations = listOf(
    ChatConversation(
      id = "conv_ankit",
      participant = ankitUser,
      lastMessage = "Thanks for joining AnkitVerse! Let me know if you love the video editor.",
      timestamp = "12:45 PM",
      unreadCount = 1,
      isOnline = true
    ),
    ChatConversation(
      id = "conv_elena",
      participant = elenaUser,
      lastMessage = "Sent you the 4K LUT package for your next vlog!",
      timestamp = "Yesterday",
      unreadCount = 0,
      isOnline = true
    ),
    ChatConversation(
      id = "conv_neo",
      participant = neoUser,
      lastMessage = "Did your 500k monetization payout come through yet?",
      timestamp = "Oct 28",
      unreadCount = 0,
      isOnline = false
    )
  )

  val sampleMessages = listOf(
    ChatMessage(
      id = "m1",
      senderId = "user_ankit",
      senderName = "Ankit Gurjar",
      text = "Hey! Welcome to AnkitVerse. Explore the stories tray, watch full-screen reels, and test out the video editor! 🎬",
      timestamp = "12:40 PM",
      isSeen = true
    ),
    ChatMessage(
      id = "m2",
      senderId = "me",
      senderName = "You",
      text = "This app looks phenomenal! The rainbow smoke touch effect feels so responsive.",
      timestamp = "12:42 PM",
      isSeen = true
    ),
    ChatMessage(
      id = "m3",
      senderId = "user_ankit",
      senderName = "Ankit Gurjar",
      text = "Thanks! Also check the Creator Monetization dashboard in your profile – once you cross 500k views, instant payouts unlock! 💰",
      timestamp = "12:45 PM",
      isSeen = false
    )
  )

  // Video Editor Initial Project
  val sampleProject = VideoProject(
    id = "proj_1",
    title = "My Viral Reel 01",
    clips = listOf(
      VideoClip("clip_1", "Intro_Shot.mp4", 4.2f, 0.0f, 4.2f, 1.0f, "Cinematic"),
      VideoClip("clip_2", "Studio_Action.mp4", 3.8f, 0.5f, 3.8f, 1.2f, "Cyberpunk"),
      VideoClip("clip_3", "Outro_Brand.mp4", 2.5f, 0.0f, 2.5f, 1.0f, "Vintage Film")
    ),
    selectedFilter = "Cyberpunk",
    filterIntensity = 0.85f,
    effectName = "Glitch",
    transitionName = "Whip Pan",
    musicTrack = "Neon Pulse (Original Mix)",
    dynamicCaptions = true,
    resolution = "1080p 60FPS"
  )

  // Creator Earnings Data
  val initialEarnings = CreatorEarnings(
    totalViews = 524850L,
    threshold = 500000L,
    isUnlocked = true, // 524k > 500k threshold!
    monthlyRevenue = 4850.20,
    rpm = 1.85,
    availableBalance = 2450.00,
    payoutHistory = listOf(
      PayoutRecord("pay_1", "Nov 01, 2026", 1200.20, "Direct Bank (IBAN)", "Paid"),
      PayoutRecord("pay_2", "Oct 15, 2026", 1200.00, "UPI / Digital Wallet", "Paid")
    )
  )
}
