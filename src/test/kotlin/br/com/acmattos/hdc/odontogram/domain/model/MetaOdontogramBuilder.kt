package br.com.acmattos.hdc.odontogram.domain.model

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.odontogram.domain.cds.CreateMetaOdontogramCommand
import br.com.acmattos.hdc.odontogram.domain.cds.CreateMetaOdontogramEvent
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 29/08/2022.
 */
class MetaOdontogramBuilder {
    companion object {
        fun build(
            upperLeft: MutableList<MetaTooth> = buildValidUpperLeft(),
            upperRight: MutableList<MetaTooth> = buildValidUpperRight(),
            lowerLeft: MutableList<MetaTooth> = buildValidLowerLeft(),
            lowerRight: MutableList<MetaTooth> = buildValidLowerRight(),
        ) = MetaOdontogram.apply(
            CreateMetaOdontogramEvent(
                CreateMetaOdontogramCommand(
                    MetaOdontogramId("01FWKTXVD72XQ2K4549SYH51FN"),
                    upperLeft,
                    upperRight,
                    lowerLeft,
                    lowerRight,
             true,
                    LocalDateTime.of(2022, 8, 29, 11, 2, 0),
                    AuditLog("who", "what")
                )
            )
        )

        private fun buildValidUpperLeft(
            codes: List<Int> = listOf(11, 12, 13, 14, 15, 16, 17, 18)
        ): MutableList<MetaTooth> {
            return buildValidMetaTeeth(codes)
        }

        private fun buildValidUpperRight(
            codes: List<Int> = listOf(21, 22, 23, 24, 25, 26, 27, 28)
        ): MutableList<MetaTooth> {
            return buildValidMetaTeeth(codes)
        }

        private fun buildValidLowerLeft(
            codes: List<Int> = listOf(31, 32, 33, 34, 35, 36, 37, 38)
        ): MutableList<MetaTooth> {
            return buildValidMetaTeeth(codes)
        }

        private fun buildValidLowerRight(
            codes: List<Int> = listOf(41, 42, 43, 44, 45, 46, 47, 48)
        ): MutableList<MetaTooth> {
            return buildValidMetaTeeth(codes)
        }

        private fun buildInvalidUpperLeft(
            invalidCode: Int = 19
        ): MutableList<MetaTooth> {
            return buildInvalidMetaTeeth(buildValidUpperLeft(), invalidCode)
        }

        private fun buildInvalidUpperRight(
            invalidCode: Int = 29
        ): MutableList<MetaTooth> {
            return buildInvalidMetaTeeth(buildValidUpperRight(), invalidCode)
        }

        private fun buildInvalidLowerLeft(
            invalidCode: Int = 39
        ): MutableList<MetaTooth> {
            return buildInvalidMetaTeeth(buildValidLowerLeft(), invalidCode)
        }

        private fun buildInvalidLowerRight(
            invalidCode: Int = 49
        ): MutableList<MetaTooth> {
            return buildInvalidMetaTeeth(buildValidLowerRight(), invalidCode)
        }

        private fun buildValidMetaTeeth(
            codes: List<Int>
        ): MutableList<MetaTooth> {
            return mutableListOf<MetaTooth>().let { list ->
                codes.stream().forEach { code ->
                    list.add(MetaTooth())
                }
                list
            }
        }

        private fun buildInvalidMetaTeeth(
            codes: MutableList<MetaTooth>, invalidCode: Int
        ): MutableList<MetaTooth> {
            return codes.let {list ->
                list.add(MetaTooth())
                list
            }
        }
//        private fun buildTooth(code: Int) = Tooth()

//        fun buildWithInvalidCode(code: Int = 1) = build(code = code)
//
//        fun buildWithInvalidDescription(description: String = "I") =
//            build(description = description)
    }
}