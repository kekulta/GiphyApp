package ru.kekulta.giphyapp.features.likes.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.kekulta.giphyapp.features.likes.data.database.dao.GifLikedDao
import ru.kekulta.giphyapp.features.likes.data.dto.GifLikedDto
import ru.kekulta.giphyapp.features.likes.domain.api.LikesRepository
import ru.kekulta.giphyapp.features.pager.domain.models.PaginationState.Companion.ITEMS_ON_PAGE

class LikesRepositoryImpl(private val dao: GifLikedDao) : LikesRepository {
    override suspend fun insert(id: String) {
        withContext(Dispatchers.IO) {
            dao.insert(GifLikedDto(id))
        }
    }

    override suspend fun deleteById(id: String) {
        withContext(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }

    override suspend fun isLiked(id: String): Boolean =
        dao.isExistById(id)


    override fun observeAll(): Flow<List<String>> = dao.observeAll().map { listDto ->
        listDto.map { dto -> dto.id }
    }

    override fun observePage(page: Int) =
        dao.observePage(ITEMS_ON_PAGE, (page - 1) * ITEMS_ON_PAGE).map { listDto ->
            listDto.map { dto -> dto.id }
        }

    override fun observeCount() = dao.observeCount()
}