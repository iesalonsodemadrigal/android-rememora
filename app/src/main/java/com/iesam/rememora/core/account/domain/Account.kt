package com.iesam.rememora.core.account.domain

import android.net.Uri

data class Account(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val urlImageProfile: Uri? = null
)