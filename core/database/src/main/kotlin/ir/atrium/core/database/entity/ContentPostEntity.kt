package ir.atrium.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.atrium.core.model.ContentPost
import ir.atrium.core.model.LText
import ir.atrium.core.model.User

@Entity(tableName = "posts")
data class ContentPostEntity(
    @PrimaryKey
    val id: String,
    val subjectId: String,
    @Embedded(prefix = "author_")
    val author: User,
    val kind: String,
    @Embedded(prefix = "title_")
    val title: LText,
    @Embedded(prefix = "body_")
    val body: LText,
    val rating: Int?,
    val evidenceState: String,
    @Embedded(prefix = "time_")
    val timeLabel: LText,
    val agreeCount: Int,
    val disagreeCount: Int,
    val commentCount: Int,
    val viewerAgreed: Boolean,
    val viewerDisagreed: Boolean,
)

fun ContentPostEntity.asExternalModel() = ContentPost(
    id = id,
    subjectId = subjectId,
    author = author,
    kind = kind,
    title = title,
    body = body,
    rating = rating,
    evidenceState = evidenceState,
    timeLabel = timeLabel,
    agreeCount = agreeCount,
    disagreeCount = disagreeCount,
    commentCount = commentCount,
    viewerAgreed = viewerAgreed,
    viewerDisagreed = viewerDisagreed,
)

fun ContentPost.asEntity() = ContentPostEntity(
    id = id,
    subjectId = subjectId,
    author = author,
    kind = kind,
    title = title,
    body = body,
    rating = rating,
    evidenceState = evidenceState,
    timeLabel = timeLabel,
    agreeCount = agreeCount,
    disagreeCount = disagreeCount,
    commentCount = commentCount,
    viewerAgreed = viewerAgreed,
    viewerDisagreed = viewerDisagreed,
)
