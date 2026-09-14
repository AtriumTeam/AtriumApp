package ir.atrium.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.atrium.core.model.Comment
import ir.atrium.core.model.User

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey
    val id: String,
    val contentId: String,
    @Embedded(prefix = "author_")
    val author: User,
    val body: String,
)

fun CommentEntity.asExternalModel() = Comment(
    id = id,
    contentId = contentId,
    author = author,
    body = body,
)

fun Comment.asEntity() = CommentEntity(
    id = id,
    contentId = contentId,
    author = author,
    body = body,
)
