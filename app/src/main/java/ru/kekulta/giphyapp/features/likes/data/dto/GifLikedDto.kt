package ru.kekulta.giphyapp.features.likes.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kekulta.giphyapp.features.likes.data.dto.GifLikedDto.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class GifLikedDto(
    @PrimaryKey
    @ColumnInfo(name = ID)
    var id: String,
    @ColumnInfo(name = MODIFY_TIME)
    var modifyTime: Long = System.currentTimeMillis()
) {
    companion object {
        const val TABLE = "table_liked_gif"
        const val ID = "liked_gif_id"
        const val MODIFY_TIME = "liked_gif_modify_time"
    }
}