package ru.kekulta.giphyapp.shared.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Gif(
    val id: String,
    val width: Int,
    val height: Int,
    val urlPreview: String?,
    val urlOriginal: String?,
    val urlDownsized: String?,
    val user: User?,
    val title: String?,
    val contentDescription: String?
) : Parcelable
