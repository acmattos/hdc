package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.context.config.ContextLogEnum.ENTITY
import br.com.acmattos.hdc.common.context.domain.cqs.Event
import br.com.acmattos.hdc.common.tool.loggable.Loggable

/**
 * @author ACMattos
 * @since 27/07/2019.
 */
interface Entity

/**
 * @author ACMattos
 * @since 04/01/2023.
 */
interface AppliableEntity: Entity {
    fun apply(events: List<Event>): Entity {
        for (event in events) {
            apply(event)
            logger.trace(
                "[{} {}] - Event [{}] applied to Entity: -> {} <-",
                this.javaClass.simpleName.toUpperCase(),
                ENTITY.name,
                event.javaClass.simpleName,
                this.toString()
            )
        }
        return this
    }

    fun apply(event: Event): Entity

    companion object: Loggable()
}

/**
 * @author ACMattos
 * @since 28/06/2020.
 */
interface ValueObject
