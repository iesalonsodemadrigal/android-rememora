package com.iesam.rememora.core.account.di

import com.iesam.rememora.core.account.data.Firebase
import com.iesam.rememora.core.account.domain.AccountRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AccountModule {
    @Binds
    abstract fun bindFirebase(firebase: Firebase): AccountRepository
}