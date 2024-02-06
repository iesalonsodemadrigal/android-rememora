package com.iesam.rememora.core.account.di

import com.iesam.rememora.app.di.AppQualifier
import com.iesam.rememora.core.account.data.AccountDataRepository
import com.iesam.rememora.core.account.data.AccountMockDataRepository
import com.iesam.rememora.core.account.domain.AccountRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AccountModule {

    @Binds
    @AppQualifier.Production
    abstract fun bindFirebaseDataRepository(accountDataRepository: AccountDataRepository): AccountRepository

    @Binds
    @AppQualifier.Mock
    abstract fun bindMockDataRepository(accountMock: AccountMockDataRepository): AccountRepository
}