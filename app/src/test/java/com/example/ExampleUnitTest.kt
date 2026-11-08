package com.example

import com.example.data.AnkitVerseRepository
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testCreatorInitializationAndMonetization() {
    val repo = AnkitVerseRepository()
    val user = repo.currentUser.value

    assertEquals("ankit", user.username)
    assertTrue("Ankit should be verified creator", user.isVerified)
    assertTrue("Views must exceed 500k threshold", user.totalViews >= 500000L)

    val earnings = repo.creatorEarnings.value
    assertTrue("Creator monetization must be unlocked", earnings.isUnlocked)
    assertTrue("Earnings must have positive monthly revenue", earnings.monthlyRevenue > 0)
  }

  @Test
  fun testFeedPostLikeAndSave() {
    val repo = AnkitVerseRepository()
    val initialPosts = repo.posts.value
    assertFalse(initialPosts.isEmpty())

    val firstPost = initialPosts[0]
    val initialLikes = firstPost.likesCount

    repo.toggleLikePost(firstPost.id)
    val updatedPost = repo.posts.value.find { it.id == firstPost.id }!!
    assertNotEquals(initialLikes, updatedPost.likesCount)

    repo.toggleSavePost(firstPost.id)
    val savedPost = repo.posts.value.find { it.id == firstPost.id }!!
    assertNotEquals(firstPost.isSaved, savedPost.isSaved)
  }

  @Test
  fun testVideoEditorStudioExportToFeed() {
    val repo = AnkitVerseRepository()
    val initialPostCount = repo.posts.value.size
    val initialReelCount = repo.reels.value.size

    repo.exportVideoToFeed("Testing CapCut 4K export to feed!")

    assertEquals(initialPostCount + 1, repo.posts.value.size)
    assertEquals(initialReelCount + 1, repo.reels.value.size)
    assertEquals("Testing CapCut 4K export to feed!", repo.posts.value[0].caption)
  }

  @Test
  fun testInstantPayoutDeduction() {
    val repo = AnkitVerseRepository()
    val initialBalance = repo.creatorEarnings.value.availableBalance
    val withdrawAmount = 500.0

    repo.requestPayout(withdrawAmount, "Direct Bank (IBAN)")
    val newBalance = repo.creatorEarnings.value.availableBalance

    assertEquals(initialBalance - withdrawAmount, newBalance, 0.01)
    assertEquals("Direct Bank (IBAN)", repo.creatorEarnings.value.payoutHistory[0].method)
  }

  @Test
  fun testCustomDirectLoginSuccess() {
    val repo = AnkitVerseRepository()
    val err = repo.directLogin("ankit", "123")
    assertNull("Login should succeed with correct username and password", err)
    assertTrue("Repository should be authenticated", repo.isAuthenticated.value)
    assertEquals("ankit", repo.currentUser.value.username)
  }

  @Test
  fun testCustomDirectLoginFailure() {
    val repo = AnkitVerseRepository()
    val nonExistentErr = repo.directLogin("ghost_user_99", "123")
    assertNotNull(nonExistentErr)

    val wrongPasswordErr = repo.directLogin("ankit", "wrong_pass")
    assertNotNull(wrongPasswordErr)
  }

  @Test
  fun testCustomSignUpUniqueUsernameValidation() {
    val repo = AnkitVerseRepository()

    // Duplicated username check
    assertTrue("Username ankit should be taken", repo.isUsernameTaken("ankit"))
    val duplicateErr = repo.signUp("ankit", "Ankit Impostor", 20, "pass123")
    assertNotNull("Duplicate username must be rejected", duplicateErr)

    // Underage check
    val underageErr = repo.signUp("kid_creator", "Kid", 10, "pass123")
    assertNotNull("Age < 13 must be rejected", underageErr)

    // Successful custom account creation
    val success = repo.signUp("creative_star", "Sarah Jenkins", 21, "securePass")
    assertNull("Sign up should succeed for unique user", success)
    assertEquals("creative_star", repo.currentUser.value.username)
    assertEquals("Sarah Jenkins", repo.currentUser.value.name)
    assertEquals(21, repo.currentUser.value.age)
    assertTrue("User should be authenticated", repo.isAuthenticated.value)
  }
}
