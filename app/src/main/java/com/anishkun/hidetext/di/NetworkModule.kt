package com.anishkun.hidetext.di

import com.anishkun.hidetext.data.remote.websocket.ChatWebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .pingInterval(30, TimeUnit.SECONDS) // Keeps WebSocket alive
            .build()
    }

    @Provides
    @Singleton
    fun provideChatWebSocketClient(client: OkHttpClient): ChatWebSocketClient {
        return ChatWebSocketClient(client)
    }
}
