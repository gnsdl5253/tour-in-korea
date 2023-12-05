package com.hoon.tourinkorea.ui.download

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.net.URL

class ImageDownloadWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val imageUrl = inputData.getString("imageUrls") ?: ""
        val position = inputData.getInt("position", 0)
        if (imageUrl.isEmpty()) {
            return Result.failure()
        }

        val uri = getFileUri(
            fileName = "test",
            url = imageUrl.split(",")[position],
            context = context
        )
        return if (uri != null) {
            Result.success(workDataOf("fileUrl" to uri.toString()))
        } else {
            Result.failure()
        }
    }

    private fun getFileUri(fileName: String, url: String, context: Context): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageToMediaStoreQ(fileName, url, context)
        } else {
            saveImageToMediaStoreLegacy(fileName, url, context)
        }
    }

    private fun saveImageToMediaStoreQ(fileName: String, url: String, context: Context): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                "${Environment.DIRECTORY_PICTURES}/tour"
            )
        }

        val resolver = context.contentResolver
        return resolver.insert(
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
            contentValues
        )?.let { uri ->
            resolver.openOutputStream(uri)?.use { output ->
                URL(url).openStream().use { input ->
                    input.copyTo(output)
                }
            }
            uri
        }
    }

    private fun saveImageToMediaStoreLegacy(fileName: String, url: String, context: Context): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        }

        val resolver = context.contentResolver
        return resolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )?.let { uri ->
            resolver.openOutputStream(uri)?.use { output ->
                URL(url).openStream().use { input ->
                    input.copyTo(output)
                }
            }
            uri
        }
    }
}