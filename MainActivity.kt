package com.faris.assistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DeepBackground = Color(0xFF030812)
private val Cyan = Color(0xFF53E7FF)
private val Blue = Color(0xFF397BFF)

private enum class AssistantState { IDLE, LISTENING, THINKING, SPEAKING }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FARISHome()
            }
        }
    }
}

@Composable
private fun FARISHome() {
    var expanded by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(AssistantState.IDLE) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DeepBackground
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("F A R I S", color = Cyan, fontSize = 25.sp, fontWeight = FontWeight.Light)
                    Text("PERSONAL AI SYSTEM", color = Color.LightGray, fontSize = 10.sp, letterSpacing = 3.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    HolographicOrb(
                        modifier = Modifier
                            .size(if (expanded) 270.dp else 190.dp)
                            .clickable { expanded = !expanded },
                        active = state != AssistantState.IDLE
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(
                        when (state) {
                            AssistantState.IDLE -> "READY"
                            AssistantState.LISTENING -> "LISTENING"
                            AssistantState.THINKING -> "THINKING"
                            AssistantState.SPEAKING -> "SPEAKING"
                        },
                        color = Cyan,
                        letterSpacing = 4.sp,
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        if (expanded) "Hologram interface preview" else "Tap the core to expand",
                        color = Color(0xFF9CAFC7),
                        fontSize = 13.sp
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StateButton("Listen") { state = AssistantState.LISTENING }
                        StateButton("Think") { state = AssistantState.THINKING }
                        StateButton("Speak") { state = AssistantState.SPEAKING }
                    }
                    Spacer(Modifier.height(10.dp))
                    Button(onClick = {
                        state = AssistantState.IDLE
                        expanded = false
                    }) { Text("Stop / Close") }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "UI prototype only • microphone and AI are not connected",
                        color = Color(0xFF6E8199),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StateButton(label: String, onClick: () -> Unit) {
    Button(onClick = onClick) { Text(label) }
}

@Composable
private fun HolographicOrb(modifier: Modifier = Modifier, active: Boolean) {
    val transition = rememberInfiniteTransition(label = "orb")
    val pulse by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = if (active) 1.08f else 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier) {
        val c = Offset(size.width / 2f, size.height / 2f)
        val base = size.minDimension * 0.32f * pulse
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFBDFBFF), Cyan, Blue, Color(0x00397BFF)),
                center = c,
                radius = base * 1.7f
            ),
            radius = base * 1.7f,
            center = c,
            alpha = 0.9f
        )
        for (i in 0..4) {
            val radius = base * (0.78f + i * 0.22f)
            drawCircle(
                color = if (i % 2 == 0) Cyan else Blue,
                radius = radius,
                center = c,
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = if (i == 0) 2.5f else 1.1f
                ),
                alpha = 0.72f - i * 0.09f
            )
        }
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, Cyan, Color(0xFF1266FF)),
                center = c,
                radius = base * 0.75f
            ),
            radius = base * 0.65f,
            center = c
        )
        // Small orbit points for a lightweight holographic feel.
        val orbit = base * 1.35f
        drawCircle(Cyan, radius = 3.5f, center = Offset(c.x + orbit, c.y - orbit * 0.28f))
        drawCircle(Color.White, radius = 2.5f, center = Offset(c.x - orbit * 0.72f, c.y + orbit * 0.72f))
        drawCircle(Blue, radius = 3f, center = Offset(c.x - orbit * 0.92f, c.y - orbit * 0.1f))
    }
}
