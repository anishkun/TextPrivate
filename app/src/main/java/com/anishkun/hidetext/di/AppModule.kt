package com.anishkun.hidetext.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Singleton
    fun provideApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): kotlinx.coroutines.CoroutineScope {
        return kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.SupervisorJob() + defaultDispatcher)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        @dagger.hilt.android.qualifiers.ApplicationContext context: android.content.Context
    ): com.anishkun.hidetext.domain.repository.UserPreferencesRepository {
        return com.anishkun.hidetext.data.repository.UserPreferencesRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideContactRepository(
        database: com.anishkun.hidetext.data.local.ChatDatabase
    ): com.anishkun.hidetext.domain.repository.ContactRepository {
        return com.anishkun.hidetext.data.repository.ContactRepositoryImpl(database.contactDao)
    }
}
