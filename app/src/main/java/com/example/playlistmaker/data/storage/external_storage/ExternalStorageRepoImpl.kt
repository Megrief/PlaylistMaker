package com.example.playlistmaker.data.storage.external_storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.domain.storage.repos.ExternalStorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreItemUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class ExternalStorageRepoImpl(
    context: Context,
    private val storeIdUseCase: StoreItemUseCase<Long>
) : ExternalStorageManagerRepo<Uri> {
    private val contentResolver = context.contentResolver
    private val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_maker")
    private val endOfName = "_image.jpg"
    private var tempId = 0L

    init {
        if (!filePath.exists()) filePath.mkdirs()
    }

    override fun getById(id: Long): Flow<Uri?> = flow {
        val file = try {
            File(filePath, provideName(id))
        } catch (_: NullPointerException) {
            null
        }
        emit(file?.toUri())
    }

    override fun store(item: Uri): Boolean {
        tempId = Random.nextLong(1, Long.MAX_VALUE)
        val file = File(filePath, provideName(tempId))
        if (file.exists()) return store(item)

        val inputStream = contentResolver.openInputStream(item)
        val outputStream = FileOutputStream(file)

        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return storeIdUseCase.store(tempId)
    }

    private fun provideName(id: Long): String = "$id$endOfName"

}