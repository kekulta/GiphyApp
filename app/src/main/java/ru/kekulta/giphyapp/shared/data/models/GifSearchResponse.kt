package ru.kekulta.giphyapp.shared.data.models

import ru.kekulta.giphyapp.features.search.list.data.dto.Response


data class GifSearchResponse(val gifList: List<Gif>, val pagination: Pagination) :
    Response()