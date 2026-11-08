package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AnkitVerseRepository
import com.example.model.CreatorEarnings
import com.example.ui.effects.RainbowTouchContainer
import com.example.ui.glass.iosGlass
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.ObsidianDark

@Composable
fun CreatorMonetizationScreen(
  repository: AnkitVerseRepository,
  earnings: CreatorEarnings,
  onBack: () -> Unit
) {
  var showPayoutModal by remember { mutableStateOf(false) }
  var payoutMethod by remember { mutableStateOf("Direct Bank (IBAN / UPI)") }
  var payoutAmountInput by remember { mutableStateOf("1000") }
  var payoutSuccessMessage by remember { mutableStateOf<String?>(null) }

  val progressFraction = (earnings.totalViews.toFloat() / earnings.threshold).coerceAtMost(1f)

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(ObsidianDark)
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 100.dp)
    ) {
      // Header Bar
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .iosGlass(RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Creator Monetization",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(Brush.horizontalGradient(listOf(NeonAmber, NeonGreen)))
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text("500k Program", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
          }
        }
      }

      // Celebratory Unlocked Status Banner
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(
              Brush.verticalGradient(
                listOf(Color(0xFF381F00), Color(0xFF1E1102), Color(0xFF140D04))
              )
            )
            .border(1.5.dp, NeonAmber.copy(alpha = 0.8f), RoundedCornerShape(26.dp))
            .padding(20.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(NeonAmber, Color(0xFFFF9900)))),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text("PROGRAM STATUS", color = NeonAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                  Text("UNLOCKED & ACTIVE 🎉", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 500k Milestone Progress Bar
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Total Views Milestone", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
              Text("524,850 / 500,000 (105%)", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
              progress = { 1f },
              modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
              color = NeonGreen,
              trackColor = Color.White.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "⚡ Unlike YouTube's complex 4,000 hour criteria, AnkitVerse unlocks full monetization automatically at 500k total views!",
              color = Color.White.copy(alpha = 0.85f),
              fontSize = 12.sp,
              lineHeight = 17.sp
            )
          }
        }
      }

      // Revenue Highlights Card
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Monthly Revenue
          Box(
            modifier = Modifier
              .weight(1f)
              .height(110.dp)
              .iosGlass(RoundedCornerShape(20.dp))
              .padding(14.dp)
          ) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("MONTHLY EST.", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
              }
              Text("$${earnings.monthlyRevenue}", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
              Text("+24.5% vs last month", color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
          }

          // Effective RPM
          Box(
            modifier = Modifier
              .weight(1f)
              .height(110.dp)
              .iosGlass(RoundedCornerShape(20.dp))
              .padding(14.dp)
          ) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("CREATOR RPM", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Icon(Icons.Default.Speed, contentDescription = null, tint = NeonBlue, modifier = Modifier.size(16.dp))
              }
              Text("$${earnings.rpm}", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
              Text("per 1,000 video views", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
            }
          }
        }
      }

      // Available Balance & Instant Cashout Banner
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .iosGlass(RoundedCornerShape(22.dp))
            .padding(18.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text("AVAILABLE FOR PAYOUT", color = Color.White.copy(alpha = 0.55f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text("$${earnings.availableBalance}", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 26.sp)
              }

              // Instant Payout Button with Rainbow Smoke Particle Touch
              RainbowTouchContainer(
                modifier = Modifier
                  .clip(RoundedCornerShape(14.dp))
                  .background(Brush.horizontalGradient(listOf(NeonGreen, NeonBlue)))
                  .padding(horizontal = 16.dp, vertical = 10.dp),
                onClick = { showPayoutModal = true }
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Payments, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Instant Cashout", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                }
              }
            }

            payoutSuccessMessage?.let {
              Spacer(modifier = Modifier.height(10.dp))
              Text(it, color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      }

      // Revenue Breakdown
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .iosGlass(RoundedCornerShape(22.dp))
            .padding(18.dp)
        ) {
          Text("Revenue Streams Breakdown", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
          Spacer(modifier = Modifier.height(14.dp))

          RevenueStreamRow(title = "Reels & Feed Ad Split (65% share)", amount = "$2,900.00", percentage = 60, color = NeonPink)
          RevenueStreamRow(title = "Brand Collabs & CapCut Templates", amount = "$950.00", percentage = 20, color = NeonPurple)
          RevenueStreamRow(title = "Creator Badges & Fan Tipping", amount = "$640.00", percentage = 13, color = NeonAmber)
          RevenueStreamRow(title = "Audio Track Royalties", amount = "$360.20", percentage = 7, color = NeonBlue)
        }
      }

      // Payout History Ledger
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .iosGlass(RoundedCornerShape(22.dp))
            .padding(18.dp)
        ) {
          Text("Payout History", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
          Spacer(modifier = Modifier.height(12.dp))

          earnings.payoutHistory.forEach { payout ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.AccountBalance, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(payout.method, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                  Text(payout.date, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                }
              }

              Column(horizontalAlignment = Alignment.End) {
                Text("$${payout.amount}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(payout.status, color = NeonGreen, fontSize = 11.sp)
              }
            }
          }
        }
      }
    }

    // Cashout / Payout Modal
    if (showPayoutModal) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = 0.8f))
          .padding(20.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF161928))
            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
            .padding(22.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Request Instant Payout", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            IconButton(onClick = { showPayoutModal = false }) {
              Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text("Select Destination:", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
          Spacer(modifier = Modifier.height(6.dp))

          listOf("Direct Bank (IBAN / ACH)", "UPI / Instant Pay", "PayPal / Apple Cash").forEach { method ->
            val isSelected = payoutMethod == method
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) Color(0x3310B981) else Color.White.copy(alpha = 0.08f))
                .border(1.dp, if (isSelected) NeonGreen else Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                .clickable { payoutMethod = method }
                .padding(12.dp)
            ) {
              Text(method, color = if (isSelected) NeonGreen else Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = payoutAmountInput,
            onValueChange = { payoutAmountInput = it },
            label = { Text("Amount ($ USD)") },
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedBorderColor = NeonGreen
            ),
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(20.dp))

          RainbowTouchContainer(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Brush.horizontalGradient(listOf(NeonGreen, NeonBlue)))
              .padding(vertical = 12.dp),
            onClick = {
              val amt = payoutAmountInput.toDoubleOrNull() ?: 100.0
              repository.requestPayout(amt, payoutMethod)
              payoutSuccessMessage = "✅ Successfully initiated $${amt} payout to $payoutMethod!"
              showPayoutModal = false
            }
          ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
              Text("Confirm & Withdraw", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
          }
        }
      }
    }
  }
}

@Composable
fun RevenueStreamRow(title: String, amount: String, percentage: Int, color: Color) {
  Column(modifier = Modifier.padding(vertical = 6.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(title, color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
      Text(amount, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
    Spacer(modifier = Modifier.height(4.dp))
    LinearProgressIndicator(
      progress = { percentage / 100f },
      modifier = Modifier
        .fillMaxWidth()
        .height(6.dp)
        .clip(RoundedCornerShape(3.dp)),
      color = color,
      trackColor = Color.White.copy(alpha = 0.15f)
    )
  }
}
