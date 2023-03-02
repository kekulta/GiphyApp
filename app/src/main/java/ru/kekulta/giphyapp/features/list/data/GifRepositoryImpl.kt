package ru.kekulta.giphyapp.features.list.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kekulta.giphyapp.features.list.data.dto.GifSearchRequest
import ru.kekulta.giphyapp.features.list.data.dto.GifSearchResponseDto
import ru.kekulta.giphyapp.features.list.domain.api.GifRepository
import ru.kekulta.giphyapp.shared.data.models.Gif
import ru.kekulta.giphyapp.shared.data.models.GifSearchResponse
import ru.kekulta.giphyapp.shared.data.models.Pagination
import ru.kekulta.giphyapp.shared.data.models.Resource
import ru.kekulta.giphyapp.shared.utils.HTTTPCodes


class GifRepositoryImpl(private val networkClient: NetworkClient) : GifRepository {
    override suspend fun searchGifs(request: GifSearchRequest): Resource<GifSearchResponse> {

        var result: GifSearchResponse? = null
        withContext(Dispatchers.IO) {
            val response = networkClient.doRequest(request)
            if (response.resultCode == HTTTPCodes.OK && response is GifSearchResponseDto) {
                val data = response.data.map { gifDto ->

                    val original = gifDto.images[ORIGINAL]
                    val preview = gifDto.images[PREVIEW]
                    val downsized = gifDto.images[DOWNSIZED]

                    val id = gifDto.id
                    val width = original?.width?.toIntOrNull() ?: preview?.width?.toIntOrNull()
                    ?: DEFAULT_WIDTH
                    val height = original?.height?.toIntOrNull() ?: preview?.height?.toIntOrNull()
                    ?: DEFAULT_HEIGHT

                    val urlPreview = preview?.url ?: downsized?.url ?: original?.url
                    val urlDownsized = downsized?.url ?: original?.url
                    val urlOriginal = original?.url

                    Gif(
                        id = id,
                        width = width,
                        height = height,
                        urlPreview = urlPreview,
                        urlOriginal = urlOriginal,
                        urlDownsized = urlDownsized
                    )
                }
                val pagination = Pagination(
                    response.pagination.offset,
                    response.pagination.totalCount,
                    response.pagination.count
                )
                result = GifSearchResponse(data, pagination)
            }
        }
        result.let {
            return if (it != null) {
                Resource.Success(it)
            } else {
                Resource.Error("Unknown error")
            }
        }
    }

    companion object {
        const val LOG_TAG = "GifRepositoryImpl"
        private const val ORIGINAL = "original"
        private const val PREVIEW = "preview_gif"
        private const val DOWNSIZED = "downsized"
        private const val DEFAULT_WIDTH = 150
        private const val DEFAULT_HEIGHT = 100

    }
}