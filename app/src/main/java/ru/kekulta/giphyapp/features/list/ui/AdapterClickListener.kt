package ru.kekulta.giphyapp.features.list.ui

interface AdapterClickListener {
    fun onClick(adapterPosition: Int)
    fun onLikeClick(adapterPosition: Int)
}