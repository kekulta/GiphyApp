package ru.kekulta.giphyapp.features.list.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kekulta.giphyapp.features.list.data.NetworkClient
import ru.kekulta.giphyapp.features.list.data.dto.GifSearchRequest
import ru.kekulta.giphyapp.features.list.data.dto.Response
import ru.kekulta.giphyapp.shared.utils.HTTTPCodes

class RetrofitNetworkClient : NetworkClient {
    private val giphyBaseUrl = "https://api.giphy.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(giphyBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val giphyService = retrofit.create(GiphyApi::class.java)
    override suspend fun doRequest(dto: Any): Response {
        return if (dto is GifSearchRequest) {
            val resp = giphyService.searchGif(dto.query, dto.offset)
            val body = resp.body() ?: Response()
            body.apply { resultCode = resp.code() }
        } else {
            Response().apply { resultCode = HTTTPCodes.BAD_REQUEST }
        }
    }
}