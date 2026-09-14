package ir.atrium.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.atrium.core.database.dao.ActivityDao
import ir.atrium.core.database.dao.CommentDao
import ir.atrium.core.database.dao.EvidenceDao
import ir.atrium.core.database.dao.PostDao
import ir.atrium.core.database.dao.SubjectDao
import ir.atrium.core.database.entity.ActivityItemEntity
import ir.atrium.core.database.entity.CommentEntity
import ir.atrium.core.database.entity.ContentPostEntity
import ir.atrium.core.database.entity.EvidenceItemEntity
import ir.atrium.core.database.entity.SubjectEntity

@Database(
    entities = [
        SubjectEntity::class,
        ContentPostEntity::class,
        CommentEntity::class,
        EvidenceItemEntity::class,
        ActivityItemEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AtriumDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun evidenceDao(): EvidenceDao
    abstract fun activityDao(): ActivityDao
}
