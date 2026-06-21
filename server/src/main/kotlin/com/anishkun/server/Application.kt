package com.anishkun.server

import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.channels.consumeEach
import com.google.gson.Gson

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

// In-memory database for our prototype
// Maps phoneNumber -> publicKey
val userDirectory = ConcurrentHashMap<String, String>()

// Maps phoneNumber -> active WebSocketSession
val activeConnections = ConcurrentHashMap<String, DefaultWebSocketServerSession>()

// Data transfer objects
data class RegisterRequest(val phoneNumber: String, val publicKey: String)
data class KeyResponse(val publicKey: String?)
data class ChatMessage(val receiverPhone: String, val senderPhone: String, val encryptedContent: String, val timestamp: Long)

fun Application.module() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    
    val gson = Gson()

    routing {
        get("/") {
            call.respondText("HideText Backend is running!")
        }

        // Register a user's public key
        post("/register") {
            val request = call.receive<RegisterRequest>()
            userDirectory[request.phoneNumber] = request.publicKey
            call.respond(mapOf("success" to true))
        }

        // Fetch a user's public key
        get("/key/{phoneNumber}") {
            val phone = call.parameters["phoneNumber"]
            val key = phone?.let { userDirectory[it] }
            if (key != null) {
                call.respond(KeyResponse(key))
            } else {
                call.respond(KeyResponse(null))
            }
        }

        // WebSocket for live messaging
        webSocket("/chat") {
            val phoneNumber = call.request.queryParameters["phone"]
            if (phoneNumber == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No phone number provided"))
                return@webSocket
            }

            // Register connection
            activeConnections[phoneNumber] = this

            try {
                // Listen for incoming messages
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        // Parse as ChatMessage
                        val message = gson.fromJson(text, ChatMessage::class.java)
                        
                        // Route to receiver if connected
                        val receiverSession = activeConnections[message.receiverPhone]
                        if (receiverSession != null) {
                            receiverSession.send(Frame.Text(text))
                        } else {
                            // In a full app, we would queue offline messages here
                            println("User ${message.receiverPhone} is offline.")
                        }
                    }
                }
            } catch (e: Exception) {
                println("WebSocket error: ${e.localizedMessage}")
            } finally {
                // Remove connection
                activeConnections.remove(phoneNumber)
            }
        }
    }
}
