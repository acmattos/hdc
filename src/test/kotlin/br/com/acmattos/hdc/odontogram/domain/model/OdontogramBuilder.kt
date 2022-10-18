package br.com.acmattos.hdc.odontogram.domain.model

val UPPER_LEFT = 18 downTo 11
val UPPER_RIGHT = 21 ..  28
val LOWER_LEFT = 48 downTo 41
val LOWER_RIGHT = 31 .. 38
val UPPER_LEFT_CHILD = 55 downTo 51
val UPPER_RIGHT_CHILD = 61 .. 65
val LOWER_LEFT_CHILD = 85 downTo 81
val LOWER_RIGHT_CHILD = 71 .. 75

/**
 * @author ACMattos
 * @since 29/08/2022.
 */
class OdontogramBuilder {
    companion object {
        fun build(
            upperLeft: MutableList<Tooth> = buildTeeth(UPPER_LEFT),
            upperRight: MutableList<Tooth> = buildTeeth(UPPER_RIGHT),
            lowerLeft: MutableList<Tooth> = buildTeeth(LOWER_LEFT),
            lowerRight: MutableList<Tooth> = buildTeeth(LOWER_RIGHT),
            upperLeftChild: MutableList<Tooth> = buildTeeth(UPPER_LEFT_CHILD),
            upperRightChild: MutableList<Tooth> = buildTeeth(UPPER_RIGHT_CHILD),
            lowerLeftChild: MutableList<Tooth> = buildTeeth(LOWER_LEFT_CHILD),
            lowerRightChild: MutableList<Tooth> = buildTeeth(LOWER_RIGHT_CHILD),
        ) = Odontogram(
            upperLeftData = upperLeft,
            upperRightData = upperRight,
            lowerLeftData = lowerLeft,
            lowerRightData = lowerRight,
            upperLeftChildData = upperLeftChild,
            upperRightChildData = upperRightChild,
            lowerLeftChildData = lowerLeftChild,
            lowerRightChildData = lowerRightChild,
        )

        fun buildUpperLeftWithMoreThanEightTeeth(last: Int = UPPER_LEFT.last + 1) = build(upperLeft = buildTeeth(UPPER_LEFT.changeFirst(last)))
        fun buildUpperLeftWithLessThanEightTeeth(last: Int = UPPER_LEFT.last - 1) = build(upperLeft = buildTeeth(UPPER_LEFT.changeFirst(last)))
        fun buildUpperLeftWithEightTeethWrongLast(tooth: Tooth = Tooth.create(UPPER_RIGHT.last)) = build(upperLeft = buildTeeth(UPPER_LEFT).replaceFirstBy(tooth))
        fun buildUpperRightWithMoreThanEightTeeth(last: Int = UPPER_RIGHT.last + 1) = build(upperRight = buildTeeth(UPPER_RIGHT.changeLast(last)))
        fun buildUpperRightWithLessThanEightTeeth(last: Int = UPPER_RIGHT.last - 1) = build(upperRight = buildTeeth(UPPER_RIGHT.changeLast(last)))
        fun buildUpperRightWithEightTeethWrongLast(tooth: Tooth = Tooth.create(UPPER_LEFT.last)) = build(upperRight = buildTeeth(UPPER_RIGHT).replaceLastBy(tooth))
        fun buildLowerLeftWithMoreThanEightTeeth(last: Int = LOWER_LEFT.last + 1) = build(lowerLeft = buildTeeth(LOWER_LEFT.changeFirst(last)))
        fun buildLowerLeftWithLessThanEightTeeth(last: Int = LOWER_LEFT.last - 1) = build(lowerLeft = buildTeeth(LOWER_LEFT.changeFirst(last)))
        fun buildLowerLeftWithEightTeethWrongLast(tooth: Tooth = Tooth.create(LOWER_RIGHT.last)) = build(lowerLeft = buildTeeth(LOWER_LEFT).replaceFirstBy(tooth))
        fun buildLowerRightWithMoreThanEightTeeth(last: Int = LOWER_RIGHT.last + 1) = build(lowerRight = buildTeeth(LOWER_RIGHT.changeLast(last)))
        fun buildLowerRightWithLessThanEightTeeth(last: Int = LOWER_RIGHT.last - 1) = build(lowerRight = buildTeeth(LOWER_RIGHT.changeLast(last)))
        fun buildLowerRightWithEightTeethWrongLast(tooth: Tooth = Tooth.create(LOWER_LEFT.last)) = build(lowerRight = buildTeeth(LOWER_RIGHT).replaceLastBy(tooth))

        fun buildUpperLeftChildWithMoreThanFiveTeeth(last: Int = UPPER_LEFT_CHILD.last + 1) = build(upperLeftChild = buildTeeth(UPPER_LEFT_CHILD.changeFirst(last)))
        fun buildUpperLeftChildWithLessThanFiveTeeth(last: Int = UPPER_LEFT_CHILD.last - 1) = build(upperLeftChild = buildTeeth(UPPER_LEFT_CHILD.changeFirst(last)))
        fun buildUpperLeftChildWithFiveTeethWrongLast(tooth: Tooth = Tooth.create(UPPER_RIGHT_CHILD.last)) = build(upperLeftChild = buildTeeth(UPPER_LEFT_CHILD).replaceFirstBy(tooth))
        fun buildUpperRightChildWithMoreThanFiveTeeth(last: Int = UPPER_RIGHT_CHILD.last + 1) = build(upperRightChild = buildTeeth(UPPER_RIGHT_CHILD.changeLast(last)))
        fun buildUpperRightChildWithLessThanFiveTeeth(last: Int = UPPER_RIGHT_CHILD.last - 1) = build(upperRightChild = buildTeeth(UPPER_RIGHT_CHILD.changeLast(last)))
        fun buildUpperRightChildWithFiveTeethWrongLast(tooth: Tooth = Tooth.create(UPPER_LEFT_CHILD.last)) = build(upperRightChild = buildTeeth(UPPER_RIGHT_CHILD).replaceLastBy(tooth))
        fun buildLowerLeftChildWithMoreThanFiveTeeth(last: Int = LOWER_LEFT_CHILD.last + 1) = build(lowerLeftChild = buildTeeth(LOWER_LEFT_CHILD.changeFirst(last)))
        fun buildLowerLeftChildWithLessThanFiveTeeth(last: Int = LOWER_LEFT_CHILD.last - 1) = build(lowerLeftChild = buildTeeth(LOWER_LEFT_CHILD.changeFirst(last)))
        fun buildLowerLeftChildWithFiveTeethWrongLast(tooth: Tooth = Tooth.create(LOWER_RIGHT_CHILD.last)) = build(lowerLeftChild = buildTeeth(LOWER_LEFT_CHILD).replaceFirstBy(tooth))
        fun buildLowerRightChildWithMoreThanFiveTeeth(last: Int = LOWER_RIGHT_CHILD.last + 1) = build(lowerRightChild = buildTeeth(LOWER_RIGHT_CHILD.changeLast(last)))
        fun buildLowerRightChildWithLessThanFiveTeeth(last: Int = LOWER_RIGHT_CHILD.last - 1) = build(lowerRightChild = buildTeeth(LOWER_RIGHT_CHILD.changeLast(last)))
        fun buildLowerRightChildWithFiveTeethWrongLast(tooth: Tooth = Tooth.create(LOWER_LEFT_CHILD.last)) = build(lowerRightChild = buildTeeth(LOWER_RIGHT_CHILD).replaceLastBy(tooth))

        private fun buildTeeth(
            codes: IntProgression
        ): MutableList<Tooth> = mutableListOf<Tooth>().let { list ->
            codes.forEach { code ->
                list.add(Tooth.create(code))
            }
            list
        }
    }
}

private fun IntProgression.changeLast(last: Int): IntProgression =
    this.first .. last

private fun IntProgression.changeFirst(first: Int): IntProgression =
    first downTo this.last

private fun <E> MutableList<E>.replaceLastBy(
    tooth: E
): MutableList<E> = this.also { list ->
    list.removeLast()
    list.add(tooth)
    list
}

private fun <E> MutableList<E>.replaceFirstBy(
    tooth: E
): MutableList<E> = this.also { list ->
    list.removeFirst()
    list.add(tooth)
    list
}
