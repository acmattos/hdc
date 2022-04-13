package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.context.domain.cqs.Filter
import br.com.acmattos.hdc.common.tool.page.Page
import br.com.acmattos.hdc.common.tool.page.PageResult
import java.util.Optional

/**
 * @author ACMattos
 * @since 25/07/2019.
 */
interface Repository<T> {
    fun save(t: T)
    fun update(filter: Filter<*>, t: T)
    fun delete(filter: Filter<*>)
    fun findOneByFilter(filter: Filter<*>): Optional<T>
    fun findAllByFilter(filter: Filter<*>): List<T>
    @Deprecated("Use #findAllByPage method")
    fun findAll(): List<T>// TODO Subject to change (APPLY a filter)
    fun findAllByPage(page: Page): PageResult<T>
}
