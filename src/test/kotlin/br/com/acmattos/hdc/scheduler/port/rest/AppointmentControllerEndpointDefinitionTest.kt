//package br.com.acmattos.hdc.scheduler.port.rest
//
//import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
//import br.com.acmattos.hdc.scheduler.config.AED
//import br.com.acmattos.hdc.scheduler.config.AppointmentKoinComponent
//import br.com.acmattos.hdc.scheduler.config.ScheduleKoinComponent
//import io.javalin.Javalin
//import io.javalin.apibuilder.ApiBuilder
//import org.koin.core.Koin
//import org.koin.core.qualifier.named
//import org.koin.dsl.koinApplication
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.style.gherkin.Feature
//
///**
// * @author ACMattos
// * @since 27/11/2021.
// */
//object AppointmentControllerEndpointDefinitionTest: FreeSpec({
//    "Feature: ${AppointmentControllerEndpointDefinition::class.java} existence") {
//        "Scenario: endpoint definition exists") {
//            lateinit var koin: Koin
//            lateinit var endpoint: EndpointDefinition
//            "Given: a Koin Application properly instantiated" {
//                koin = koinApplication {
//                    modules(AppointmentKoinComponent.loadModule())
//                    modules(ScheduleKoinComponent.loadModule())// TODO analyse dependency
//                }.koin
//            }
//            "And: a javalin server is configured" {
//                ApiBuilder.setStaticJavalin(Javalin.create())
//            }
//            "When: a the endpoint definition is injected" {
//                endpoint = koin.get(named(AED))
//            }
//            "Then: #routes can be called" {
//                endpoint.routes()
//            }
//        }
//    }
//})
