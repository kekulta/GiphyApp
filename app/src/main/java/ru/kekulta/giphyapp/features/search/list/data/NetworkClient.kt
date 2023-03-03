package ru.kekulta.giphyapp.features.search.list.data

import ru.kekulta.giphyapp.features.search.list.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}