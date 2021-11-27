package br.com.acmattos.hdc.scheduler.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.scheduler.domain.model.Patient
import br.com.acmattos.hdc.scheduler.domain.model.PatientId

/**
 * @author ACMattos
 * @since 27/11/2019.
 */
class PatientMdbDocument(
    val patientId: String,
    val cpf: String,
    val fullName: String,
    val contact: String,
    val healthInsurance: String,
): MdbDocument() {
    constructor(
        patient: Patient
    ): this(
        patientId = patient.patientId.id,
        cpf = patient.cpf,
        fullName = patient.fullName,
        contact = patient.contact,
        healthInsurance = patient.healthInsurance,
    )

    override fun toType(): Patient =
        Patient(
            PatientId(patientId),
            cpf,
            fullName,
            contact,
            healthInsurance,
        )
}
