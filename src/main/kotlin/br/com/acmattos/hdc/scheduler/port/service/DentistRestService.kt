package br.com.acmattos.hdc.scheduler.port.service

import br.com.acmattos.hdc.common.tool.http.Http
import br.com.acmattos.hdc.common.tool.http.isEmpty
import br.com.acmattos.hdc.common.tool.http.toId
import br.com.acmattos.hdc.common.tool.http.toResult
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import br.com.acmattos.hdc.scheduler.domain.model.Dentist
import br.com.acmattos.hdc.scheduler.domain.model.DentistId
import java.util.Optional
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author ACMattos
 * @since 06/11/2021.
 */
interface PersonRestApiService {
    @GET("persons/{personId}")
    fun findTheDentist(@Path("personId") personId: String): Call<Response>
}

/**
 * @author ACMattos
 * @since 07/11/2021.
 */
class DentistRestService(
    private val personRestApiService: PersonRestApiService
) {
    fun findTheDentist(
        dentistId: DentistId
    ): Optional<Dentist> {
        val response: Response? = personRestApiService
            .findTheDentist(dentistId.id).execute().body()
        if (response != null && !response.isEmpty()) {
            return Optional.of(
                Dentist(
                    response.toId("person_id") { id ->
                        DentistId(id)
                    },
                    response.toResult()["full_name"] as String
                )
            )
        }
        return Optional.empty<Dentist>()
    }
}

/**
 * @author ACMattos
 * @since 06/11/2021.
 */
class DentistRestServiceBuilder(private val http: Http) {
    fun build(): DentistRestService = DentistRestService(
        http.client.create(PersonRestApiService::class.java)
    )
}
