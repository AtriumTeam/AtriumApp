package ir.atrium.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.atrium.core.database.entity.ContentPostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getPosts(): Flow<List<ContentPostEntity>>

    @Query("SELECT * FROM posts WHERE subjectId = :subjectId ORDER BY id DESC")
    fun getPostsForSubject(subjectId: String): Flow<List<ContentPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(posts: List<ContentPostEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: ContentPostEntity)

    @Query("UPDATE posts SET agreeCount = agreeCount + :diff, viewerAgreed = :agreed WHERE id = :id")
    suspend fun updateAgree(id: String, diff: Int, agreed: Boolean)

    @Query("UPDATE posts SET disagreeCount = disagreeCount + :diff, viewerDisagreed = :disagreed WHERE id = :id")
    suspend fun updateDisagree(id: String, diff: Int, disagreed: Boolean)
}
