package br.com.acmattos.hdc.common.tool

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Response

object HttpClient {
    private const val SERVER_URL_PATTERN = "http://localhost"
    private var port: Int = 7000

    fun port(port: Int): HttpClient {
        this.port = port
        return this
    }

    fun get(uri: String): Response {
        val url = createURL(uri)
        val request = Fuel.get(url)
        return request.response().second
    }

    private fun createURL(uri: String): String {
        return "$SERVER_URL_PATTERN:$port$uri"
    }
}