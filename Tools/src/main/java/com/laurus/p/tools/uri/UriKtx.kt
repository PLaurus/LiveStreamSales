package com.laurus.p.tools.uri

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

/**
 * Converts content which is located at uri path to [MultipartBody.Part].
 * @param applicationContext Application context.
 * @param fieldName Field name in multipart/form-data encoding.
 */
fun Uri.toMultipartBodyPart(
    applicationContext: Context,
    fieldName: String
): MultipartBody.Part? {
    return try {
        val file = toFile()

        applicationContext.contentResolver
            .getType(this)
            ?.toMediaTypeOrNull()
            ?.let { fileMediaType ->
                MultipartBody.Part.createFormData(
                    name = fieldName,
                    filename = file.name,
                    file.asRequestBody(fileMediaType)
                )
            }
    } catch (exception: IllegalArgumentException) {
        null
    }
}