package br.com.acmattos.hdc.scheduler.domain.model

import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.scheduler.config.MessageTrackerCodeEnum.INVALID_PATIENT_FULL_NAME
import br.com.acmattos.hdc.scheduler.config.ScheduleLogEnum.APPOINTMENT

/**
 * @author ACMattos
 * @since 10/07/2019.
 */
data class Patient(
    val patientId: PatientId,
    val cpf: String,
    val fullName: String,
    val contact: String,
    val companyName: String?,
    val planNumber: String?,
    val planName: String?,
): Entity {

    init {
        assertFullName() // TODO test
        // TODO Assert plan data
    }

    private fun assertFullName() {
        Assertion.assert(
            "Invalid name for the Patient",
            APPOINTMENT.name,
            INVALID_PATIENT_FULL_NAME.code
        ) {
            fullName.isNotBlank()
        }
    }
}

/**
 * @author ACMattos
 * @since 17/09/2020.
 */
class PatientId: Id {
    constructor(id: String): super(id)
    constructor(): super()
    companion object {
        fun of(id: String?) = if(id != null) {
            PatientId(id)
        } else {
            PatientId()
        }
    }
}
