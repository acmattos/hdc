package br.com.acmattos.hdc.odontogram.domain.model

import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 03/10/2022.
 */
class ToothBuilder {
    companion object {
        fun build(
            code: Int = 11,
            surfaceA: Surface = Surface.create(SurfaceType.INCISAL),
            surfaceB: Surface = Surface.create(SurfaceType.FACIAL),
            surfaceC: Surface = Surface.create(SurfaceType.DISTAL),
            surfaceD: Surface = Surface.create(SurfaceType.MESIAL),
            surfaceE: Surface = Surface.create(SurfaceType.PALATAL),
            createdAt: LocalDateTime = LocalDateTime.of(2022, 8, 3, 13, 38, 0),
            updatedAt: LocalDateTime? = null
        ) = Tooth(code, surfaceA, surfaceB, surfaceC, surfaceD, surfaceE, createdAt, updatedAt)
        fun buildWithInvalidCode(code: Int = 19) = build(code = code)
        fun buildWithInvalidSurfaceAOcclusal(surfaceA: SurfaceType = SurfaceType.OCCLUSAL) = build(surfaceA = Surface.create(surfaceA))
        fun buildWithInvalidSurfaceAIncisal(surfaceA: SurfaceType = SurfaceType.INCISAL) = build(code = 18, surfaceA = Surface.create(surfaceA))
        fun buildWithInvalidSurfaceB(surfaceB: SurfaceType = SurfaceType.INCISAL) = build(surfaceB = Surface.create(surfaceB))
        fun buildWithInvalidSurfaceC(surfaceC: SurfaceType = SurfaceType.FACIAL) = build(surfaceC = Surface.create(surfaceC))
        fun buildWithInvalidSurfaceD(surfaceD: SurfaceType = SurfaceType.FACIAL) = build(surfaceD = Surface.create(surfaceD))
        fun buildWithInvalidSurfaceELingual(surfaceE: SurfaceType = SurfaceType.PALATAL) = build(code = 31, surfaceE = Surface.create(surfaceE))
        fun buildWithInvalidSurfaceEPalatal(surfaceE: SurfaceType = SurfaceType.LINGUAL) = build(code = 21, surfaceE = Surface.create(surfaceE))
    }
}
