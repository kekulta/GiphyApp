package ru.kekulta.giphyapp.features.list.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kekulta.giphyapp.shared.data.dto.GifSearchRequest
import ru.kekulta.giphyapp.shared.data.dto.GifSearchResponse
import ru.kekulta.giphyapp.features.list.domain.api.GifRepository
import ru.kekulta.giphyapp.shared.data.models.Gif
import ru.kekulta.giphyapp.shared.utils.HTTTPCodes
import android.util.Log


// TODO обработка ошибок
class GifRepositoryImpl(private val networkClient: NetworkClient) : GifRepository {
    override suspend fun searchGifs(query: String): List<Gif> {
        var result = emptyList<Gif>()
        withContext(Dispatchers.IO) {
            val response = networkClient.doRequest(GifSearchRequest(query))
            if (response.resultCode == HTTTPCodes.OK && response is GifSearchResponse) {
                result = response.data.map { gifDto ->
                    Log.d(
                        LOG_TAG,
                        "${gifDto.images["preview_gif"]?.height} : ${gifDto.images["preview_gif"]?.width}"
                    )

                    // TODO fix numbers
                    Gif(
                        gifDto.id,
                        gifDto.images["preview_gif"]?.url
                            ?: gifDto.images.firstNotNullOf { it.value.url }, 100, 150
                    )
                }
            }
        }
        return result
    }

    companion object {
        const val LOG_TAG = "GifRepositoryImpl"
    }
}