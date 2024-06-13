package com.iesam.rememora.ia.di

import com.iesam.rememora.ia.data.IaDataRepository
import com.iesam.rememora.ia.domain.IaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class IaModule {
    @Binds
    abstract fun bindIaDataRepository (iaDataRepository: IaDataRepository) : IaRepository
}