package ir.atrium.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.atrium.core.model.ActivityItem
import ir.atrium.core.model.LText

@Entity(tableName = "activity")
data class ActivityItemEntity(
    @PrimaryKey
    val id: String,
    @Embedded(prefix = "title_")
    val title: LText,
    @Embedded(prefix = "body_")
    val body: LText,
    val contentId: String?,
    val subjectId: String?,
)

fun ActivityItemEntity.asExternalModel() = ActivityItem(
    id = id,
    title = title,
    body = body,
    contentId = contentId,
    subjectId = subjectId,
)

fun ActivityItem.asEntity() = ActivityItemEntity(
    id = id,
    title = title,
    body = body,
    contentId = contentId,
    subjectId = subjectId,
)
