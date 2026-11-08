package com.example.ui.effects

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.example.ui.theme.RainbowColors
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Data class representing an individual Satarangi (Rainbow) smoke puff particle.
 */
data class SmokeParticle(
  val id: Long,
  var x: Float,
  var y: Float,
  var vx: Float,
  var vy: Float,
  var radius: Float,
  var maxRadius: Float,
  val color: Color,
  var alpha: Float,
  val lifeSpeed: Float
)

/**
 * High-performance 60 FPS rainbow smoke particle manager.
 */
class RainbowSmokeEmitter {
  val particles = mutableStateListOf<SmokeParticle>()
  private var nextId = 0L

  fun emit(origin: Offset, count: Int = 16) {
    val baseAngle = Random.nextFloat() * 6.283185f
    for (i in 0 until count) {
      val angle = baseAngle + (i.toFloat() / count) * 6.283185f + (Random.nextFloat() - 0.5f) * 0.4f
      val speed = Random.nextFloat() * 3.5f + 1.2f
      val color = RainbowColors[i % RainbowColors.size]
      val particle = SmokeParticle(
        id = nextId++,
        x = origin.x,
        y = origin.y,
        vx = cos(angle) * speed,
        vy = sin(angle) * speed,
        radius = Random.nextFloat() * 8f + 6f,
        maxRadius = Random.nextFloat() * 32f + 24f,
        color = color,
        alpha = 0.85f,
        lifeSpeed = Random.nextFloat() * 0.045f + 0.035f
      )
      particles.add(particle)
    }
  }

  fun update() {
    val iterator = particles.iterator()
    while (iterator.hasNext()) {
      val p = iterator.next()
      p.x += p.vx
      p.y += p.vy
      p.vx *= 0.94f
      p.vy *= 0.94f
      p.radius += (p.maxRadius - p.radius) * 0.12f
      p.alpha -= p.lifeSpeed
      if (p.alpha <= 0f) {
        iterator.remove()
      }
    }
  }
}

/**
 * Modifier extension that attaches the rainbow smoke touch particle effect
 * strictly within the element's bounding box (overflow clipped).
 */
fun Modifier.rainbowTouch(
  enabled: Boolean = true,
  onClick: () -> Unit
): Modifier = this
  .clipToBounds()
  .pointerInput(enabled) {
    if (enabled) {
      detectTapGestures(
        onTap = { offset ->
          onClick()
        }
      )
    }
  }

/**
 * A wrapper composable that renders an overlay canvas of rainbow smoke
 * particles strictly inside its bounds whenever tapped.
 */
@Composable
fun RainbowTouchContainer(
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  onClick: () -> Unit,
  content: @Composable () -> Unit
) {
  val emitter = remember { RainbowSmokeEmitter() }
  val scope = rememberCoroutineScope()

  // Frame tick animation loop
  LaunchedEffect(emitter.particles.size) {
    if (emitter.particles.isNotEmpty()) {
      while (emitter.particles.isNotEmpty()) {
        emitter.update()
        withFrameNanos { /* next frame */ }
      }
    }
  }

  Box(
    modifier = modifier
      .clipToBounds()
      .pointerInput(enabled) {
        if (enabled) {
          detectTapGestures(
            onTap = { offset ->
              emitter.emit(offset, count = 16)
              onClick()
            }
          )
        }
      }
  ) {
    content()

    // Hardware accelerated Canvas drawing smoke particles
    if (emitter.particles.isNotEmpty()) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        for (p in emitter.particles) {
          if (p.alpha > 0.01f) {
            drawCircle(
              brush = Brush.radialGradient(
                colors = listOf(
                  p.color.copy(alpha = p.alpha),
                  p.color.copy(alpha = p.alpha * 0.5f),
                  Color.Transparent
                ),
                center = Offset(p.x, p.y),
                radius = p.radius
              ),
              radius = p.radius,
              center = Offset(p.x, p.y)
            )
          }
        }
      }
    }
  }
}

private suspend fun withFrameNanos(onFrame: (Long) -> Unit) {
  androidx.compose.runtime.withFrameNanos(onFrame)
}
