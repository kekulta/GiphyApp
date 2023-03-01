package ru.kekulta.giphyapp.features.list.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kekulta.giphyapp.shared.data.dto.GifSearchResponse

interface GiphyApi {
    @GET("/v1/gifs/search")
    suspend fun searchGif(
        @Query("q") query: String,
        @Query("api_key") key: String = "NJhjiU6444yPb5Mwk2KAj5Do8iCVvac3",
    ): Response<GifSearchResponse>
}