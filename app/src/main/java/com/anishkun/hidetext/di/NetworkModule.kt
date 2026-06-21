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
    fun provideRetrofit(client: OkHttpClient): retrofit2.Retrofit {
        return retrofit2.Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Android Emulator localhost
            .client(client)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideHideTextApi(retrofit: retrofit2.Retrofit): com.anishkun.hidetext.data.remote.api.HideTextApi {
        return retrofit.create(com.anishkun.hidetext.data.remote.api.HideTextApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatWebSocketClient(client: OkHttpClient): ChatWebSocketClient {
        return ChatWebSocketClient(client)
    }
}
