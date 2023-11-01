package com.example.playlistmaker.data.storage.external_storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.domain.storage.external_storage.ExternalStorageRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class ExternalStorageRepoImpl(context: Context) : ExternalStorageRepo<Uri> {
    private val contentResolver = context.contentResolver
    private val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_maker")

    init {
        if (!filePath.exists()) filePath.mkdirs()
    }

    override fun getByName(name: String): Flow<Uri?> = flow {
        val file = try {
            File(filePath, name)
        } catch (_: NullPointerException) {
            null
        }
        emit(file?.toUri())
    }

    override fun store(item: Uri): String {
        val file = File(filePath, generateName())
        if (file.exists()) return store(item)

        val inputStream = contentResolver.openInputStream(item)
        val outputStream = FileOutputStream(file)

        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.name
    }

    private fun generateName(): String = buildString {
        append(Random.nextInt())
        append("_image.jpg")
    }

}