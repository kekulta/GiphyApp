package ru.kekulta.giphyapp.features.search.domain.impl


import android.util.Log
import kotlinx.coroutines.flow.*
import ru.kekulta.giphyapp.features.likes.domain.api.LikesRepository
import ru.kekulta.giphyapp.features.search.data.dto.GifSearchRequest
import ru.kekulta.giphyapp.features.search.domain.api.GifInteractor
import ru.kekulta.giphyapp.features.search.domain.api.GifRepository
import ru.kekulta.giphyapp.shared.data.models.GifSearchResponse
import ru.kekulta.giphyapp.shared.data.models.Resource

class GifInteractorSearchImpl(
    private val likesRepository: LikesRepository, private val gifRepository: GifRepository
) : GifInteractor {
    override suspend fun searchGifs(request: GifSearchRequest): Flow<Resource<GifSearchResponse>> {
        if (request is GifSearchRequest.QueryRequest) {
            Log.d(LOG_TAG, "New search request: $request")
            val result = gifRepository.searchGifs(request)

            return likesRepository.observeAll().distinctUntilChanged().map { liked ->
                Log.d(LOG_TAG, "Likes observed: ${liked.size}")
                result.let { res ->
                    when (res) {
                        is Resource.Error -> {
                            Log.d(LOG_TAG, "Resource Error")
                            result
                        }
                        is Resource.Success -> {
                            val listLikeable = res.data.gifList.map { gif ->
                                gif.copy(liked = liked.contains(gif.id))
                            }
                            val response = res.data.copy(gifList = listLikeable)
                            Log.d(LOG_TAG, "Resource Success")
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
        const val LOG_TAG = "GifInterctorSearchImpl"
    }
}