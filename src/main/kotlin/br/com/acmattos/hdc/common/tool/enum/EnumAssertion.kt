package br.com.acmattos.hdc.common.tool.enum

import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.server.javalin.ErrorTrackerCode

/**
 * @author ACMattos
 * @since 31/10/2019.
 */
inline fun <reified T: Enum<T>> assertThatTerm(
    term: String,
    assertionMessage: String,
    code: ErrorTrackerCode
): T {
    val element = enumValues<T>().firstOrNull { t ->
        term.toUpperCase() == t.name
    }
    Assertion.assert(assertionMessage, code) {
        element != null
    }
    return element!!
}
