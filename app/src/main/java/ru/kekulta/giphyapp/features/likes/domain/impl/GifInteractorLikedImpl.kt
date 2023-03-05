package ru.kekulta.giphyapp.features.likes.domain.impl

import android.util.Log
import kotlinx.coroutines.flow.*
import ru.kekulta.giphyapp.features.likes.domain.api.LikesRepository
import ru.kekulta.giphyapp.features.pager.domain.models.PaginationState.Companion.ITEMS_ON_PAGE
import ru.kekulta.giphyapp.features.search.data.dto.GifSearchRequest
import ru.kekulta.giphyapp.features.search.domain.api.GifInteractor
import ru.kekulta.giphyapp.features.search.domain.api.GifRepository
import ru.kekulta.giphyapp.shared.data.models.GifSearchResponse
import ru.kekulta.giphyapp.shared.data.models.Pagination
import ru.kekulta.giphyapp.shared.data.models.Resource

class GifInteractorLikedImpl(
    private val likesRepository: LikesRepository,
    private val gifRepository: GifRepository
) : GifInteractor {
    override suspend fun searchGifs(request: GifSearchRequest): Flow<Resource<GifSearchResponse>> {
        if (request is GifSearchRequest.LikedRequest) {
            return likesRepository.observeCount().distinctUntilChanged()
                .combine(
                    likesRepository.observePage(request.page).distinctUntilChanged()
                ) { count, page ->
                    if (page.isEmpty()) return@combine Resource.Success(
                        GifSearchResponse(
                            emptyList(),
                            Pagination(1, 1)
                        )
                    )
                    val result = gifRepository.searchGifs(GifSearchRequest.IdsRequest(page, 0))
                    result.let { res ->
                        when (res) {
                            is Resource.Error -> {
                                Log.d(LOG_TAG, "Error with message: ${res.message}")
                                result
                            }
                            is Resource.Success -> {
                                val pagination = Pagination(
                                    request.page,
                                    count / ITEMS_ON_PAGE + if (count % ITEMS_ON_PAGE > 0) 1 else 0,
                                )
                                val listLikeable = res.data.gifList.map { gif ->
                                    gif.copy(liked = true)
                                }
                                val response =
                                    res.data.copy(gifList = listLikeable, pagination = pagination)
                                Resource.Success(response)
                            }
                        }
                    }
                }


        } else {
            return MutableStateFlow(Resource.Error("Bad request"))
        }
    }

    override suspend fun likeGif(id: String) {
        likesRepository.insert(id)
    }

    override suspend fun unlikeGif(id: String) {
        likesRepository.deleteById(id)
    }

    companion object {
        const val LOG_TAG = "GifInteractorLikedImlp"
    }
}