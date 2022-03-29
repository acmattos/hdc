package br.com.acmattos.hdc.procedure.domain.model

import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 29/03/2022.
 */
class ProcedureBuilder {
    companion object {
        fun build() = Procedure(
            ProcedureId("01FJJDJKDXN4K558FMCKEMQE6B"),
            81000014,
            "Procedure description",
            true,
            LocalDateTime.now(),
            null
        )
    }
}