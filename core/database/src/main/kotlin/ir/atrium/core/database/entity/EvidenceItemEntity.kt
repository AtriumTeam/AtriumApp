package ir.atrium.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.atrium.core.model.EvidenceItem
import ir.atrium.core.model.LText

@Entity(tableName = "evidence")
data class EvidenceItemEntity(
    @PrimaryKey
    val id: String,
    val contentId: String,
    @Embedded(prefix = "lbl_")
    val label: LText,
    val state: String,
)

fun EvidenceItemEntity.asExternalModel() = EvidenceItem(
    id = id,
    contentId = contentId,
    label = label,
    state = state,
)

fun EvidenceItem.asEntity() = EvidenceItemEntity(
    id = id,
    contentId = contentId,
    label = label,
    state = state,
)
