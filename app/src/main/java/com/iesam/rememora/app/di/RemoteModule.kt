package com.iesam.rememora.app.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Singleton
    @Provides
    fun provideFirebaseDataBase() : FirebaseDatabase = FirebaseDatabase.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseStorage() : FirebaseStorage = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideAuthUI(): AuthUI = AuthUI.getInstance()
}