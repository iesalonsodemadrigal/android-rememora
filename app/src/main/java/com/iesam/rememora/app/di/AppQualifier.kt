package com.iesam.rememora.app.di

import javax.inject.Qualifier

interface AppQualifier {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Mock

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Production
}