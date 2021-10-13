package br.com.acmattos.hdc.scheduler.config

import br.com.acmattos.hdc.common.context.port.rest.EndpointDefinition
import br.com.acmattos.hdc.scheduler.port.rest.ScheduleCommandController
import br.com.acmattos.hdc.scheduler.port.rest.ScheduleCommandControllerEndpointDefinition
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val ED = "ScheduleCommandControllerEndpointDefinition"

/**
 * @author ACMattos
 * @since 14/08/2019.
 */
object ScheduleKoinComponent: KoinComponent {
    fun loadModule() = module {
        // 1 - Endpoint Definition
        single<EndpointDefinition>(named(ED)) {
            ScheduleCommandControllerEndpointDefinition(get())
        }
        // 2 - Controller Endpoint
        single() {
            ScheduleCommandController()
        }
   }
}
