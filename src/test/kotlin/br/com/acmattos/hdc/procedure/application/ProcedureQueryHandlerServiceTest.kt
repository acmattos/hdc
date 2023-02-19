package br.com.acmattos.hdc.odontogram.application

import br.com.acmattos.hdc.common.context.domain.cqs.QueryResult
import br.com.acmattos.hdc.common.context.domain.cqs.QueryStore
import br.com.acmattos.hdc.common.context.domain.model.Repository
import br.com.acmattos.hdc.common.tool.page.EqFilter
import br.com.acmattos.hdc.common.tool.page.Page
import br.com.acmattos.hdc.common.tool.page.PageResult
import br.com.acmattos.hdc.odontogram.domain.cqs.OdontogramQuery
import br.com.acmattos.hdc.odontogram.domain.model.Odontogram
import br.com.acmattos.hdc.odontogram.domain.model.OdontogramBuilder
import io.javalin.http.Context
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import java.util.Optional
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author ACMattos
 * @since 29/03/2022.
 */
object OdontogramQueryHandlerServiceTest: Spek({
    Feature("${OdontogramQueryHandlerService::class.java} usage") {
//        Scenario("handling ${FindAllOdontogramsQuery::class.java} successfully - results found") {
//            lateinit var query: FindAllOdontogramsQuery
//            lateinit var store: QueryStore<OdontogramQuery>
//            lateinit var repository: Repository<Odontogram>
//            lateinit var service: OdontogramQueryHandlerService
//            lateinit var result: QueryResult<Odontogram>
//            Given("""a ${QueryStore::class.java} mock""") {
//                store = mockk()
//            }
//            And("""QueryStore#addQuery just runs""") {
//                every { store.addQuery(any<FindAllOdontogramsQuery>()) } just Runs
//            }
//            And("""a ${Repository::class.java} mock""") {
//                repository = mockk()
//            }
//            And("""repository#findAllByPage returns a${PageResult::class.java.simpleName}""") {
//                every {
//                    repository.findAllByPage(any())
//                } returns PageResult.create(listOf(OdontogramBuilder.buildCreate()), Page.create(), 1)
//            }
//            And("""a ${FindAllOdontogramsQuery::class.java} created from ${Context::class.java}""") {
//                query = OdontogramRequestBuilder.buildFindAllOdontogramsRequest()
//                    .toType() as FindAllOdontogramsQuery
//            }
//            And("""a ${OdontogramQueryHandlerService::class.java} successfully instantiated""") {
//                service = OdontogramQueryHandlerService(store, repository)
//            }
//            When("""#handle is executed""") {
//                result = service.handle(query)
//            }
//            Then("""${QueryResult::class.java} is not empty""") {
//                assertThat(result.results).isNotEmpty()
//            }
//            And("""${QueryResult::class.java} size is one""") {
//                assertThat(result.results.size).isEqualTo(1)
//            }
//            And("""the repository is accessed""") {
//                verify {
//                    repository.findAllByPage(any())
//                }
//            }
//            And("""the query store is accessed as well""") {
//                verify {
//                    store.addQuery(any<FindAllOdontogramsQuery>())
//                }
//            }
//        }
//
//        Scenario("handling ${FindAllOdontogramsQuery::class.java} successfully - results not found") {
//            lateinit var query: FindAllOdontogramsQuery
//            lateinit var store: QueryStore<OdontogramQuery>
//            lateinit var repository: Repository<Odontogram>
//            lateinit var service: OdontogramQueryHandlerService
//            lateinit var result: QueryResult<Odontogram>
//            Given("""a ${QueryStore::class.java} mock""") {
//                store = mockk()
//            }
//            And("""QueryStore#addQuery just runs""") {
//                every { store.addQuery(any<FindAllOdontogramsQuery>()) } just Runs
//            }
//            And("""a ${Repository::class.java} mock""") {
//                repository = mockk()
//            }
//            And("""repository#findAll returns null""") {
//                every {
//                    repository.findAllByPage(any())
//                } returns PageResult.create(listOf(), Page.create(), 0)
//            }
//            And("""a ${FindAllOdontogramsQuery::class.java} created from ${Context::class.java}""") {
//                query = OdontogramRequestBuilder.buildFindAllOdontogramsRequest()
//                    .toType() as FindAllOdontogramsQuery
//            }
//            And("""a ${OdontogramQueryHandlerService::class.java} successfully instantiated""") {
//                service = OdontogramQueryHandlerService(store, repository)
//            }
//            When("""#handle is executed""") {
//                result = service.handle(query)
//            }
//            Then("""${QueryResult::class.java} is empty""") {
//                assertThat(result.results).isEmpty()
//            }
//            And("""the repository is accessed""") {
//                verify {
//                    repository.findAllByPage(any())
//                }
//            }
//            And("""the query store is accessed as well""") {
//                verify {
//                    store.addQuery(any<FindAllOdontogramsQuery>())
//                }
//            }
//        }
//
//        Scenario("handling ${FindAllOdontogramsQuery::class.java} successfully - results found") {
//            lateinit var query: FindTheOdontogramQuery
//            lateinit var store: QueryStore<OdontogramQuery>
//            lateinit var repository: Repository<Odontogram>
//            lateinit var service: OdontogramQueryHandlerService
//            lateinit var result: QueryResult<Odontogram>
//            Given("""a ${QueryStore::class.java} mock""") {
//                store = mockk()
//            }
//            And("""QueryStore#addQuery just runs""") {
//                every { store.addQuery(any<FindTheOdontogramQuery>()) } just Runs
//            }
//            And("""a ${Repository::class.java} mock""") {
//                repository = mockk()
//            }
//            And("""repository#findOneByFilter returns a dentist""") {
//                every {
//                    repository.findOneByFilter(
//                        EqFilter<String, String>(
//                            PROCEDURE_ID.fieldName,
//                            "01FJJDJKDXN4K558FMCKEMQE6B"
//                        )
//                    )
//                } returns Optional.of(OdontogramBuilder.buildCreate())
//            }
//            And("""a ${FindTheOdontogramRequest::class.java} created from ${Context::class.java}""") {
//                query = OdontogramRequestBuilder.buildFindTheOdontogramRequest()
//                    .toType() as FindTheOdontogramQuery
//            }
//            And("""a ${OdontogramQueryHandlerService::class.java} successfully instantiated""") {
//                service = OdontogramQueryHandlerService(store, repository)
//            }
//            When("""#handle is executed""") {
//                result = service.handle(query)
//            }
//            Then("""${QueryResult::class.java} is not empty""") {
//                assertThat(result.results).isNotEmpty()
//            }
//            And("""${QueryResult::class.java} size is one""") {
//                assertThat(result.results.size).isEqualTo(1)
//            }
//            And("""the repository is accessed""") {
//                verify {
//                    repository.findOneByFilter(
//                        EqFilter<String, String>(
//                            PROCEDURE_ID.fieldName,
//                            "01FJJDJKDXN4K558FMCKEMQE6B"
//                        )
//                    )
//                }
//            }
//            And("""the query store is accessed as well""") {
//                verify {
//                    store.addQuery(any<FindTheOdontogramQuery>())
//                }
//            }
//        }
    }
})
