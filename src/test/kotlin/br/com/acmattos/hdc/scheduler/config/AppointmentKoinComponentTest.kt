package br.com.acmattos.hdc.scheduler.config

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.scheduler.port.rest.ScheduleCommandController
import kotlin.test.assertEquals
import org.assertj.core.api.Assertions
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val NUMBER_OF_DEPENDENCIES = 32
private const val ENDPOINT_DEFINITION = "AppointmentControllerEndpointDefinition"

/**
 * @author ACMattos
 * @since 23/09/2021.
 */
object AppointmentKoinComponentTest: Spek({
    Feature("${AppointmentKoinComponent::class.java} usage") {
        Scenario("assertion succeed") {
            lateinit var koin: Koin
            lateinit var application: KoinApplication
            Given("""a Koin Application properly instantiated""") {
                application = koinApplication {
                    modules(AppointmentKoinComponent.loadModule())
                    modules(ScheduleKoinComponent.loadModule()) // TODO review dependency
                }
            }
            When("""a Koin instantiated successfully""") {
                koin =  application.koin
            }
            Then("""the number of modules loaded is equal to $NUMBER_OF_DEPENDENCIES""") {
                application.assertDefinitionsCount(NUMBER_OF_DEPENDENCIES)
            }
            And("the $ENDPOINT_DEFINITION component was loaded") {
                Assertions.assertThat(
                    koin.get<EndpointDefinition>(named(ENDPOINT_DEFINITION))
                ).isNotNull()
            }
            And("""the ${ScheduleCommandController::class.java} component was loaded""") {
                Assertions.assertThat(koin.get<ScheduleCommandController>()).isNotNull()
            }
        }
    }
})
