package ru.kekulta.giphyapp.features.search.list.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kekulta.giphyapp.features.search.list.data.dto.GifSearchResponseDto

interface GiphyApi {
    @GET("/v1/gifs/search")
    suspend fun searchGif(
        @Query("q") query: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 25,
        @Query("api_key") key: String = "NJhjiU6444yPb5Mwk2KAj5Do8iCVvac3",
    ): Response<GifSearchResponseDto>
}