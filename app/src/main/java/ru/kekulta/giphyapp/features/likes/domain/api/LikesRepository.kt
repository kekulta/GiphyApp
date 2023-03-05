package ru.kekulta.giphyapp.features.likes.domain.api

import kotlinx.coroutines.flow.Flow

interface LikesRepository {
    suspend fun insert(id: String)

    suspend fun deleteById(id: String)

    suspend fun isLiked(id: String): Boolean

    fun observeAll(): Flow<List<String>>

    fun observePage(page: Int = 1): Flow<List<String>>

    fun observeCount(): Flow<Int>
}