package br.com.acmattos.hdc.odontogram.domain.model

import br.com.acmattos.hdc.common.context.domain.model.ValueObject
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.enum.assertThatTermMatches
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.CODE_OUT_OF_RANGE
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_STAMP_CONVERT_FAILED
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_CONVERT_FAILED
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_DISTAL
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_FACIAL
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_INCISAL
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_LINGUAL
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_MESIAL
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_OCCLUSAL
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SURFACE_TYPE_NOT_ALLOWED_PALATAL
import br.com.acmattos.hdc.odontogram.config.MetaOdontogramLogEnum.TOOTH
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 24/08/2022.
 */
data class Tooth(
    val code: Int,
    val surfaceA: Surface,
    val surfaceB: Surface,
    val surfaceC: Surface,
    val surfaceD: Surface,
    val surfaceE: Surface,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null
): ValueObject {

    init {
        assertCodeIsValid()
        assertSurfaceAIsValid()
        assertSurfaceBIsValid()
        assertSurfaceCIsValid()
        assertSurfaceDIsValid()
        assertSurfaceEIsValid()
    }

    private fun assertCodeIsValid() {
        Assertion.assert(
            "Code is out of range: 11-18 | 21-28 | 31-38 | 41-48 | 51-55 | 61-65 | 71-75 | 81-85!",
            TOOTH.name,
            CODE_OUT_OF_RANGE
        ) {
            code in 11..18 || code in 21..28
                || code in 31..38 || code in 41..48
                || code in 51..55 || code in 61..65
                || code in 71..75 || code in 81..85
        }
    }

    private fun assertSurfaceAIsValid() {
        if(isIncisal(code)){
            Assertion.assert(
                "Code [$code] does not allow surface A [${surfaceA.type}]: ${SurfaceType.INCISAL}!",
                TOOTH.name,
                SURFACE_TYPE_NOT_ALLOWED_INCISAL
            ) {
                SurfaceType.INCISAL == surfaceA.type
            }
        }
        if(isOcclusal(code)){
            Assertion.assert(
                "Code [$code] does not allow surface A [${surfaceA.type}]: ${SurfaceType.OCCLUSAL}!",
                TOOTH.name,
                SURFACE_TYPE_NOT_ALLOWED_OCCLUSAL
            ) {
                SurfaceType.OCCLUSAL == surfaceA.type
            }
        }
    }

    private fun assertSurfaceBIsValid() {
        Assertion.assert(
            "Surface B does not allow [${surfaceB.type}]: ${SurfaceType.FACIAL}!",
            TOOTH.name,
            SURFACE_TYPE_NOT_ALLOWED_FACIAL
        ) {
            SurfaceType.FACIAL == surfaceB.type
        }
    }

    private fun assertSurfaceCIsValid() {
        Assertion.assert(
            "Surface C does not allow [${surfaceC.type}]: ${SurfaceType.DISTAL}!",
            TOOTH.name,
            SURFACE_TYPE_NOT_ALLOWED_DISTAL
        ) {
            SurfaceType.DISTAL == surfaceC.type
        }
    }

    private fun assertSurfaceDIsValid() {
        Assertion.assert(
            "Surface D does not allow [${surfaceD.type}]: ${SurfaceType.MESIAL}!",
            TOOTH.name,
            SURFACE_TYPE_NOT_ALLOWED_MESIAL
        ) {
            SurfaceType.MESIAL == surfaceD.type
        }
    }

    private fun assertSurfaceEIsValid() {
        if(isLingual(code)){
            Assertion.assert(
                "Code [$code] does not allow surface E [${surfaceE.type}]: ${SurfaceType.LINGUAL}!",
                TOOTH.name,
                SURFACE_TYPE_NOT_ALLOWED_LINGUAL
            ) {
                SurfaceType.LINGUAL == surfaceE.type
            }
        }
        if(isPalatal(code)){
            Assertion.assert(
                "Code [$code] does not allow surface E [${surfaceE.type}]: ${SurfaceType.PALATAL}!",
                TOOTH.name,
                SURFACE_TYPE_NOT_ALLOWED_PALATAL
            ) {
                SurfaceType.PALATAL == surfaceE.type
            }
        }
    }

    companion object: Loggable() {
        fun create(code: Int): Tooth = Tooth(
            code,
            surfaceA(code),
            surfaceB(),
            surfaceC(),
            surfaceD(),
            surfaceE(code)
        )

        private fun surfaceA(code: Int): Surface = if(isIncisal(code)) {
                Surface.create(SurfaceType.INCISAL)
            } else {
                Surface.create(SurfaceType.OCCLUSAL)
            }

        private fun surfaceB(): Surface = Surface.create(SurfaceType.FACIAL)

        private fun surfaceC(): Surface = Surface.create(SurfaceType.DISTAL)

        private fun surfaceD(): Surface  = Surface.create(SurfaceType.MESIAL)

        private fun surfaceE(code: Int): Surface = if(isLingual(code)) {
                Surface.create(SurfaceType.LINGUAL)
            } else {
                Surface.create(SurfaceType.PALATAL)
            }

        private fun isIncisal(code: Int) =
            code in 11..13 || code in 21..23
            || code in 31..33 || code in 41..43
            || code in 51..53 || code in 61..63
            || code in 71..73 || code in 81..83

        private fun isOcclusal(code: Int) =
            code in 14..18 || code in 24..28
            || code in 34..38 || code in 44..48
            || code in 54..55 || code in 64..65
            || code in 74..75 || code in 84..85

        private fun isLingual(code: Int) =
            code in 31..38 || code in 41..48
            || code in 71..75 || code in 81..85

        private fun isPalatal(code: Int) =
            code in 11..18 || code in 21..28
            || code in 51..55 || code in 61..65
    }
}

/**
 * @author ACMattos
 * @since 29/09/2022.
 */
data class Surface(
    val type: SurfaceType,
    val stamp: SurfaceStamp = SurfaceStamp.HEALTHY,
    val note: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAtData: LocalDateTime? = null
): ValueObject {
    companion object: Loggable() {
        fun create(surfaceType: SurfaceType) = Surface(surfaceType)
    }
}

/**
 * @author ACMattos
 * @since 29/09/2022.
 */
enum class SurfaceType(val stamp: String) {
    INCISAL("I"),
    OCCLUSAL("O"),
    FACIAL("V"),
    DISTAL("D"),
    MESIAL("M"),
    LINGUAL("L"),
    PALATAL("P");

    companion object {
        fun convert(term: String): SurfaceType = assertThatTermMatches(
            term,
            "[$term] does not correspond to a valid surface type!",
            TOOTH.name,
            SURFACE_TYPE_CONVERT_FAILED,
        ) { surfaceType, stamp ->
            surfaceType.stamp == stamp.toUpperCase()
        }
    }
}

/**
 * @author ACMattos
 * @since 29/09/2022.
 */
enum class SurfaceStamp(val stamp: String) {
    HEALTHY(""),
    CARIES("C"),
    AMALGAM_RESTORATION("RA"),
    RESIN_RESTORATION("RR"),
    PORCELAIN_FACET("FP"),
    RESIN_FACET("FR");

    companion object {
        fun convert(term: String): SurfaceStamp = assertThatTermMatches(
            term,
            "[$term] does not correspond to a valid surface stamp!",
            TOOTH.name,
            SURFACE_STAMP_CONVERT_FAILED,
        ) { surfaceStamp, stamp ->
            surfaceStamp.stamp == stamp.toUpperCase()
        }
    }
}