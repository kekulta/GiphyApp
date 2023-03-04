package ru.kekulta.giphyapp.features.search.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.kekulta.giphyapp.features.search.data.database.dao.GifLikedDao
import ru.kekulta.giphyapp.features.search.data.dto.GifLikedDto

class LikesRepository(val dao: GifLikedDao) {
    suspend fun insert(id: String) {
        withContext(Dispatchers.IO) {
            dao.insert(GifLikedDto(id))
        }
    }

    suspend fun deleteById(id: String) {
        withContext(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }

    fun observeAll(): Flow<List<String>> = dao.observeAll().map { listDto ->
        listDto.map { dto -> dto.id }
    }
}