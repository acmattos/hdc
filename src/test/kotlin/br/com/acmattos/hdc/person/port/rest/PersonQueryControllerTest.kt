//package br.com.acmattos.hdc.person.port.rest
//
//import br.com.acmattos.hdc.common.context.domain.cqs.QueryResult
//import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder
//import br.com.acmattos.hdc.common.tool.server.javalin.Response
//import br.com.acmattos.hdc.common.tool.server.javalin.getRequest
//import br.com.acmattos.hdc.common.tool.server.mapper.JacksonObjectMapperFactory
//import br.com.acmattos.hdc.person.application.PersonQueryHandlerService
//import br.com.acmattos.hdc.person.domain.cqs.FindAllPersonsQuery
//import br.com.acmattos.hdc.person.domain.cqs.FindTheDentistQuery
//import br.com.acmattos.hdc.person.domain.model.Person
//import br.com.acmattos.hdc.person.domain.model.PersonBuilder
//import io.javalin.http.Context
//import io.javalin.plugin.json.JavalinJackson
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import io.mockk.verifyOrder
//import java.util.Optional
//import org.assertj.core.api.Assertions.assertThat
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.style.gherkin.Feature
//
//private const val STATUS = 200
//
///**
// * @author ACMattos
// * @since 02/11/2021.
// */
//object PersonQueryControllerTest: Spek({
////    afterEachTest { // Avoids error when all tests are executed at the same time
////        JavalinJackson.configure(JacksonObjectMapperFactory.build())
////    }
//    Feature("${PersonQueryController::class.java} usage") {
////        Scenario("handling ${FindAllPersonsQuery::class.java} successfully") {
////            lateinit var request: FindTheDentistRequest
////            lateinit var query: FindTheDentistQuery
////            lateinit var result: QueryResult<Person>
////            lateinit var service: PersonQueryHandlerService
////            lateinit var context: Context
////            lateinit var controller: PersonQueryController
////            Given("""a ${FindTheDentistRequest::class.java} successfully instantiated""") {
////                request = PersonRequestBuilder.buildFindTheDentistRequest()
////            }
////            And("""a ${FindTheDentistQuery::class.java} successfully generated""") {
////                query = request.toType() as FindTheDentistQuery
////            }
////            And("""a ${QueryResult::class.java} successfully instantiated""") {
////                result = QueryResult.build(Optional.of(PersonBuilder.buildDentist()))
////            }
////            And("""a ${PersonQueryHandlerService::class.java} mock""") {
////                service = mockk()
////            }
////            And("""service#handle returning the ${QueryResult::class.java}""") {
////                every { service.handle(any<FindTheDentistQuery>()) } returns result
////            }
////            And("""a ${Context::class.java} mock""") {
////                context = ContextBuilder().mockContext("dentist_id")
////            }
////            And("""${JavalinJackson::class.java} is properly configured""") {
////                JavalinJackson.configure(JacksonObjectMapperFactory.build())
////            }
////            And("""context#fullUrl returns fullUrl""") {
////                every { context.fullUrl() } returns "fullUrl"
////            }
////            And("""context#status returns ${Context::class.java}""") {
////                every { context.status(STATUS) } returns context
////            }
////            And("""context#status returns $STATUS""") {
////                every { context.status() } returns STATUS
////            }
////            And("""context#json returns ${Context::class.java}""") {
////                every { context.json(Response("", "", 0, "")) } returns context
////            }
////            And("""a ${PersonQueryController::class.java} successfully instantiated""") {
////                controller = PersonQueryController(service)
////            }
////            When("""#findTheDentist is executed""") {
////                controller.findTheDentist(context)
////            }
////            Then("""status is $STATUS""") {
////                assertThat(context.status()).isEqualTo(STATUS)
////            }
////            And("""the context is accessed in the right order""") {
////                verifyOrder {
////                    context.getRequest(::FindTheDentistRequest)
////                    context.fullUrl()
////                    context.status(STATUS)
////                    context.status()
////                    context.json(Response("", "", 0, ""))
////                }
////            }
////            And("""the service store is accessed as well""") {
////                verify(exactly = 1) {
////                    service.handle(any<FindTheDentistQuery>())
////                }
////            }
////        }
//
//        Scenario("handling ${FindTheDentistQuery::class.java} successfully") {
//            lateinit var request: FindTheDentistRequest
//            lateinit var query: FindTheDentistQuery
//            lateinit var result: QueryResult<Person>
//            lateinit var service: PersonQueryHandlerService
//            lateinit var context: Context
//            lateinit var controller: PersonQueryController
//            Given("""a ${FindTheDentistRequest::class.java} successfully instantiated""") {
//                request = PersonRequestBuilder.buildFindTheDentistRequest()
//            }
//            And("""a ${FindTheDentistQuery::class.java} successfully generated""") {
//                query = request.toType() as FindTheDentistQuery
//            }
//            And("""a ${QueryResult::class.java} successfully instantiated""") {
//                result = QueryResult.build(Optional.of(PersonBuilder.buildDentist()))
//            }
//            And("""a ${PersonQueryHandlerService::class.java} mock""") {
//                service = mockk()
//            }
//            And("""service#handle returning the ${QueryResult::class.java}""") {
//                every { service.handle(any<FindTheDentistQuery>()) } returns result
//            }
//            And("""a ${Context::class.java} mock""") {
//                context = ContextBuilder().mockContext("dentist_id")
//            }
//            And("""${JavalinJackson::class.java} is properly configured""") {
//                JavalinJackson.configure(JacksonObjectMapperFactory.build())
//            }
//            And("""context#fullUrl returns fullUrl""") {
//                every { context.fullUrl() } returns "fullUrl"
//            }
//            And("""context#status returns ${Context::class.java}""") {
//                every { context.status(STATUS) } returns context
//            }
//            And("""context#status returns $STATUS""") {
//                every { context.status() } returns STATUS
//            }
//            And("""context#json returns ${Context::class.java}""") {
//                every { context.json(Response("", "", 0, "")) } returns context
//            }
//            And("""a ${PersonQueryController::class.java} successfully instantiated""") {
//                controller = PersonQueryController(service)
//            }
//            When("""#findTheDentist is executed""") {
//                controller.findTheDentist(context)
//            }
//            Then("""status is $STATUS""") {
//                assertThat(context.status()).isEqualTo(STATUS)
//            }
//            And("""the context is accessed in the right order""") {
//                verifyOrder {
//                    context.getRequest(::FindTheDentistRequest)
//                    context.fullUrl()
//                    context.status(STATUS)
//                    context.status()
//                    context.json(Response("", "", 0, ""))
//                }
//            }
//            And("""the service store is accessed as well""") {
//                verify(exactly = 1) {
//                    service.handle(any<FindTheDentistQuery>())
//                }
//            }
//        }
//    }
//})
