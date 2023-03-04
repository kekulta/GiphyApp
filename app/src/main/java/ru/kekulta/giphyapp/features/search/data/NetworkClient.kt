package ru.kekulta.giphyapp.features.search.data

import ru.kekulta.giphyapp.features.search.data.dto.Response


interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}