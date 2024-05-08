//package br.com.acmattos.hdc.person.application
//
//import br.com.acmattos.hdc.common.context.domain.cqs.EqFilter
//import br.com.acmattos.hdc.common.context.domain.cqs.QueryResult
//import br.com.acmattos.hdc.common.context.domain.cqs.QueryStore
//import br.com.acmattos.hdc.common.context.domain.model.Repository
//import br.com.acmattos.hdc.person.domain.cqs.FindTheDentistQuery
//import br.com.acmattos.hdc.person.domain.cqs.PersonQuery
//import br.com.acmattos.hdc.person.domain.model.Person
//import br.com.acmattos.hdc.person.domain.model.PersonBuilder
//import br.com.acmattos.hdc.person.port.rest.PersonRequestBuilder.buildFindTheDentistRequest
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
// * @since 03/11/2021.
// */
//object PersonQueryHandlerServiceTest: FreeSpec({
//    "Feature: ${PersonQueryHandlerService::class.java} usage") {
//        "Scenario: handling ${FindTheDentistQuery::class.java} successfully - results found") {
//            lateinit var query: FindTheDentistQuery
//            lateinit var store: QueryStore<PersonQuery>
//            lateinit var repository: Repository<Person>
//            lateinit var service: PersonQueryHandlerService
//            lateinit var result: QueryResult<Person>
//            "Given: a ${QueryStore::class.java} mock" {
//                store = mockk()
//            }
//            "And: QueryStore#addQuery just runs" {
//                every { store.addQuery(any<FindTheDentistQuery>()) } just Runs
//            }
//            "And: a ${Repository::class.java} mock" {
//                repository = mockk()
//            }
//            "And: repository#findOneByFilter returns a dentist" {
//                every {
//                    repository.findOneByFilter(
//                        EqFilter<String, String>("person_id", "01FJJDJKDXN4K558FMCKEMQE6B")
//                    )
//                } returns Optional.of(PersonBuilder.buildDentist())
//            }
//            "And: a ${FindTheDentistQuery::class.java} created from ${Context::class.java}" {
//                query = buildFindTheDentistRequest().toType() as FindTheDentistQuery
//            }
//            "And: a ${PersonQueryHandlerService::class.java} successfully instantiated" {
//                service = PersonQueryHandlerService(store, repository)
//            }
//            "When: #handle is executed" {
//                result = service.handle(query)
//            }
//            "Then: ${QueryResult::class.java} is not empty" {
//                assertThat(result.results).isNotEmpty()
//            }
//            "And: ${QueryResult::class.java} size is one" {
//                assertThat(result.results.size).isEqualTo(1)
//            }
//            "And: the repository is accessed" {
//                verify {
//                    repository.findOneByFilter(EqFilter<String, String>("person_id", "01FJJDJKDXN4K558FMCKEMQE6B"))
//                }
//            }
//            "And: the query store is accessed as well" {
//                verify {
//                    store.addQuery(any<FindTheDentistQuery>())
//                }
//            }
//        }
//
//        "Scenario: handling ${FindTheDentistQuery::class.java} successfully - results not found") {
//            lateinit var query: FindTheDentistQuery
//            lateinit var store: QueryStore<PersonQuery>
//            lateinit var repository: Repository<Person>
//            lateinit var service: PersonQueryHandlerService
//            lateinit var result: QueryResult<Person>
//            "Given: a ${QueryStore::class.java} mock" {
//                store = mockk()
//            }
//            "And: QueryStore#addQuery just runs" {
//                every { store.addQuery(any<FindTheDentistQuery>()) } just Runs
//            }
//            "And: a ${Repository::class.java} mock" {
//                repository = mockk()
//            }
//            "And: repository#findOneByFilter returns null" {
//                every {
//                    repository.findOneByFilter(EqFilter<String, String>("person_id", "01FJJDJKDXN4K558FMCKEMQE6B"))
//                } returns Optional.empty()
//            }
//            "And: a ${FindTheDentistQuery::class.java} created from ${Context::class.java}" {
//                query = buildFindTheDentistRequest().toType() as FindTheDentistQuery
//            }
//            "And: a ${PersonQueryHandlerService::class.java} successfully instantiated" {
//                service = PersonQueryHandlerService(store, repository)
//            }
//            "When: #handle is executed" {
//                result = service.handle(query)
//            }
//            "Then: ${QueryResult::class.java} is empty" {
//                assertThat(result.results).isEmpty()
//            }
//            "And: the repository is accessed" {
//                verify {
//                    repository.findOneByFilter(EqFilter<String, String>("person_id", "01FJJDJKDXN4K558FMCKEMQE6B"))
//                }
//            }
//            "And: the query store is accessed as well" {
//                verify {
//                    store.addQuery(any<FindTheDentistQuery>())
//                }
//            }
//        }
//    }
//})
