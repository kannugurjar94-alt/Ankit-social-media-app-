package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AnkitVerseRepository
import com.example.ui.effects.RainbowTouchContainer
import com.example.ui.glass.IOSGlassButton
import com.example.ui.glass.iosGlass
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.ObsidianDark

@Composable
fun AuthScreen(
  repository: AnkitVerseRepository,
  onLoginSuccess: () -> Unit
) {
  var isSignUp by remember { mutableStateOf(false) }

  // Sign-In State
  var loginUsername by remember { mutableStateOf("ankit") }
  var loginPassword by remember { mutableStateOf("123") }

  // Sign-Up State
  var signUpUsername by remember { mutableStateOf("") }
  var signUpDisplayName by remember { mutableStateOf("") }
  var signUpAge by remember { mutableStateOf("20") }
  var signUpPassword by remember { mutableStateOf("") }

  var errorMessage by remember { mutableStateOf<String?>(null) }

  // Real-time username availability check for sign-up
  val cleanSignUpUsername = signUpUsername.trim().removePrefix("@").lowercase()
  val isUsernameAvailable by remember(cleanSignUpUsername) {
    derivedStateOf {
      cleanSignUpUsername.length >= 3 && !repository.isUsernameTaken(cleanSignUpUsername)
    }
  }
  val isUsernameTaken by remember(cleanSignUpUsername) {
    derivedStateOf {
      cleanSignUpUsername.length >= 3 && repository.isUsernameTaken(cleanSignUpUsername)
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          listOf(ObsidianDark, Color(0xFF140D26), Color(0xFF0A0713))
        )
      )
      .padding(horizontal = 24.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .iosGlass(RoundedCornerShape(32.dp))
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // App Icon Logo
      Image(
        painter = painterResource(id = R.drawable.app_icon_asset),
        contentDescription = "AnkitVerse",
        modifier = Modifier
          .size(68.dp)
          .clip(RoundedCornerShape(20.dp))
          .border(2.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
      )

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "AnkitVerse",
        color = Color.White,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
      )

      Text(
        text = if (isSignUp) "Custom Account Creation" else "Direct Login System",
        color = Color.White.copy(alpha = 0.65f),
        fontSize = 12.sp
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Tab Switcher (Direct Login vs Sign-Up)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(Color.White.copy(alpha = 0.08f))
          .padding(4.dp)
      ) {
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .then(
              if (!isSignUp) Modifier.background(Brush.horizontalGradient(listOf(NeonPink, NeonPurple)))
              else Modifier.background(Color.Transparent)
            )
            .clickable {
              isSignUp = false
              errorMessage = null
            }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Direct Login",
            color = Color.White,
            fontWeight = if (!isSignUp) FontWeight.Bold else FontWeight.Normal,
            fontSize = 13.sp
          )
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .then(
              if (isSignUp) Modifier.background(Brush.horizontalGradient(listOf(NeonPink, NeonPurple)))
              else Modifier.background(Color.Transparent)
            )
            .clickable {
              isSignUp = true
              errorMessage = null
            }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Sign-Up",
            color = Color.White,
            fontWeight = if (isSignUp) FontWeight.Bold else FontWeight.Normal,
            fontSize = 13.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Error banner
      AnimatedVisibility(
        visible = errorMessage != null,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        errorMessage?.let { err ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0x33FF0055))
              .border(1.dp, NeonPink.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
              .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = NeonPink, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(err, color = Color.White, fontSize = 12.sp)
          }
          Spacer(modifier = Modifier.height(12.dp))
        }
      }

      if (!isSignUp) {
        // --- DIRECT LOGIN SYSTEM ---
        OutlinedTextField(
          value = loginUsername,
          onValueChange = {
            loginUsername = it
            errorMessage = null
          },
          label = { Text("Unique Username") },
          placeholder = { Text("e.g. ankit") },
          leadingIcon = {
            Icon(Icons.Default.AlternateEmail, contentDescription = null, tint = NeonPink)
          },
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = NeonPink,
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedLabelColor = NeonPink,
            unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
          ),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = loginPassword,
          onValueChange = {
            loginPassword = it
            errorMessage = null
          },
          label = { Text("Password") },
          leadingIcon = {
            Icon(Icons.Default.Lock, contentDescription = null, tint = NeonPurple)
          },
          visualTransformation = PasswordVisualTransformation(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = NeonPurple,
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedLabelColor = NeonPurple,
            unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
          ),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Direct Login Button with Satarangi Rainbow touch particle effect
        IOSGlassButton(
          text = "Direct Login",
          icon = Icons.Default.Login,
          onClick = {
            val err = repository.directLogin(loginUsername, loginPassword)
            if (err == null) {
              onLoginSuccess()
            } else {
              errorMessage = err
            }
          },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Fast Demo Account Shortcuts
        Text("Quick Test Profiles:", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("@ankit", "@elena_cinematic", "@neotech").forEach { uname ->
            RainbowTouchContainer(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.08f))
                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                .padding(vertical = 6.dp),
              onClick = {
                loginUsername = uname.removePrefix("@")
                loginPassword = "123"
                val err = repository.directLogin(loginUsername, loginPassword)
                if (err == null) onLoginSuccess() else errorMessage = err
              }
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
              ) {
                Text(uname, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
              }
            }
          }
        }
      } else {
        // --- ACCOUNT CREATION (SIGN-UP) ---
        // 1. Unique Username
        OutlinedTextField(
          value = signUpUsername,
          onValueChange = {
            signUpUsername = it.trim().removePrefix("@")
            errorMessage = null
          },
          label = { Text("Unique Username (Cannot be duplicated)") },
          placeholder = { Text("e.g. john_doe") },
          leadingIcon = {
            Icon(Icons.Default.AlternateEmail, contentDescription = null, tint = NeonPink)
          },
          trailingIcon = {
            if (isUsernameAvailable) {
              Icon(Icons.Default.CheckCircle, contentDescription = "Available", tint = NeonGreen, modifier = Modifier.size(20.dp))
            } else if (isUsernameTaken) {
              Icon(Icons.Default.ErrorOutline, contentDescription = "Taken", tint = NeonPink, modifier = Modifier.size(20.dp))
            }
          },
          supportingText = {
            if (isUsernameAvailable) {
              Text("✓ Username is available", color = NeonGreen, fontSize = 10.sp)
            } else if (isUsernameTaken) {
              Text("✗ Username already taken", color = NeonPink, fontSize = 10.sp)
            } else {
              Text("Globally unique identifier", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
            }
          },
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = if (isUsernameAvailable) NeonGreen else NeonPink,
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedLabelColor = NeonPink,
            unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
          ),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        // 2. Display Name
        OutlinedTextField(
          value = signUpDisplayName,
          onValueChange = {
            signUpDisplayName = it
            errorMessage = null
          },
          label = { Text("Display Name (Visible name)") },
          placeholder = { Text("e.g. John Doe") },
          leadingIcon = {
            Icon(Icons.Default.Badge, contentDescription = null, tint = NeonBlue)
          },
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = NeonBlue,
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedLabelColor = NeonBlue,
            unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
          ),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 3. Age (Numerical Entry)
        OutlinedTextField(
          value = signUpAge,
          onValueChange = {
            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
              signUpAge = it
              errorMessage = null
            }
          },
          label = { Text("Age (Age verification)") },
          placeholder = { Text("e.g. 21") },
          leadingIcon = {
            Icon(Icons.Default.Cake, contentDescription = null, tint = NeonAmber)
          },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = NeonAmber,
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedLabelColor = NeonAmber,
            unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
          ),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 4. Password
        OutlinedTextField(
          value = signUpPassword,
          onValueChange = {
            signUpPassword = it
            errorMessage = null
          },
          label = { Text("Password (Secure)") },
          leadingIcon = {
            Icon(Icons.Default.Lock, contentDescription = null, tint = NeonPurple)
          },
          visualTransformation = PasswordVisualTransformation(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = NeonPurple,
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedLabelColor = NeonPurple,
            unfocusedLabelColor = Color.White.copy(alpha = 0.6f)
          ),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Create Account Button with Satarangi Rainbow touch particle effect
        IOSGlassButton(
          text = "Create Account",
          icon = Icons.Default.PersonAdd,
          onClick = {
            val ageInt = signUpAge.toIntOrNull() ?: 0
            val err = repository.signUp(
              username = signUpUsername,
              displayName = signUpDisplayName,
              age = ageInt,
              password = signUpPassword
            )
            if (err == null) {
              onLoginSuccess()
            } else {
              errorMessage = err
            }
          },
          modifier = Modifier.fillMaxWidth()
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Toggle between Login & Sign-Up
      Row(
        modifier = Modifier.clickable {
          isSignUp = !isSignUp
          errorMessage = null
        },
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = if (isSignUp) "Already have an account? " else "Need a new account? ",
          color = Color.White.copy(alpha = 0.6f),
          fontSize = 12.sp
        )
        Text(
          text = if (isSignUp) "Direct Login" else "Create Unique ID",
          color = NeonPink,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp
        )
      }
    }
  }
}
