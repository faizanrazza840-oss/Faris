
package com.faris.assistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private const val FARIS_URL =
    "https://broad-art-b578faris-ai-bridge.faizanrazza840.workers.dev"

private val FarisBackground = Color(0xFF080D1B)
private val FarisCyan = Color(0xFF53E7FF)
private val FarisClient = OkHttpClient.Builder()
    .connectTimeout(20, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .build()

private data class ChatMessage(
    val text: String,
    val fromUser: Boolean
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FarisChat()
            }
        }
    }
}

@Composable
private fun FarisChat() {
    var message by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                "Hello! Main FARIS hoon. Mujhe message bhejo.",
                false
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FarisBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        Text(
            "F A R I S",
            color = FarisCyan,
            fontSize = 28.sp
        )
        Text(
            "PERSONAL AI ASSISTANT",
            color = Color.LightGray,
            fontSize = 11.sp
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (item.fromUser)
                        Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        color = if (item.fromUser)
                            Color(0xFF123A52)
                        else Color(0xFF182238),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            item.text,
                            color = Color.White,
                            modifier = Modifier
                                .widthIn(max = 290.dp)
                                .padding(12.dp)
                        )
                    }
                }
            }

            if (loading) {
                item {
                    Text(
                        "FARIS soch raha hai...",
                        color = FarisCyan
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f),
                enabled = !loading,
                placeholder = { Text("Message likho...") },
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        // Send button use karo.
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = FarisCyan,
                    unfocusedBorderColor = Color.Gray,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.LightGray
                )
            )

            Button(
                enabled = message.isNotBlank() && !loading,
                onClick = {
                    val question = message.trim()
                    message = ""
                    messages.add(ChatMessage(question, true))
                    loading = true

                    Thread {
                        val answer = try {
                            val json = JSONObject()
                                .put("message", question)

                            val body = json.toString().toRequestBody(
                                "application/json; charset=utf-8"
                                    .toMediaType()
                            )

                            val request = Request.Builder()
                                .url(FARIS_URL)
                                .post(body)
                                .build()

                            FarisClient.newCall(request).execute().use {
                                response ->
                                val text = response.body?.string().orEmpty()
                                val data = try {
                                    JSONObject(text)
                                } catch (_: Exception) {
                                    JSONObject()
                                }

                                if (response.isSuccessful) {
                                    data.optString(
                                        "reply",
                                        "AI se khaali jawab mila."
                                    )
                                } else {
                                    "AI error (${response.code}): " +
                                        data.optString(
                                            "error",
                                            "Worker ya AI service check karo."
                                        )
                                }
                            }
                        } catch (e: Exception) {
                            "Connection error: ${e.localizedMessage ?: "Internet check karo"}"
                        }

                        (android.os.Looper.getMainLooper()).let { looper ->
                            android.os.Handler(looper).post {
                                messages.add(ChatMessage(answer, false))
                                loading = false
                            }
                        }
                    }.start()
                }
            ) {
                Text("Send")
            }
        }

        Spacer(Modifier.height(8.dp))
        Text(
            "Internet aur AI service zaroori hain.",
            color = Color.Gray,
            fontSize = 11.sp
        )
    }
}
