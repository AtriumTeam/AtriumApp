package ir.atrium.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.atrium.core.database.entity.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments ORDER BY id ASC")
    fun getComments(): Flow<List<CommentEntity>>

    @Query("SELECT * FROM comments WHERE contentId = :contentId ORDER BY id ASC")
    fun getCommentsForPost(contentId: String): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(comments: List<CommentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: CommentEntity)
}
