package ru.kekulta.giphyapp.features.search.data.dto

sealed class GifSearchRequest {
    data class QueryRequest(val query: String, val offset: Int) : GifSearchRequest()
    data class IdsRequest(val ids: List<String>, val offset: Int) : GifSearchRequest()
    data class LikedRequest(val page: Int) : GifSearchRequest()
}