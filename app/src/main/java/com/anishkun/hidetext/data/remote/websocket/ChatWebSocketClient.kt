package com.anishkun.hidetext.data.remote.websocket

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatWebSocketClient @Inject constructor(
    private val client: OkHttpClient
) {
    private var webSocket: WebSocket? = null
    
    private val _incomingMessages = MutableSharedFlow<String>()
    val incomingMessages: SharedFlow<String> = _incomingMessages.asSharedFlow()

    fun connect(myPhoneNumber: String) {
        if (webSocket != null) return // Already connected
        
        // Connect to local Ktor backend
        val request = Request.Builder().url("ws://10.0.2.2:8080/chat?phone=$myPhoneNumber").build()
        
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Timber.d("WebSocket Connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Timber.d("WebSocket Message Received: $text")
                _incomingMessages.tryEmit(text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Timber.d("WebSocket Closed: $reason")
                this@ChatWebSocketClient.webSocket = null
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Timber.e(t, "WebSocket Failure")
                this@ChatWebSocketClient.webSocket = null
                // TODO: Implement reconnection logic
            }
        })
    }

    fun sendMessage(messageContent: String) {
        webSocket?.send(messageContent) ?: Timber.w("WebSocket is not connected")
    }

    fun disconnect() {
        webSocket?.close(1000, "Client disconnected")
        webSocket = null
    }
}
