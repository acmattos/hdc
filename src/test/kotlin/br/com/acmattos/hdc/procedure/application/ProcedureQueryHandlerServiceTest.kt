//package br.com.acmattos.hdc.procedure.application
//
//import br.com.acmattos.hdc.common.context.domain.cqs.EqFilter
//import br.com.acmattos.hdc.common.context.domain.cqs.QueryResult
//import br.com.acmattos.hdc.common.context.domain.cqs.QueryStore
//import br.com.acmattos.hdc.common.context.domain.model.Repository
//import br.com.acmattos.hdc.common.tool.page.Page
//import br.com.acmattos.hdc.common.tool.page.PageResult
//import br.com.acmattos.hdc.procedure.domain.cqs.FindAllProceduresQuery
//import br.com.acmattos.hdc.procedure.domain.cqs.FindTheProcedureQuery
//import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureQuery
//import br.com.acmattos.hdc.procedure.domain.model.Procedure
//import br.com.acmattos.hdc.procedure.domain.model.ProcedureBuilder
//import br.com.acmattos.hdc.procedure.port.persistence.mongodb.DocumentIndexedField.PROCEDURE_ID
//import br.com.acmattos.hdc.procedure.port.rest.FindTheProcedureRequest
//import br.com.acmattos.hdc.procedure.port.rest.ProcedureRequestBuilder
//import io.javalin.http.Context
//import io.mockk.Runs
//import io.mockk.every
//import io.mockk.just
//import io.mockk.mockk
//import io.mockk.verify
//import java.util.Optional
//import org.assertj.core.api.Assertions.assertThat
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.style.gherkin.Feature
//
///**
// * @author ACMattos
// * @since 29/03/2022.
// */
//object ProcedureQueryHandlerServiceTest: Spek({
//    Feature("${ProcedureQueryHandlerService::class.java} usage") {
//        Scenario("handling ${FindAllProceduresQuery::class.java} successfully - results found") {
//            lateinit var query: FindAllProceduresQuery
//            lateinit var store: QueryStore<ProcedureQuery>
//            lateinit var repository: Repository<Procedure>
//            lateinit var service: ProcedureQueryHandlerService
//            lateinit var result: QueryResult<Procedure>
//            Given("""a ${QueryStore::class.java} mock""") {
//                store = mockk()
//            }
//            And("""QueryStore#addQuery just runs""") {
//                every { store.addQuery(any<FindAllProceduresQuery>()) } just Runs
//            }
//            And("""a ${Repository::class.java} mock""") {
//                repository = mockk()
//            }
//            And("""repository#findAllByPage returns a${PageResult::class.java.simpleName}""") {
//                every {
//                    repository.findAllByPage(any<Page>())
//                } returns PageResult.create(listOf(ProcedureBuilder.build(), Page.create(), 10))
//            }
//            And("""a ${FindAllProceduresQuery::class.java} created from ${Context::class.java}""") {
//                query = ProcedureRequestBuilder.buildFindAllProceduresRequest()
//                    .toType() as FindAllProceduresQuery
//            }
//            And("""a ${ProcedureQueryHandlerService::class.java} successfully instantiated""") {
//                service = ProcedureQueryHandlerService(store, repository)
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
//                    repository.findAll()
//                }
//            }
//            And("""the query store is accessed as well""") {
//                verify {
//                    store.addQuery(any<FindAllProceduresQuery>())
//                }
//            }
//        }
//
//        Scenario("handling ${FindAllProceduresQuery::class.java} successfully - results not found") {
//            lateinit var query: FindAllProceduresQuery
//            lateinit var store: QueryStore<ProcedureQuery>
//            lateinit var repository: Repository<Procedure>
//            lateinit var service: ProcedureQueryHandlerService
//            lateinit var result: QueryResult<Procedure>
//            Given("""a ${QueryStore::class.java} mock""") {
//                store = mockk()
//            }
//            And("""QueryStore#addQuery just runs""") {
//                every { store.addQuery(any<FindAllProceduresQuery>()) } just Runs
//            }
//            And("""a ${Repository::class.java} mock""") {
//                repository = mockk()
//            }
//            And("""repository#findAll returns null""") {
//                every {
//                    repository.findAll()
//                } returns listOf()
//            }
//            And("""a ${FindAllProceduresQuery::class.java} created from ${Context::class.java}""") {
//                query = ProcedureRequestBuilder.buildFindAllProceduresRequest()
//                    .toType() as FindAllProceduresQuery
//            }
//            And("""a ${ProcedureQueryHandlerService::class.java} successfully instantiated""") {
//                service = ProcedureQueryHandlerService(store, repository)
//            }
//            When("""#handle is executed""") {
//                result = service.handle(query)
//            }
//            Then("""${QueryResult::class.java} is empty""") {
//                assertThat(result.results).isEmpty()
//            }
//            And("""the repository is accessed""") {
//                verify {
//                    repository.findAll()
//                }
//            }
//            And("""the query store is accessed as well""") {
//                verify {
//                    store.addQuery(any<FindAllProceduresQuery>())
//                }
//            }
//        }
//
//        Scenario("handling ${FindAllProceduresQuery::class.java} successfully - results found") {
//            lateinit var query: FindTheProcedureQuery
//            lateinit var store: QueryStore<ProcedureQuery>
//            lateinit var repository: Repository<Procedure>
//            lateinit var service: ProcedureQueryHandlerService
//            lateinit var result: QueryResult<Procedure>
//            Given("""a ${QueryStore::class.java} mock""") {
//                store = mockk()
//            }
//            And("""QueryStore#addQuery just runs""") {
//                every { store.addQuery(any<FindTheProcedureQuery>()) } just Runs
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
//                } returns Optional.of(ProcedureBuilder.build())
//            }
//            And("""a ${FindTheProcedureRequest::class.java} created from ${Context::class.java}""") {
//                query = ProcedureRequestBuilder.buildFindTheProcedureRequest()
//                    .toType() as FindTheProcedureQuery
//            }
//            And("""a ${ProcedureQueryHandlerService::class.java} successfully instantiated""") {
//                service = ProcedureQueryHandlerService(store, repository)
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
//                    store.addQuery(any<FindTheProcedureQuery>())
//                }
//            }
//        }
//    }
//})
