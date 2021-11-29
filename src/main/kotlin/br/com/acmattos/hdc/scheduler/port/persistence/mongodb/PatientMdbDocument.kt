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
    val companyName: String?,
    val planNumber: String?,
    val planName: String?,
): MdbDocument() {
    constructor(
        patient: Patient
    ): this(
        patientId = patient.patientId.id,
        cpf = patient.cpf,
        fullName = patient.fullName,
        contact = patient.contact,
        companyName = patient.companyName,
        planNumber = patient.planNumber,
        planName = patient.planName,
    )

    override fun toType(): Patient =
        Patient(
            PatientId(patientId),
            cpf,
            fullName,
            contact,
            companyName,
            planNumber,
            planName,
        )
}
