package com.iesam.rememora.core.account.di

import com.iesam.rememora.BuildConfig
import com.iesam.rememora.core.account.data.AccountDataRepository
import com.iesam.rememora.core.account.data.AccountMockDataRepository
import com.iesam.rememora.core.account.domain.AccountRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AccountModule {

    @Provides
    fun provideAccountRepository(
        accountDataRepository: AccountDataRepository,
        mockDataRepository: AccountMockDataRepository
    ): AccountRepository {
        return if (BuildConfig.IS_LOCAL_ENV) {
            mockDataRepository
        } else {
            accountDataRepository
        }
    }
}