package ru.kekulta.giphyapp.features.likes.domain.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.kekulta.giphyapp.features.likes.data.dto.GifLikedDto

interface LikesRepository {
    suspend fun insert(id: String)

    suspend fun deleteById(id: String)

    suspend fun isLiked(id: String): Boolean

    fun observeAll(): Flow<List<String>>

    fun observePage(page: Int = 1): Flow<List<String>>

    fun observeCount(): Flow<Int>
}