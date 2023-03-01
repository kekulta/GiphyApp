package ru.kekulta.giphyapp.features.list.domain.api

import ru.kekulta.giphyapp.shared.data.models.Gif

interface GifInteractor {
    fun searchGifs(query: String, consumer: GifConsumer)

    interface GifConsumer {
        fun consume(foundGifs: List<Gif>)
    }
}