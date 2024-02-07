package com.iesam.rememora.app.extensions

import android.content.Context
import java.io.File

fun Context.getFileFromAssets(subfolder: String, fileName: String): File = File(dataDir, fileName)
    .also {
        if (!it.exists()) {
            it.outputStream().use { cache ->
                assets.open("$subfolder/$fileName").use { inputStream ->
                    inputStream.copyTo(cache)
                }
            }
        }
    }