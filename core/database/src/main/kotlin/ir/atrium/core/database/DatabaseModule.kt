package ir.atrium.core.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.atrium.core.database.dao.ActivityDao
import ir.atrium.core.database.dao.CommentDao
import ir.atrium.core.database.dao.EvidenceDao
import ir.atrium.core.database.dao.PostDao
import ir.atrium.core.database.dao.SubjectDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesAtriumDatabase(
        @ApplicationContext context: Context,
    ): AtriumDatabase = Room.databaseBuilder(
        context,
        AtriumDatabase::class.java,
        "atrium-db",
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun providesSubjectDao(database: AtriumDatabase): SubjectDao = database.subjectDao()

    @Provides
    fun providesPostDao(database: AtriumDatabase): PostDao = database.postDao()

    @Provides
    fun providesCommentDao(database: AtriumDatabase): CommentDao = database.commentDao()

    @Provides
    fun providesEvidenceDao(database: AtriumDatabase): EvidenceDao = database.evidenceDao()

    @Provides
    fun providesActivityDao(database: AtriumDatabase): ActivityDao = database.activityDao()
}
