package ru.kekulta.giphyapp.features.search.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kekulta.giphyapp.features.search.data.dto.GifSearchRequest
import ru.kekulta.giphyapp.features.search.data.dto.GifSearchResponseDto
import ru.kekulta.giphyapp.features.search.domain.api.GifRepository
import ru.kekulta.giphyapp.features.search.domain.presentation.GifListViewModel
import ru.kekulta.giphyapp.features.pager.domain.models.PaginationState


import ru.kekulta.giphyapp.shared.data.models.*
import ru.kekulta.giphyapp.shared.utils.HTTTPCodes


class GifRepositoryImpl(private val networkClient: NetworkClient) :
    GifRepository {
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

                    val user = gifDto.userDto.let {
                        if (it == null) {
                            null
                        } else {
                            User(it.avatar, it.name)
                        }
                    }
                    Gif(
                        id = id,
                        width = width,
                        height = height,
                        urlPreview = urlPreview,
                        urlOriginal = urlOriginal,
                        urlDownsized = urlDownsized,
                        user = user,
                        title = gifDto.title,
                        contentDescription = gifDto.contentDescription,
                    )
                }

                val currentPage =
                    (response.pagination.count + response.pagination.offset) / PaginationState.ITEMS_ON_PAGE +
                            if ((response.pagination.count + response.pagination.offset) % PaginationState.ITEMS_ON_PAGE > 0) 1 else 0
                val pagesTotal =
                    (response.pagination.totalCount) / PaginationState.ITEMS_ON_PAGE +
                            if ((response.pagination.totalCount) % PaginationState.ITEMS_ON_PAGE > 0) 1 else 0


                val pagination = Pagination(
                    currentPage, pagesTotal
                )

                Log.d(
                    GifListViewModel.LOG_TAG, """
                        offset: ${response.pagination.offset}
                        count: ${response.pagination.count}
                        countTotal: ${response.pagination.totalCount}
                        currentPage: $currentPage
                        pagesTotal: $pagesTotal
                    """.trimIndent()
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