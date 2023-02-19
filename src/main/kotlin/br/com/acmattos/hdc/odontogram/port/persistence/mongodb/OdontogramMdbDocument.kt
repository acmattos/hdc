package br.com.acmattos.hdc.odontogram.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.odontogram.domain.model.Odontogram
import br.com.acmattos.hdc.odontogram.domain.model.OdontogramId
import br.com.acmattos.hdc.odontogram.domain.model.Tooth
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 29/12/2022.
 */
class OdontogramMdbDocument(
    val odontogramId: String,
    val upperLeft: List<Tooth>,
    val upperRight: List<Tooth>,
    val lowerLeft: List<Tooth>,
    val lowerRight: List<Tooth>,
    val upperLeftChild: List<Tooth>,
    val upperRightChild: List<Tooth>,
    val lowerLeftChild: List<Tooth>,
    val lowerRightChild: List<Tooth>,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
): MdbDocument() {
    constructor(
        odontogram: Odontogram
    ): this(
        odontogramId = odontogram.odontogramId.id,
        upperLeft = odontogram.upperLeft,
        upperRight = odontogram.upperRight,
        lowerLeft = odontogram.lowerLeft,
        lowerRight = odontogram.lowerRight,
        upperLeftChild = odontogram.upperLeftChild,
        upperRightChild = odontogram.upperRightChild,
        lowerLeftChild = odontogram.lowerLeftChild,
        lowerRightChild = odontogram.lowerRightChild,
        enabled = odontogram.enabled,
        createdAt = odontogram.createdAt,
        updatedAt = odontogram.updatedAt
    )

    override fun toType(): Odontogram =
        Odontogram(
            odontogramIdData = OdontogramId(odontogramId),
            upperLeftData = upperLeft.toMutableList(),
            upperRightData = upperRight.toMutableList(),
            lowerLeftData = lowerLeft.toMutableList(),
            lowerRightData = lowerRight.toMutableList(),
            upperLeftChildData = upperLeftChild.toMutableList(),
            upperRightChildData = upperRightChild.toMutableList(),
            lowerLeftChildData = lowerLeftChild.toMutableList(),
            lowerRightChildData = lowerRightChild.toMutableList(),
            enabledData = enabled,
            createdAtData = createdAt,
            updatedAtData = updatedAt
        )
}