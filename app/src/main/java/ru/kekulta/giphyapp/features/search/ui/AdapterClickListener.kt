package ru.kekulta.giphyapp.features.search.ui

interface AdapterClickListener {
    fun onClick(adapterPosition: Int)
    fun onLikeClick(adapterPosition: Int)
}