package br.com.acmattos.hdc.common.tool.config

import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author ACMattos
 * @since 09/06/2020.
 */
object PropHandlerTest: Spek({
    Feature("""${PropHandler::class.java} usage""") {
        Scenario("""valid key given to #getProperty""") {
            lateinit var key: String
            lateinit var value: String
            lateinit var result: String

            Given("""a valid key named 'key'""") {
                key = "key"
            }
            And("""a valid value containing 'value'""") {
                value = "value"
            }
            And("""a System Property defined""") {
                System.setProperty(key, value)
            }
            When("""a #getProperty(key) is requested""") {
                result = PropHandler.getProperty(key)
            }
            Then("""the property value is retrieved as expected""") {
                Assertions.assertThat(result).isEqualTo(value)
            }
        }

        Scenario("""invalid key given to #getProperty""") {
            val message = "Property 'invalidkey' is missing!"
            lateinit var key: String
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>

            Given("""an invalid key""") {
                key = "invalidkey"
            }
            When("""a #getProperty(key) is requested""") {
                assertion = Assertions.assertThatCode {
                    PropHandler.getProperty<String>(key)
                }
            }
            Then("""#getProperty(key) throws exception""") {
                assertion.hasSameClassAs(IllegalArgumentException(message))
            }
            And("""exception has message '$message'""") {
                assertion.hasMessage(message)
            }
        }

        Scenario("""valid key given to #getProperty, no defaultValue usage""") {
            lateinit var key: String
            var value = 0
            var defaultValue = 0
            var result = 0

            Given("""a valid key named 'key'""") {
                key = "key"
            }
            And("""a valid value containing 'value'""") {
                value = 100
            }
            And("""a valid default value containing 'defaultValue'""") {
                defaultValue = 200
            }
            And("""a System Property defined""") {
                System.setProperty(key, value.toString())
            }
            When("""a #getProperty(key, defaultValue) is requested""") {
                result = PropHandler.getProperty(key, defaultValue)
            }
            Then("""the property value is retrieved as expected""") {
                Assertions.assertThat(result).isEqualTo(value)
            }
            And("""the property value is not defaultValue""") {
                Assertions.assertThat(result).isNotEqualTo(defaultValue)
            }
        }
        ////
        Scenario("""invalid key given to #getProperty, defaultValue usage""") {
            lateinit var key: String
            var defaultValue = 0
            var result = 0

            Given("""an invalid key named 'invalidkey'""") {
                key = "invalidkey"
            }
            And("""a valid default value containing '10'""") {
                defaultValue = 10
            }
            When("""a #getProperty(key, defaultValue) is requested""") {
                result = PropHandler.getProperty(key, defaultValue)
            }
            Then("""the property value is not retrieved as expected""") {
                Assertions.assertThat(result).isEqualTo(defaultValue)
            }
        }
    }
})