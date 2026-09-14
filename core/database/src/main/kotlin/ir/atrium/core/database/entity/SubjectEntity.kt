package ir.atrium.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.atrium.core.model.LText
import ir.atrium.core.model.Subject

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey
    val id: String,
    @Embedded(prefix = "name_")
    val name: LText,
    @Embedded(prefix = "cat_")
    val category: LText,
    @Embedded(prefix = "sum_")
    val summary: LText,
    val followerCount: Int,
    val isOfficial: Boolean,
    val followed: Boolean,
    @Embedded(prefix = "note_")
    val officialNote: LText?,
)

fun SubjectEntity.asExternalModel() = Subject(
    id = id,
    name = name,
    category = category,
    summary = summary,
    followerCount = followerCount,
    isOfficial = isOfficial,
    followed = followed,
    officialNote = officialNote,
)

fun Subject.asEntity() = SubjectEntity(
    id = id,
    name = name,
    category = category,
    summary = summary,
    followerCount = followerCount,
    isOfficial = isOfficial,
    followed = followed,
    officialNote = officialNote,
)
