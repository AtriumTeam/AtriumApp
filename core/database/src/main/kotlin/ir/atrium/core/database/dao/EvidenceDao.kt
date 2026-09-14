package ir.atrium.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.atrium.core.database.entity.EvidenceItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EvidenceDao {
    @Query("SELECT * FROM evidence")
    fun getEvidence(): Flow<List<EvidenceItemEntity>>

    @Query("SELECT * FROM evidence WHERE contentId = :contentId")
    fun getEvidenceForPost(contentId: String): Flow<List<EvidenceItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(evidence: List<EvidenceItemEntity>)
}
