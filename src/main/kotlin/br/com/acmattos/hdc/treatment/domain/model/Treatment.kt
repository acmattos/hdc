package br.com.acmattos.hdc.treatment.domain.model

import br.com.acmattos.hdc.common.context.domain.model.Id
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 11/08/2022.
 */
data class Treatment(
    private var treatmentIdData: TreatmentId? = null,
    private var descriptionData: String? = null,
    private var costData: BigDecimal? = null,
    private var createdAtData: LocalDateTime = LocalDateTime.now(),
    private var updatedAtData: LocalDateTime? = null
) {
//    private var treatmentIdData: TreatmentId? = null,
//    val description get() = descriptionData!!
//    val cost get() = costData!!
//    val enabled get() = enabledData
//    val createdAt get() = createdAtData
//    val updatedAt get() = updatedAtData
//
//    fun apply(events: List<TreatmentEvent>): Treatment {
//        for (event in events) {
//            apply(event)
//        }
//        return this
//    }
//
//    fun apply(event: TreatmentEvent): Treatment {
//        when(event) {
//            is CreateDentalTreatmentEvent-> apply(event)
//            else -> apply(event as UpdateDentalTreatmentEvent)
//        }
//        return this
//    }
//    materialCost
//    clinicalHourCost
//    govTaxCost
//    ccFeeCost
//    dentistCost
//    prostheticCost
//    profit
//    totalCost
// Total Cost of the Procedure R$1.826,94
// Amount Charged for the Procedure R$2.300,00
// Cost of the Prosthesis R$500.00
// Clinic Hours Spent 5
// Taxes 16.00% // Cards 3.39
// Cards 3.39
// Cost of the Dentist - Commission % 40.00%
// Fixed Cost of Dentistry - Monthly
// Dentist's Fixed Cost Per Procedure
//    Custo Total do Material				R$55,83
//    Hora Clínica				R$105,15
//    Impostos				R$368,00
//    Taxas do Cartão				R$77,97
//    Remuneração do Dentista (Comissão %)				R$720,00
//    Remuneração do Dentista (Fixo Mensal)				R$0,00
//    Remuneração do Dentista  (Fixo por Procedimento)				R$0,00
//    Laboratório de Prótese				R$500,00
//    Lucro Obtido				R$473,06
//    Custo Total do Procedimento				R$1.826,94
//    Valor Cobrado pelo Procedimento				R$2.300,00
//    Custo da Prótese				R$500,00
//    Horas Clínicas Gastas				5
//    Impostos				16,00%
//    Cartões				3,39%
//    Custo do Dentista - Comissão % 				40,00%
//    Custo Fixo do Dentista - Mensal
//    Custo Fixo do Dentista - Por Procedimento
//    companion object: Loggable() {
//        fun apply(events: List<TreatmentEvent>): Treatment = Treatment().apply(events)
//        fun apply(event: TreatmentEvent): Treatment = Treatment().apply(event)
//    }
}

/**
 * @author ACMattos
 * @since 11/08/2022.
 */
class TreatmentId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}
