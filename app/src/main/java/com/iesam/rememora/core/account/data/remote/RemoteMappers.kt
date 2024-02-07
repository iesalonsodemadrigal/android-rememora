package com.iesam.rememora.core.account.data.remote

import com.google.firebase.auth.FirebaseUser
import com.iesam.rememora.core.account.domain.Account

fun FirebaseUser.toModel() = Account(this.uid, this.displayName, this.email, this.photoUrl)
