package ru.kekulta.giphyapp.features.likes.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.kekulta.giphyapp.features.likes.data.dto.GifLikedDto

@Dao
interface GifLikedDao {
    @Query(
        """
        SELECT EXISTS (SELECT * FROM ${GifLikedDto.TABLE} 
        WHERE ${GifLikedDto.ID} = :id)
        """
    )
    suspend fun isExistById(id: String): Boolean

    @Query(
        """
        SELECT * 
        FROM ${GifLikedDto.TABLE}
        ORDER BY ${GifLikedDto.MODIFY_TIME}
        """
    )
    fun observeAll(): Flow<List<GifLikedDto>>

    @Query(
        """
        SELECT * 
        FROM ${GifLikedDto.TABLE}
        ORDER BY ${GifLikedDto.MODIFY_TIME}
        LIMIT :count
        OFFSET :offset
        """
    )
    fun observePage(count: Int, offset: Int): Flow<List<GifLikedDto>>

    @Query(
        """
        SELECT COUNT(*) 
        FROM ${GifLikedDto.TABLE}
        """
    )
    fun observeCount(): Flow<Int>

    @Query(
        """
        DELETE  
        FROM ${GifLikedDto.TABLE}
        WHERE ${GifLikedDto.ID} = :id
        """
    )
    suspend fun deleteById(id: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dto: GifLikedDto)
}