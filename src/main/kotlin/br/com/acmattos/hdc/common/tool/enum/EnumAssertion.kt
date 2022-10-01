package br.com.acmattos.hdc.common.tool.enum

import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.server.javalin.MessageTracker

/**
 * @author ACMattos
 * @since 31/10/2019.
 */
inline fun <reified T: Enum<T>> assertThatTerm(
    term: String,
    assertionMessage: String,
    context: String,
    messageTracker: MessageTracker,
): T {
    val element = enumValues<T>().firstOrNull { t ->
        t.name == term.toUpperCase()
    }
    Assertion.assert(assertionMessage, context, messageTracker) {
        element != null
    }
    return element!!
}

/**
 * @author ACMattos
 * @since 29/09/2022.
 */
inline fun <reified T: Enum<T>> assertThatTermMatches(
    term: String,
    assertionMessage: String,
    context: String,
    messageTracker: MessageTracker,
    matches: (enumeration: T, term: String) -> Boolean
): T {
    val element = enumValues<T>().firstOrNull { t ->
        matches(t, term)
    }
    Assertion.assert(assertionMessage, context, messageTracker) {
        element != null
    }
    return element!!
}