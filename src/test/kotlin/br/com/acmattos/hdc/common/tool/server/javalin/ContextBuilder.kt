package br.com.acmattos.hdc.common.tool.server.javalin

import io.javalin.http.Context
import io.javalin.http.util.ContextUtil
import io.mockk.mockk
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author ACMattos
 * @since 27/11/2021.
 */
object ContextBuilder {
    val req = mockk<HttpServletRequest>()
    val res = mockk<HttpServletResponse>()

    fun mockContext(machedPath: String): Context {
        val context: Context = ContextUtil.init(
            req,
            res,
            machedPath,
            mapOf(machedPath to "01FJJDJKDXN4K558FMCKEMQE6B")
        )

        return context
    }
}