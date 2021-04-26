package com.ifyezedev.coslog.core.common.usecase

import android.content.Intent
import com.ifyezedev.coslog.core.builders.buildIntent
import com.ifyezedev.coslog.core.common.usecase.core.LambdaUseCase
import com.ifyezedev.coslog.core.common.usecase.core.UseCase.None

class OpenAndroidImageGallery : LambdaUseCase<Intent, Int, None>() {
    private val intentChooserTitle = "Select Picture"

    override suspend fun run(params: (Intent, Int) -> Unit): None {
        val intent = buildIntent {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
            putExtra("return-data", true);
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        params(Intent.createChooser(intent, intentChooserTitle), 0)
        return None()
    }
}
