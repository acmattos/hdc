package br.com.acmattos.hdc.common.tool.http

import br.com.acmattos.hdc.common.tool.config.PropHandler.getProperty
import br.com.acmattos.hdc.common.tool.server.javalin.Response
import java.util.concurrent.TimeUnit.SECONDS
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * @author ACMattos
 * @since 06/11/2021.
 */
class Http(private val urlProperty: String) {
    val client: Retrofit = Retrofit.Builder()
        .baseUrl(getProperty<String>(this.urlProperty))
        .client(
            Builder()
                .connectTimeout(
                    getProperty<Long>(
                        "HTTP_CLIENT_CONNECT_TIMEOUT",
                        60
                    ),
                    SECONDS
                )
                .readTimeout(
                    getProperty<Long>(
                        "HTTP_CLIENT_READ_TIMEOUT",
                        30
                    ),
                    SECONDS
                )
                .writeTimeout(
                    getProperty<Long>(
                        "HTTP_CLIENT_WRITE_TIMEOUT",
                        15
                    ),
                    SECONDS
                )
                .build()
        )
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
}

/**
 * @author ACMattos
 * @since 07/11/2021.
 */
fun Response.isEmpty(): Boolean =
    when(data) {
        is Map<*,*> -> (data["results"] as List<*>).isEmpty()
        else -> false
    }

/**
 * @author ACMattos
 * @since 07/11/2021.
 */
fun Response.toResult(): Map<String, *> =
    ((this.data as LinkedHashMap<String,*>)
        .values.first() as List<Map<String, *>>).first()


/**
 * @author ACMattos
 * @since 07/11/2021.
 */
fun <T> Response.toId(key: String, converter: (String) -> T): T =
    converter(
        (toResult()[key] as Map<String, String>)["id"]!!
    )