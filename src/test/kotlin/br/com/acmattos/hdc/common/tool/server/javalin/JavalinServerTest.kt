package br.com.acmattos.hdc.common.tool.server.javalin

import br.com.acmattos.hdc.common.tool.HttpClient
import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.common.tool.exception.InternalServerErrorException
import com.github.kittinunf.fuel.core.Response
import io.javalin.apibuilder.ApiBuilder
import io.javalin.plugin.openapi.annotations.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.jetty.http.HttpStatus
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * @author ACMattos
 * @since 09/06/2020.
 */
object JavalinServerTest : Spek({
    Feature("${JavalinServer::class.java} usage") {
        var server: JavalinServer? = null
        var port = 7001
        lateinit var getRoute: (() -> Unit)
        lateinit var response: Response

        afterEachScenario { server?.stop() }
        Scenario("Javalin Server GET access is ${HttpStatus.OK_200}") {
            val uri = "/found"

            Given("""a HTTP Server GET route defined""") {
                getRoute = {
                    ApiBuilder.get(uri) { context ->
                        context.result("found")
                    }
                }
            }
            And("""an instance of ${JavalinServer::class.java} instantiated""" ) {
                server = JavalinServerBuilder.routes { getRoute() }.port(port).build()
            }
            When("""a HTTP GET connection to a valid resource is done to ${JavalinServer::class.java}""") {
                response = HttpClient.port(port).get(uri)
            }
            Then("""response status is ${HttpStatus.OK_200}""") {
                assertThat(response.statusCode).isEqualTo(HttpStatus.OK_200)
            }
            And("""the response body is equal to 'found'"""){
                assertThat(response.body().asString(ContentType.JSON)).isEqualTo("found")
            }
        }

        Scenario("Javalin Server GET access is ${HttpStatus.INTERNAL_SERVER_ERROR_500}") {
            val uri = "/internal-server-error"

            Given("""a HTTP Server GET route is defined but throws ${InternalServerErrorException::class.java}""") {
                getRoute = {
                    ApiBuilder.get(uri) {
                        throw InternalServerErrorException("Condition not met!", MessageTrackerCodeBuilder.build().messageTrackerId, Exception()) // TODO FIND CODE
                    }
                }
            }
            And("""an instance of ${JavalinServer::class.java} instantiated""") {
                server = JavalinServerBuilder.routes { getRoute() }.port(++port).build()
            }
            When("""a HTTP GET connection to a valid resource is done to ${JavalinServer::class.java}""") {
                response = HttpClient.port(port).get(uri)
            }
            Then("""response status is ${HttpStatus.INTERNAL_SERVER_ERROR_500}""") {
                assertThat(response.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR_500)
            }
            And("response body contains 'Condition not met!'") {
                assertThat(response.body().asString("text/json"))
                    .contains("\"code\":\"01FVPVJN7G52MV85FW4ZTYTE6F\",\"status\":500,\"data\":\"Condition not met!\"")
            }
        }

        Scenario("Javalin Server GET access is ${HttpStatus.NOT_FOUND_404}") {
            val uri = "/not-found"

            Given("""a HTTP Server GET route is not defined""") {
                getRoute = {}
            }
            And("""an instance of ${JavalinServer::class.java} instantiated and started""") {
                server = JavalinServerBuilder.routes { getRoute() }.port(++port).build()
            }
            When("""a HTTP GET connection to a invalid resource is done to ${JavalinServer::class.java}""") {
                response = HttpClient.port(port).get(uri)
            }
            Then("""response status is ${HttpStatus.NOT_FOUND_404}""") {
                assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND_404)
            }
            And("response body contains 'Not found'") {
                assertThat(response.body().asString("text/json"))
                    .contains("\"code\":\"01FVPVJN7G52MV85FW4ZTYTE6F\",\"status\":404,\"data\":\"Not found")
            }
        }

        Scenario("Javalin Server GET access is ${HttpStatus.BAD_REQUEST_400}") {
            val uri = "/bad-request"

            Given("""a HTTP Server GET route is defined but throws ${AssertionFailedException::class.java}""") {
                getRoute = {
                    ApiBuilder.get(uri) {
                        throw AssertionFailedException("Condition not met!", MessageTrackerCodeBuilder.build().messageTrackerId) // TODO FIND CODE
                    }
                }
            }
            And("""an instance of ${JavalinServer::class.java} instantiated""") {
                server = JavalinServerBuilder.routes { getRoute() }.port(++port).build()
            }
            When("""a HTTP GET connection to a valid resource is done to ${JavalinServer::class.java}""") {
                response = HttpClient.port(port).get(uri)
            }
            Then("""response status is ${HttpStatus.BAD_REQUEST_400}""") {
                assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST_400)
            }
            And("response body contains 'Condition not met!'") {
                assertThat(response.body().asString("text/json"))
                    .contains("\"code\":\"01FVPVJN7G52MV85FW4ZTYTE6F\",\"status\":400,\"data\":\"Condition not met!\"}")
            }
        }
// TODO Fix OpenApiPlugin
//        Scenario("Javalin Server GET access to 'health-check' path is ${HttpStatus.OK_200}") {
//            val uri = "/health-check"
//
//            Given("""an instance of ${Javalin::class.java} fully configured""") {
//                server = JavalinServerBuilder.port(++port).build()
//            }
//            When("""a HTTP GET connection to 'health-check' resource is done to ${JavalinServer::class.java}""") {
//                response = HttpClient.port(port).get(uri)
//            }
//            Then("""response status is ${HttpStatus.OK_200}""") {
//                assertThat(response.statusCode).isEqualTo(HttpStatus.OK_200)
//            }
//            And("""the response body contains 'version'"""){
//                assertThat(String(response.data)).contains("version")
//            }
//        }
    }
})