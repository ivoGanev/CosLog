package com.ifyezedev.coslog.feature.elements.internal.usecase

import android.content.Intent
import com.ifyezedev.coslog.core.builders.buildIntent

class OpenAndroidImageGallery {
    private val intentChooserTitle = "Select Picture"

    fun invoke(startActivityForResult: (Intent, Int) -> Unit) {
        val intent = buildIntent {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
            putExtra("return-data", true);
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(Intent.createChooser(intent, intentChooserTitle), 0)
    }
}