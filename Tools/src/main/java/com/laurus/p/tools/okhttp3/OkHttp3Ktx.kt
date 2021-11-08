package com.laurus.p.tools.okhttp3

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.joda.time.DateTime

/**
 * Converts content which is located at uri path to [MultipartBody.Part].
 * @param applicationContext Application context.
 * @param fieldName Field name in multipart/form-data encoding.
 */
fun Uri.toMultipartBodyPart(
    applicationContext: Context,
    fieldName: String,
    generateFileName: Boolean = true
): MultipartBody.Part? {
    return try {
        val contentResolver = applicationContext.contentResolver

        val mediaType = contentResolver
            .getType(this)
            ?.toMediaTypeOrNull()
            ?: return null

        val contentByteArray = contentResolver.openInputStream(this)
            ?.readBytes() ?: return null

        val fileName = if(generateFileName) {
            DateTime().toString("dd-MM-YYYY-HH-mm-ss")
        } else null

        MultipartBody.Part.createFormData(
            fieldName,
            fileName,
            contentByteArray.toRequestBody(mediaType)
        )
    } catch (exception: Exception) {
        null
    }
}