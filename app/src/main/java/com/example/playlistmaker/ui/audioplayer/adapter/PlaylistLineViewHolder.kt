package com.example.playlistmaker.ui.audioplayer.adapter

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.app.di.GET_PHOTO_BY_ID_USE_CASE
import com.example.playlistmaker.databinding.PlaylistLineBinding
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.use_cases.GetItemByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin

class PlaylistLineViewHolder(private val binding: PlaylistLineBinding) : RecyclerView.ViewHolder(binding.root) {

    private val getPhotoByIdUseCase: GetItemByIdUseCase<Uri> = getKoin().get(named(GET_PHOTO_BY_ID_USE_CASE))

    fun bind(playlist: Playlist) {
        with(binding) {
            MainScope().launch(Dispatchers.IO) {
                val photo = getPhotoByIdUseCase.get(playlist.photoId).single()
                withContext(Dispatchers.Main) {
                    val cornerSizeInPx = root.resources.getDimensionPixelSize(R.dimen.small)
                    Glide.with(root)
                        .load(photo)
                        .placeholder(R.drawable.placeholder)
                        .transform(CenterCrop(), RoundedCorners(cornerSizeInPx))
                        .into(playlistPhoto)
                    playlistName.text = playlist.name
                    val size = playlist.trackIdsList.size.toString()
                    val text = size + " " + getCorrect(size)
                    playlistSize.text = text
                }
            }
        }
    }

    private fun getCorrect(num: String): String {
        return when {
            num.last() == '1'
                    && if (num.length > 1) num[num.lastIndex - 1] != '1' else true -> "трек"
            num.last() in '2'..'4'
                    && if (num.length > 1) num[num.lastIndex - 1] != '1' else true -> "трека"
            else -> "треков"
        }
    }

}