package com.example.data

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AnkitVerseRepository {

  private val _currentUser = MutableStateFlow(DummyData.ankitUser)
  val currentUser: StateFlow<User> = _currentUser.asStateFlow()

  private val _isAuthenticated = MutableStateFlow(true)
  val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

  private val _posts = MutableStateFlow(DummyData.initialPosts)
  val posts: StateFlow<List<Post>> = _posts.asStateFlow()

  private val _stories = MutableStateFlow(DummyData.initialStories)
  val stories: StateFlow<List<Story>> = _stories.asStateFlow()

  private val _reels = MutableStateFlow(DummyData.initialReels)
  val reels: StateFlow<List<Reel>> = _reels.asStateFlow()

  private val _comments = MutableStateFlow(DummyData.sampleComments)
  val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

  private val _conversations = MutableStateFlow(DummyData.initialConversations)
  val conversations: StateFlow<List<ChatConversation>> = _conversations.asStateFlow()

  private val _activeMessages = MutableStateFlow(DummyData.sampleMessages)
  val activeMessages: StateFlow<List<ChatMessage>> = _activeMessages.asStateFlow()

  private val _videoProject = MutableStateFlow(DummyData.sampleProject)
  val videoProject: StateFlow<VideoProject> = _videoProject.asStateFlow()

  private val _creatorEarnings = MutableStateFlow(DummyData.initialEarnings)
  val creatorEarnings: StateFlow<CreatorEarnings> = _creatorEarnings.asStateFlow()

  // Custom Simplified Account Registration & Direct Login Store
  private val _registeredAccounts = mutableMapOf<String, Pair<String, User>>(
    "ankit" to Pair("123", DummyData.ankitUser.copy(age = 22)),
    "elena_cinematic" to Pair("123", DummyData.elenaUser.copy(age = 24)),
    "neotech" to Pair("123", DummyData.neoUser.copy(age = 26))
  )

  fun isUsernameTaken(username: String): Boolean {
    val clean = username.trim().removePrefix("@").lowercase()
    return _registeredAccounts.containsKey(clean)
  }

  fun directLogin(username: String, password: String): String? {
    val cleanUsername = username.trim().removePrefix("@").lowercase()
    if (cleanUsername.isEmpty()) return "Please enter your username"
    if (password.isEmpty()) return "Please enter your password"

    val account = _registeredAccounts[cleanUsername]
      ?: return "Username '@$cleanUsername' not found. Please sign up."

    if (account.first != password) {
      return "Incorrect password for '@$cleanUsername'."
    }

    _currentUser.value = account.second
    _isAuthenticated.value = true
    return null // Success
  }

  fun signUp(username: String, displayName: String, age: Int, password: String): String? {
    val cleanUsername = username.trim().removePrefix("@").lowercase()
    if (cleanUsername.length < 3) return "Username must be at least 3 characters"
    if (!cleanUsername.matches(Regex("^[a-z0-9_.]+$"))) {
      return "Username can only contain letters, numbers, underscores, and dots"
    }
    if (_registeredAccounts.containsKey(cleanUsername)) {
      return "Username '@$cleanUsername' is already taken. Please choose another."
    }
    if (displayName.trim().isEmpty()) {
      return "Please enter a display name"
    }
    if (age < 13) {
      return "Must be at least 13 years old to join AnkitVerse"
    }
    if (age > 120) {
      return "Please enter a valid age"
    }
    if (password.length < 4) {
      return "Password must be at least 4 characters"
    }

    val newUser = User(
      id = "user_${cleanUsername}_${System.currentTimeMillis()}",
      name = displayName.trim(),
      username = cleanUsername,
      avatarRes = null,
      bio = "New creator on AnkitVerse ✨ | Age $age",
      followersCount = 0,
      followingCount = 1,
      postsCount = 0,
      isVerified = false,
      totalViews = 0L,
      isMonetized = false,
      website = "ankitverse.io/@$cleanUsername",
      age = age
    )

    _registeredAccounts[cleanUsername] = Pair(password, newUser)
    _currentUser.value = newUser
    _isAuthenticated.value = true
    return null // Success
  }

  fun logout() {
    _isAuthenticated.value = false
  }

  fun updateProfile(name: String, bio: String, website: String) {
    _currentUser.value = _currentUser.value.copy(
      name = name,
      bio = bio,
      website = website
    )
  }

  // Post actions
  fun toggleLikePost(postId: String) {
    _posts.value = _posts.value.map { post ->
      if (post.id == postId) {
        val newLiked = !post.isLiked
        post.copy(
          isLiked = newLiked,
          likesCount = if (newLiked) post.likesCount + 1 else post.likesCount - 1
        )
      } else post
    }
  }

  fun toggleSavePost(postId: String) {
    _posts.value = _posts.value.map { post ->
      if (post.id == postId) post.copy(isSaved = !post.isSaved) else post
    }
  }

  fun addComment(text: String) {
    val newComment = Comment(
      id = "c_${System.currentTimeMillis()}",
      author = _currentUser.value,
      text = text,
      timestamp = "Just now",
      likesCount = 0,
      isLiked = false
    )
    _comments.value = listOf(newComment) + _comments.value
  }

  // Reel actions
  fun toggleLikeReel(reelId: String) {
    _reels.value = _reels.value.map { reel ->
      if (reel.id == reelId) {
        val newLiked = !reel.isLiked
        reel.copy(
          isLiked = newLiked,
          likesCount = if (newLiked) reel.likesCount + 1 else reel.likesCount - 1
        )
      } else reel
    }
  }

  // Story actions
  fun markStoryViewed(storyId: String) {
    _stories.value = _stories.value.map { story ->
      if (story.id == storyId) story.copy(isViewed = true) else story
    }
  }

  // Direct Message actions
  fun sendChatMessage(text: String, isDisappearing: Boolean = false, isVoiceNote: Boolean = false, voiceDurationSec: Int = 0) {
    val newMsg = ChatMessage(
      id = "msg_${System.currentTimeMillis()}",
      senderId = "me",
      senderName = _currentUser.value.name,
      text = text,
      timestamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()),
      isVoiceNote = isVoiceNote,
      voiceDurationSec = voiceDurationSec,
      isDisappearing = isDisappearing,
      isSeen = false
    )
    _activeMessages.value = _activeMessages.value + newMsg
  }

  // Video Editor Studio actions
  fun updateFilter(filterName: String, intensity: Float) {
    _videoProject.value = _videoProject.value.copy(
      selectedFilter = filterName,
      filterIntensity = intensity
    )
  }

  fun updateEffect(effectName: String) {
    _videoProject.value = _videoProject.value.copy(effectName = effectName)
  }

  fun updateTransition(transitionName: String) {
    _videoProject.value = _videoProject.value.copy(transitionName = transitionName)
  }

  fun updateMusicTrack(trackName: String) {
    _videoProject.value = _videoProject.value.copy(musicTrack = trackName)
  }

  fun toggleCaptions() {
    _videoProject.value = _videoProject.value.copy(dynamicCaptions = !_videoProject.value.dynamicCaptions)
  }

  fun splitCurrentClip() {
    val clips = _videoProject.value.clips.toMutableList()
    if (clips.isNotEmpty()) {
      val first = clips[0]
      val half = first.durationSec / 2
      val c1 = first.copy(id = "c_${System.currentTimeMillis()}_a", durationSec = half, endTrimSec = half)
      val c2 = first.copy(id = "c_${System.currentTimeMillis()}_b", durationSec = half, startTrimSec = 0f, endTrimSec = half)
      clips[0] = c1
      clips.add(1, c2)
      _videoProject.value = _videoProject.value.copy(clips = clips)
    }
  }

  fun updateClipSpeed(speed: Float) {
    val clips = _videoProject.value.clips.map { it.copy(speed = speed) }
    _videoProject.value = _videoProject.value.copy(clips = clips)
  }

  fun exportVideoToFeed(caption: String) {
    // Add exported creation directly into live Posts feed and live Reels feed!
    val newPost = Post(
      id = "post_${System.currentTimeMillis()}",
      author = _currentUser.value,
      imageRes = com.example.R.drawable.ankit_creator,
      caption = caption.ifBlank { "Exported directly from AnkitVerse CapCut Studio! 🎬🔥 #${_videoProject.value.selectedFilter}" },
      likesCount = 1,
      commentsCount = 0,
      sharesCount = 0,
      isLiked = true,
      isSaved = false,
      timestamp = "Just now",
      musicTrack = _videoProject.value.musicTrack,
      tags = listOf("#CapCutStudio", "#AnkitVerse", "#ViralVideo")
    )
    _posts.value = listOf(newPost) + _posts.value

    val newReel = Reel(
      id = "reel_${System.currentTimeMillis()}",
      author = _currentUser.value,
      mediaRes = com.example.R.drawable.ankit_creator,
      caption = caption.ifBlank { "Fresh export from CapCut Suite! 🎬 #AnkitVerse" },
      likesCount = 1,
      commentsCount = 0,
      sharesCount = 0,
      audioTitle = _videoProject.value.musicTrack,
      audioArtist = _currentUser.value.name,
      isLiked = true,
      tags = listOf("#Export4K", "#AnkitVerse")
    )
    _reels.value = listOf(newReel) + _reels.value
  }

  // Creator Monetization actions
  fun requestPayout(amount: Double, method: String) {
    val current = _creatorEarnings.value
    if (current.availableBalance >= amount) {
      val newRecord = PayoutRecord(
        id = "pay_${System.currentTimeMillis()}",
        date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()),
        amount = amount,
        method = method,
        status = "Processing (Instant Payout)"
      )
      _creatorEarnings.value = current.copy(
        availableBalance = current.availableBalance - amount,
        payoutHistory = listOf(newRecord) + current.payoutHistory
      )
    }
  }
}
