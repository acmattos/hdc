//package br.com.acmattos.hdc.scheduler.config
//
//import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
//import br.com.acmattos.hdc.common.tool.assertDefinitionsCount
//import br.com.acmattos.hdc.scheduler.port.rest.ScheduleCommandController
//import org.assertj.core.api.Assertions.assertThat
//import org.koin.core.Koin
//import org.koin.core.KoinApplication
//import org.koin.core.qualifier.named
//import org.koin.dsl.koinApplication
//import org.spekframework.spek2.Spek
//import org.spekframework.spek2.style.gherkin.Feature
//
//private const val NUMBER_OF_DEPENDENCIES = 17
//private const val ENDPOINT_DEFINITION = "ScheduleCommandControllerEndpointDefinition"
//
///**
// * @author ACMattos
// * @since 23/09/2021.
// */
//object ScheduleKoinComponentTest: FreeSpec({
//    "Feature: ${ScheduleKoinComponent::class.java.simpleName} usage") {
//        "Scenario: assertion succeed") {
//            lateinit var koin: Koin
//            lateinit var application: KoinApplication
//            "Given: a Koin Application properly instantiated" {
//                application = koinApplication {
//                    modules(ScheduleKoinComponent.loadModule())
//                }
//            }
//            "When: a Koin instantiated successfully" {
//                koin =  application.koin
//            }
//            "Then: the number of modules loaded is equal to $NUMBER_OF_DEPENDENCIES" {
//                application.assertDefinitionsCount(NUMBER_OF_DEPENDENCIES)
//            }
//            "And: the $ENDPOINT_DEFINITION component was loaded") {
//                assertThat(
//                    koin.get<EndpointDefinition>(named(ENDPOINT_DEFINITION))
//                ).isNotNull()
//            }
//            "And: the ${ScheduleCommandController::class.java.simpleName} component was loaded" {
//                assertThat(koin.get<ScheduleCommandController>()).isNotNull()
//            }
//        }
//    }
//})
