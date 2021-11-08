package br.com.acmattos.hdc.person.config

import br.com.acmattos.hdc.scheduler.config.assertDefinitionsCount
import org.assertj.core.api.Assertions.assertThat
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val NUMBER_OF_DEPENDENCIES = 23

/**
 * @author ACMattos
 * @since 05/10/2021.
 */
object PersonKoinComponentTest: Spek({
    Feature("${PersonKoinComponent::class.java} usage") {
        Scenario("assertion succeed") {
            lateinit var koin: Koin
            lateinit var application: KoinApplication
            Given("""a Koin Application properly instantiated""") {
                application = koinApplication {
                    modules(PersonKoinComponent.loadModule())
                }
            }
            When("""a Koin instantiated successfully""") {
                koin = application.koin
            }
            Then("""the number of modules loaded is equal to $NUMBER_OF_DEPENDENCIES""") {
                application.assertDefinitionsCount(NUMBER_OF_DEPENDENCIES)
            }
            And("""koin is not null""") {
                assertThat(koin).isNotNull()
            }
        }
    }
})
