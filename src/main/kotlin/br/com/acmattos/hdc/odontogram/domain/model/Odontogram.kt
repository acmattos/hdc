package br.com.acmattos.hdc.odontogram.domain.model

import br.com.acmattos.hdc.common.context.domain.cqs.CreateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertEvent
import br.com.acmattos.hdc.common.context.domain.model.AppliableEntity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.MessageTracker
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.CODE_OUT_OF_RANGE
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SIZE_DIFFERENT_THAN_EIGHT
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SIZE_DIFFERENT_THAN_FIVE
import br.com.acmattos.hdc.odontogram.config.OdontogramLogEnum.ODONTOGRAM
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramCreateEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramUpdateEvent
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramUpsertEvent
import br.com.acmattos.hdc.odontogram.domain.model.OdontogramId.Companion.INVALID_ID
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 24/08/2022.
 */
data class Odontogram(
    private var odontogramIdData: OdontogramId? = null,
    private var upperLeftData: List<Tooth>? = null,
    private var upperRightData: List<Tooth>? = null,
    private var lowerLeftData: List<Tooth>? = null,
    private var lowerRightData: List<Tooth>? = null,
    private var upperLeftChildData: List<Tooth>? = null,
    private var upperRightChildData: List<Tooth>? = null,
    private var lowerLeftChildData: List<Tooth>? = null,
    private var lowerRightChildData: List<Tooth>? = null,
    private var enabledData: Boolean = true,
    override var createdAtData: LocalDateTime = LocalDateTime.now(),
    override var updatedAtData: LocalDateTime? = null,
    override var deletedAtData: LocalDateTime? = null,
): AppliableEntity {
    val odontogramId get() = odontogramIdData!!
    val upperLeft get() = upperLeftData!!
    val upperRight get() = upperRightData!!
    val lowerLeft get() = lowerLeftData!!
    val lowerRight get() = lowerRightData!!
    val upperLeftChild get() = upperLeftChildData!!
    val upperRightChild get() = upperRightChildData!!
    val lowerLeftChild get() = lowerLeftChildData!!
    val lowerRightChild get() = lowerRightChildData!!
    val enabled get() = enabledData
    val createdAt get() = createdAtData
    val updatedAt get() = updatedAtData
    val deletedAt get() = deletedAtData

//    init {
//        validate()
//    }

    override fun apply(event: CreateEvent) {
        odontogramIdData = (event as OdontogramCreateEvent).odontogramId
        upperLeftData = event.upperLeft
        upperRightData = event.upperRight
        lowerLeftData = event.lowerLeft
        lowerRightData = event. lowerRight
        upperLeftChildData = event.upperLeftChild
        upperRightChildData = event. upperRightChild
        lowerLeftChildData = event.lowerLeftChild
        lowerRightChildData = event.lowerRightChild
        enabledData = event.enabled
        validate()
        super.apply(event as CreateEvent)
    }

    override fun apply(event: UpsertEvent) {
        upperLeftData = (event as OdontogramUpsertEvent).upperLeft
        upperRightData = event.upperRight
        lowerLeftData = event.lowerLeft
        lowerRightData = event. lowerRight
        upperLeftChildData = event.upperLeftChild
        upperRightChildData = event. upperRightChild
        lowerLeftChildData = event.lowerLeftChild
        lowerRightChildData = event.lowerRightChild
        enabledData = event.enabled
        validate()
        super.apply(event as UpsertEvent)
    }

    override fun apply(event: UpdateEvent) {
        upperLeftData = (event as OdontogramUpdateEvent).upperLeft
        upperRightData = event.upperRight
        lowerLeftData = event.lowerLeft
        lowerRightData = event. lowerRight
        upperLeftChildData = event.upperLeftChild
        upperRightChildData = event. upperRightChild
        lowerLeftChildData = event.lowerLeftChild
        lowerRightChildData = event.lowerRightChild
        enabledData = event.enabled
        validate()
        super.apply(event as UpdateEvent)
    }

    fun validate(): Odontogram {
        validateUpperLeft()
        validateUpperRight()
        validateLowerLeft()
        validateLowerRight()
        validateUpperLeftChild()
        validateUpperRightChild()
        validateLowerLeftChild()
        validateLowerRightChild()
        return this
    }

    private fun validateUpperLeft() {
        validateUpperLowerSize("Upper left", upperLeftData!!, 8)
        validateUpperLowerRange(upperLeftData!!, 11 .. 18)
    }

    private fun validateUpperRight() {
        validateUpperLowerSize("Upper right", upperRightData!!, 8)
        validateUpperLowerRange(upperRightData!!, 21..28)
    }

    private fun validateLowerLeft() {
        validateUpperLowerSize("Lower left", lowerLeftData!!, 8)
        validateUpperLowerRange(lowerLeftData!!, 41 .. 48)
    }

    private fun validateLowerRight() {
        validateUpperLowerSize("Lower right", lowerRightData!!, 8)
        validateUpperLowerRange(lowerRightData!!, 31..38)
    }

    private fun validateUpperLeftChild() {
        validateUpperLowerSize("Upper left child", upperLeftChildData!!, 5)
        validateUpperLowerRange(upperLeftChildData!!, 51 .. 55)
    }

    private fun validateUpperRightChild() {
        validateUpperLowerSize("Upper right child", upperRightChildData!!, 5)
        validateUpperLowerRange(upperRightChildData!!, 61..65)
    }

    private fun validateLowerLeftChild() {
        validateUpperLowerSize("Lower left child", lowerLeftChildData!!, 5)
        validateUpperLowerRange(lowerLeftChildData!!, 81 .. 85)
    }

    private fun validateLowerRightChild() {
        validateUpperLowerSize("Lower right child", lowerRightChildData!!, 5)
        validateUpperLowerRange(lowerRightChildData!!, 71..75)
    }

    private fun validateUpperLowerSize(
        context: String,
        teeth: List<Tooth>,
        size: Int
    ) {
        val messageTracker: MessageTracker = if (size == 8) {
            SIZE_DIFFERENT_THAN_EIGHT
        } else {
            SIZE_DIFFERENT_THAN_FIVE
        }
        Assertion.assert(
            "$context must contain exactly $size teeth!",
            ODONTOGRAM.name,
            messageTracker
        ) {
            teeth.size == size
        }
    }

    private fun validateUpperLowerRange(
        teeth: List<Tooth>,
        range: IntRange
    ) {
        teeth.stream().forEach { tooth ->
            Assertion.assert(
                "Tooth code [${tooth.code}] does not belong to the range: $range!",
                ODONTOGRAM.name,
                CODE_OUT_OF_RANGE
            ) {
                tooth.code in range
            }
        }
    }

    companion object: Loggable() {
        fun apply(events: List<OdontogramEvent>): Odontogram =
            Odontogram().apply(events) as Odontogram
        fun apply(event: OdontogramEvent): Odontogram =
            Odontogram().apply(event) as Odontogram

        fun create(): Odontogram = Odontogram(
            odontogramIdData = INVALID_ID,
            upperLeftData = createUpperLeft(),
            upperRightData = createUpperRight(),
            lowerLeftData = createLowerLeft(),
            lowerRightData = createLowerRight(),
            upperLeftChildData = createUpperLeftChild(),
            upperRightChildData = createUpperRightChild(),
            lowerLeftChildData = createLowerLeftChild(),
            lowerRightChildData = createLowerRightChild(),
        )

        private fun createUpperLeft(): List<Tooth> =
            mutableListOf<Tooth>().let { list ->
                for (code in 18 downTo 11) {
                    list.add(Tooth.create(code))
                }
                list
            }

        private fun createUpperRight(): MutableList<Tooth> =
            mutableListOf<Tooth>().let { list ->
                for (code in 21..28) {
                    list.add(Tooth.create(code))
                }
                list
            }

        private fun createLowerLeft(): MutableList<Tooth> =
            mutableListOf<Tooth>().let { list ->
                for (code in 48 downTo 41) {
                    list.add(Tooth.create(code))
                }
                list
            }

        private fun createLowerRight(): MutableList<Tooth> =
            mutableListOf<Tooth>().let { list ->
                for (code in 31..38) {
                    list.add(Tooth.create(code))
                }
                list
            }

        private fun createUpperLeftChild(): MutableList<Tooth> =
            mutableListOf<Tooth>().let { list ->
                for (code in 55 downTo 51) {
                    list.add(Tooth.create(code))
                }
                list
            }

        private fun createUpperRightChild(): MutableList<Tooth> =
            mutableListOf<Tooth>().let { list ->
                for (code in 61..65) {
                    list.add(Tooth.create(code))
                }
                list
            }

        private fun createLowerLeftChild(): MutableList<Tooth> =
            mutableListOf<Tooth>().let { list ->
                for (code in 85 downTo 81) {
                    list.add(Tooth.create(code))
                }
                list
            }

        private fun createLowerRightChild(): MutableList<Tooth> =
            mutableListOf<Tooth>().let { list ->
                for (code in 71..75) {
                    list.add(Tooth.create(code))
                }
                list
            }
    }
}

/**
 * @author ACMattos
 * @since 24/08/2022.
 */
class OdontogramId: Id {
    constructor(id: String): super(id)
    constructor(): super()
    companion object {
        val INVALID_ID = OdontogramId(INVALID)
    }
}
