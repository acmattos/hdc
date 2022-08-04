package br.com.acmattos.hdc.person.domain.model

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.person.domain.cqs.CreateDentistCommand
import br.com.acmattos.hdc.person.domain.cqs.CreateDentistEvent
import br.com.acmattos.hdc.person.domain.cqs.CreatePatientCommand
import br.com.acmattos.hdc.person.domain.cqs.CreatePatientEvent
import br.com.acmattos.hdc.person.domain.model.ContactType.EMAIL
import java.time.LocalDate
import java.time.LocalDateTime

private val address = Address(
    "Street Name",
    "Number 30",
    "Complement 1",
    "22777-230",
    "Neighborhood",
    State.RJ,
    "City",
    LocalDateTime.now(),
    null
)
private val contact = Contact(
    "info@email.com",
    EMAIL,
    "OBS",
    LocalDateTime.now(),
    null
)

/**
 * @author ACMattos
 * @since 04/11/2021.
 */
class PersonBuilder {
    companion object {
        fun buildDentist(
            fullName: String = "Full Name",
            dob: LocalDate = LocalDate.of(2000, 11, 7),
            cpf: String = "123.456.789-09",
            maritalStatus: String = "SINGLE",
            gender: String = "MALE",
            personType: PersonType = PersonType.DENTIST,
            personalId: String? = "PI 123",
            addresses: List<Address> = listOf(address),
            contacts: List<Contact> = listOf(contact),
            responsibleFor: PersonId? = PersonId("01FK96GENJKTN1BYZW6BRHFZFJ"),
            familyGroup: List<PersonId> = listOf()
        ) = Person.apply(
            CreateDentistEvent(
                CreateDentistCommand(
                    PersonId("01FK96GENJKTN1BYZW6BRHFZFJ"),
                    fullName,
                    dob,
                    MaritalStatus.convert(maritalStatus),
                    Gender.convert(gender),
                    personType,
                    cpf,
                    personalId,
                    addresses,
                    contacts,
                    responsibleFor,
                    familyGroup,
                    true,
                    LocalDateTime.of(2022, 4, 22, 11, 2, 0),
                    AuditLog("who", "what")
                )
            )
        )

        fun buildPatient(
            fullName: String = "Full Name",
            dob: LocalDate = LocalDate.of(2000, 11, 7),
            cpf: String = "123.456.789-09",//"792.771.510-05",
            maritalStatus: String = "SINGLE",
            gender: String = "MALE",
            personType: PersonType = PersonType.PATIENT,
            personalId: String? = "PI 123",
            occupation: String? = "Occupation",
            addresses: List<Address> = listOf(address),
            contacts: List<Contact> = listOf(contact),
            dentalPlan: DentalPlan? = DentalPlan("Name", "12345"),
            responsibleFor: PersonId? = PersonId("01FK96GENJKTN1BYZW6BRHFZFJ"),
            indicatedBy: String? = "Indicated BY",
            familyGroup: List<PersonId> = listOf(),
            status: String = "REGULAR",
        ) = Person.apply(
            CreatePatientEvent(
                CreatePatientCommand(
                    PersonId("01FK96GENJKTN1BYZW6BRHFZFJ"),
                    fullName,
                    dob,
                    MaritalStatus.convert(maritalStatus),
                    Gender.convert(gender),
                    personType,
                    cpf,
                    personalId,
                    occupation,
                    addresses,
                    contacts,
                    dentalPlan,
                    responsibleFor,
                    indicatedBy,
                    familyGroup,
                    Status.convert(status),
                    true,
                    LocalDateTime.of(2022, 4, 22, 11, 2, 0),
                    AuditLog("who", "what")
                )
            )
        )

        fun buildPatientWithInvalidFullName(fullName: String = "") = buildPatient(fullName = fullName)
        fun buildPatientWithInvalidPersonType(personType: String = "DENTIST") =
            buildPatient(personType = PersonType.convert(personType))
        fun buildDentistWithInvalidPersonType(personType: String = "PATIENT") =
            buildDentist(personType = PersonType.convert(personType))
        fun buildPatientWithInvalidOccupation(occupation: String = "") =
            buildPatient(occupation = occupation)
        fun buildPatientWithInvalidAddresses(addresses: List<Address> = listOf()) =
            buildPatient(addresses = addresses)
        fun buildPatientWithInvalidContacts(contacts: List<Contact> = listOf()) =
            buildPatient(contacts = contacts)
        fun buildPatientWithInvalidDob(dob: LocalDate = LocalDate.now().plusDays(1)) =
            buildPatient(dob = dob)
        fun buildPatientWithInvalidPersonalId(personalId: String = "1234") =
            buildPatient(personalId = personalId)
        fun buildPatientWithInvalidStatus(status: String = "") =
            buildPatient(status = status)
    }
//    companion object {
//        fun build(
//            code: Int = 81000014,
//            description: String = "Procedure description"
//        ) = Procedure.apply(
//            CreateDentalProcedureEvent(
//                CreateDentalProcedureCommand(
//                    ProcedureId("01FK96GENJKTN1BYZW6BRHFZFJ"),
//                    code,
//                    description,
//                    true,
//                    LocalDateTime.of(2022, 3, 30, 11, 2, 0),
//                    AuditLog("who", "what")
//                )
//            )
//        )
//
//        fun buildWithInvalidCode(code: Int = 1) = build(code = code)
//
//        fun buildWithInvalidDescription(description: String = "I") =
//            build(description = description)
//    }
}
