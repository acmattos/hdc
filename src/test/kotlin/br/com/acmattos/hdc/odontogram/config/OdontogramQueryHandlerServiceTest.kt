package br.com.acmattos.hdc.odontogram.config

import br.com.acmattos.hdc.common.context.domain.cqs.QueryResult
import br.com.acmattos.hdc.common.context.domain.cqs.QueryStore
import br.com.acmattos.hdc.odontogram.domain.cqs.GetAnOdontogramQuery
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramQuery
import br.com.acmattos.hdc.odontogram.domain.model.Odontogram
import br.com.acmattos.hdc.odontogram.port.rest.OdontogramRequestBuilder
import io.javalin.http.Context
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author ACMattos
 * @since 26/12/2022.
 */
object OdontogramQueryHandlerServiceTest: Spek({
    Feature("${OdontogramQueryHandlerService::class.java} usage") {
        Scenario("handling ${GetAnOdontogramQuery::class.java} successfully") {
            lateinit var query: GetAnOdontogramQuery
            lateinit var store: QueryStore<OdontogramQuery>
            lateinit var service: OdontogramQueryHandlerService
            lateinit var result: QueryResult<Odontogram>
            Given("""a ${QueryStore::class.java} mock""") {
                store = mockk()
            }
            And("""QueryStore#addQuery just runs""") {
                every { store.addQuery(any<GetAnOdontogramQuery>()) } just Runs
            }
            And("""a ${GetAnOdontogramQuery::class.java} created from ${Context::class.java}""") {
                query = OdontogramRequestBuilder.buildGetAnOdontogramRequest()
                    .toType() as GetAnOdontogramQuery
            }
            And("""a ${OdontogramQueryHandlerService::class.java} successfully instantiated""") {
                service = OdontogramQueryHandlerService(store)
            }
            When("""#handle is executed""") {
                result = service.handle(query)
            }
            Then("""${QueryResult::class.java} is not empty""") {
                assertThat(result.results).isNotEmpty()
            }
            And("""${QueryResult::class.java} size is one""") {
                assertThat(result.results.size).isEqualTo(1)
            }
            And("""the query store is accessed as well""") {
                verify {
                    store.addQuery(any<GetAnOdontogramQuery>())
                }
            }
        }
    }
})

