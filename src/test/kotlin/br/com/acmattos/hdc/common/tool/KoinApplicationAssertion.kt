@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package br.com.acmattos.hdc.common.tool

import org.junit.jupiter.api.Assertions.assertEquals
import org.koin.core.KoinApplication
import org.koin.core.annotation.KoinInternalApi

@OptIn(KoinInternalApi::class)
fun KoinApplication.assertDefinitionsCount(count: Int) {
    assertEquals(count, this.koin.instanceRegistry.size(), "definitions count")
}
