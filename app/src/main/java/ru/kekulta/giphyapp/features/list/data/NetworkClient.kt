package ru.kekulta.giphyapp.features.list.data

import ru.kekulta.giphyapp.features.list.data.dto.Response


interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}