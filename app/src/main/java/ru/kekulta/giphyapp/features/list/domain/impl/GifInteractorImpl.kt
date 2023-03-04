package ru.kekulta.giphyapp.features.list.domain.impl


import kotlinx.coroutines.flow.*
import ru.kekulta.giphyapp.features.list.data.LikesRepository
import ru.kekulta.giphyapp.features.list.data.dto.GifSearchRequest
import ru.kekulta.giphyapp.features.list.domain.api.GifInteractor
import ru.kekulta.giphyapp.features.list.domain.api.GifRepository
import ru.kekulta.giphyapp.shared.data.models.GifSearchResponse
import ru.kekulta.giphyapp.shared.data.models.Resource

class GifInteractorImpl(
    private val likesRepository: LikesRepository,
    private val gifRepository: GifRepository
) : GifInteractor {
    override suspend fun searchGifs(request: GifSearchRequest): Flow<Resource<GifSearchResponse>> {
        val result = gifRepository.searchGifs(request)

        return likesRepository.observeAll().map { liked ->
            result.let { res ->
                when (res) {
                    is Resource.Error -> result
                    is Resource.Success -> {
                        val listLikeable = res.data.gifList.map { gif ->
                            gif.copy(liked = liked.contains(gif.id))
                        }
                        val response = res.data.copy(gifList = listLikeable)
                        Resource.Success(response)
                    }
                }
            }
        }
    }
}