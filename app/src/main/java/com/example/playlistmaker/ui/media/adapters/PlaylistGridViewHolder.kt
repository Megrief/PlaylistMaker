package com.example.playlistmaker.ui.media.adapters

import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.app.di.GET_PHOTO_BY_ID_USE_CASE
import com.example.playlistmaker.databinding.PlaylistCardBinding
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.use_cases.GetItemByIdUseCase
import com.example.playlistmaker.utils.getCorrectTracks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin

class PlaylistGridViewHolder(private val binding: PlaylistCardBinding) : RecyclerView.ViewHolder(binding.root) {

    private val getPhotoByIdUseCase: GetItemByIdUseCase<Uri> = getKoin().get(named(GET_PHOTO_BY_ID_USE_CASE))

    fun bind(playlist: Playlist) {
        MainScope().launch(Dispatchers.IO) {
            with(binding) {
                val photo = getPhotoByIdUseCase.get(playlist.photoId).single()
                Log.wtf("AAA", "Photo not null == ${ photo != null }")
                withContext(Dispatchers.Main) {
                    val cornerSizeInPx = root.resources.getDimensionPixelSize(R.dimen.small)
                    Glide.with(root)
                        .load(photo)
                        .placeholder(R.drawable.placeholder)
                        .transform(CenterCrop(), RoundedCorners(cornerSizeInPx))
                        .into(playlistPhoto)
                    playlistName.text = playlist.name
                    val size = playlist.trackIdsList.size.toString()
                    val text = size + " " + getCorrectTracks(size)
                    playlistSize.text = text
                }
            }
        }

    }

}