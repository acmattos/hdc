//package br.com.acmattos.hdc.odontogram.domain.model
//
//import br.com.acmattos.hdc.common.context.domain.cqs.EntityEvent
//import br.com.acmattos.hdc.common.context.domain.model.AppliableEntity
//import br.com.acmattos.hdc.common.context.domain.model.Id
//import br.com.acmattos.hdc.common.tool.assertion.Assertion
//import br.com.acmattos.hdc.common.tool.loggable.Loggable
//import br.com.acmattos.hdc.common.tool.server.javalin.MessageTracker
//import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.CODE_OUT_OF_RANGE
//import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SIZE_DIFFERENT_THAN_EIGHT
//import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SIZE_DIFFERENT_THAN_FIVE
//import br.com.acmattos.hdc.odontogram.config.OdontogramLogEnum.ODONTOGRAM
//import br.com.acmattos.hdc.odontogram.domain.cqs.CreateOdontogramEvent
//import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramEvent
//import java.time.LocalDateTime
//
///**
// * @author ACMattos
// * @since 24/08/2022.
// */
//data class Odontogram(
//    private var odontogramIdData: OdontogramId? = null,
//    private var upperLeftData: MutableList<Tooth>? = null,
//    private var upperRightData: MutableList<Tooth>? = null,
//    private var lowerLeftData: MutableList<Tooth>? = null,
//    private var lowerRightData: MutableList<Tooth>? = null,
//    private var upperLeftChildData: MutableList<Tooth>? = null,
//    private var upperRightChildData: MutableList<Tooth>? = null,
//    private var lowerLeftChildData: MutableList<Tooth>? = null,
//    private var lowerRightChildData: MutableList<Tooth>? = null,
//    private var enabledData: Boolean = true,
//    private var createdAtData: LocalDateTime = LocalDateTime.now(),
//    private var updatedAtData: LocalDateTime? = null
//): AppliableEntity {
//    val odontogramId get() = odontogramIdData //?: ""
//    val upperLeft get() = upperLeftData!!
//    val upperRight get() = upperRightData!!
//    val lowerLeft get() = lowerLeftData!!
//    val lowerRight get() = lowerRightData!!
//    val upperLeftChild get() = upperLeftChildData!!
//    val upperRightChild get() = upperRightChildData!!
//    val lowerLeftChild get() = lowerLeftChildData!!
//    val lowerRightChild get() = lowerRightChildData!!
//    val enabled get() = enabledData
//    val createdAt get() = createdAtData
//    val updatedAt get() = updatedAtData
//
//    init {
//        validate()
//    }
//
//    private fun validate() {
//        validateUpperLeft()
//        validateUpperRight()
//        validateLowerLeft()
//        validateLowerRight()
//        validateUpperLeftChild()
//        validateUpperRightChild()
//        validateLowerLeftChild()
//        validateLowerRightChild()
//    }
//
//    private fun validateUpperLeft() {
//        validateUpperLowerSize("Upper left", upperLeftData!!, 8)
//        validateUpperLowerRange(upperLeftData!!, 11 .. 18)
//    }
//
//    private fun validateUpperRight() {
//        validateUpperLowerSize("Upper right", upperRightData!!, 8)
//        validateUpperLowerRange(upperRightData!!, 21..28)
//    }
//
//    private fun validateLowerLeft() {
//        validateUpperLowerSize("Lower left", lowerLeftData!!, 8)
//        validateUpperLowerRange(lowerLeftData!!, 41 .. 48)
//    }
//
//    private fun validateLowerRight() {
//        validateUpperLowerSize("Lower right", lowerRightData!!, 8)
//        validateUpperLowerRange(lowerRightData!!, 31..38)
//    }
//
//    private fun validateUpperLeftChild() {
//        validateUpperLowerSize("Upper left child", upperLeftChildData!!, 5)
//        validateUpperLowerRange(upperLeftChildData!!, 51 .. 55)
//    }
//
//    private fun validateUpperRightChild() {
//        validateUpperLowerSize("Upper right child", upperRightChildData!!, 5)
//        validateUpperLowerRange(upperRightChildData!!, 61..65)
//    }
//
//    private fun validateLowerLeftChild() {
//        validateUpperLowerSize("Lower left child", lowerLeftChildData!!, 5)
//        validateUpperLowerRange(lowerLeftChildData!!, 81 .. 85)
//    }
//
//    private fun validateLowerRightChild() {
//        validateUpperLowerSize("Lower right child", lowerRightChildData!!, 5)
//        validateUpperLowerRange(lowerRightChildData!!, 71..75)
//    }
//
//    private fun validateUpperLowerSize(
//        context: String,
//        teeth: MutableList<Tooth>,
//        size: Int
//    ) {
//        val messageTracker: MessageTracker = if (size == 8) {
//            SIZE_DIFFERENT_THAN_EIGHT
//        } else {
//            SIZE_DIFFERENT_THAN_FIVE
//        }
//        Assertion.assert(
//            "$context must contain exactly $size teeth!",
//            ODONTOGRAM.name,
//            messageTracker
//        ) {
//            teeth.size == size
//        }
//    }
//
//    private fun validateUpperLowerRange(
//        teeth: MutableList<Tooth>,
//        range: IntRange
//    ) {
//        teeth.stream().forEach { tooth ->
//            Assertion.assert(
//                "Tooth code [${tooth.code}] does not belong to the range: $range!",
//                ODONTOGRAM.name,
//                CODE_OUT_OF_RANGE
//            ) {
//                tooth.code in range
//            }
//        }
//    }
//
//    fun apply(events: List<OdontogramEvent>): Odontogram {
//        for (event in events) {
//            apply(event)
//        }
//        return this
//    }
//
//    override fun apply(event: EntityEvent): Odontogram {
//        when(event) {
//            is CreateOdontogramEvent -> apply(event)
//            else -> apply(event as CreateOdontogramEvent)
//        }
//        return this
//    }
//
////    private fun apply(event: CreateOdontogramEvent) {
////        metaOdontogramIdData = event.metaOdontogramId
////        upperLeftData = event.upperLeft
////        upperRightData = event.upperRight
////        lowerLeftData = event.lowerLeft
////        lowerRightData = event.lowerRight
////        enabledData = event.enabled
////        createdAtData = event.createdAt
//////        assertCodeIsValid()
//////        assertDescriptionIsValid()
////    }
//
////    private fun assertCodeIsValid() {
////        Assertion.assert(
////            "Code is out of range (81000014-87000199)!",
////            PROCEDURE.name,
////            CODE_OUT_OF_RANGE.code
////        ) {
////            codeData!! in 81000014..87000199
////        }
////    }
////
////    private fun assertDescriptionIsValid() {
////        Assertion.assert(
////            "Description length is out of range (3-120)!",
////            PROCEDURE.name,
////            DESCRIPTION_INVALID_LENGTH.code
////        ) {
////            descriptionData!!.length in 3..120
////        }
////    }
//
//    companion object: Loggable() {
//        fun create(): Odontogram = Odontogram(
//            upperLeftData = createUpperLeft(),
//            upperRightData = createUpperRight(),
//            lowerLeftData = createLowerLeft(),
//            lowerRightData = createLowerRight(),
//            upperLeftChildData = createUpperLeftChild(),
//            upperRightChildData = createUpperRightChild(),
//            lowerLeftChildData = createLowerLeftChild(),
//            lowerRightChildData = createLowerRightChild(),
//        )
//        private fun createUpperLeft(): MutableList<Tooth> =
//            mutableListOf<Tooth>().let { list ->
//                for (code in 18 downTo 11) {
//                    list.add(Tooth.create(code))
//                }
//                list
//            }
//        private fun createUpperRight(): MutableList<Tooth> =
//            mutableListOf<Tooth>().let { list ->
//                for (code in 21..28) {
//                    list.add(Tooth.create(code))
//                }
//                list
//            }
//        private fun createLowerLeft(): MutableList<Tooth> =
//            mutableListOf<Tooth>().let { list ->
//                for (code in 48 downTo 41) {
//                    list.add(Tooth.create(code))
//                }
//                list
//            }
//        private fun createLowerRight(): MutableList<Tooth> =
//            mutableListOf<Tooth>().let { list ->
//                for (code in 31..38) {
//                    list.add(Tooth.create(code))
//                }
//                list
//            }
//        private fun createUpperLeftChild(): MutableList<Tooth> =
//            mutableListOf<Tooth>().let { list ->
//                for (code in 55 downTo 51) {
//                    list.add(Tooth.create(code))
//                }
//                list
//            }
//        private fun createUpperRightChild(): MutableList<Tooth> =
//            mutableListOf<Tooth>().let { list ->
//                for (code in 61..65) {
//                    list.add(Tooth.create(code))
//                }
//                list
//            }
//        private fun createLowerLeftChild(): MutableList<Tooth> =
//            mutableListOf<Tooth>().let { list ->
//                for (code in 85 downTo 81) {
//                    list.add(Tooth.create(code))
//                }
//                list
//            }
//        private fun createLowerRightChild(): MutableList<Tooth> =
//            mutableListOf<Tooth>().let { list ->
//                for (code in 71..75) {
//                    list.add(Tooth.create(code))
//                }
//                list
//            }
//
//        fun apply(events: List<OdontogramEvent>): Odontogram = Odontogram().apply(events)
//        fun apply(event: OdontogramEvent): Odontogram = Odontogram().apply(event)
//    }
//
//
//}
//
///**
// * @author ACMattos
// * @since 24/08/2022.
// */
//class OdontogramId: Id {
//    constructor(id: String): super(id)
//    constructor(): super()
//}
