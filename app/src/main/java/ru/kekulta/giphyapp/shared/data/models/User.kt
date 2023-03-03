package ru.kekulta.giphyapp.shared.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val avatar: String,
    val name: String,
) : Parcelable