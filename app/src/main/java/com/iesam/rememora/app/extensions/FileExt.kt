package com.iesam.rememora.app.extensions

import android.content.Context
import java.io.File

fun Context.getFileFromAssets(fileName: String): File = File(dataDir, fileName)
    .also {
        if (!it.exists()) {
            it.outputStream().use { cache ->
                assets.open(fileName).use { inputStream ->
                    inputStream.copyTo(cache)
                }
            }
        }
    }