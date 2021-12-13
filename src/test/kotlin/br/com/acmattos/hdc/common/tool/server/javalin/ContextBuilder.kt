package br.com.acmattos.hdc.common.tool.server.javalin

import io.javalin.http.Context
import io.javalin.http.util.ContextUtil
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import java.io.InputStream
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author ACMattos
 * @since 27/11/2021.
 */
class ContextBuilder {
    val req = mockk<HttpServletRequest>()
    val res = mockk<HttpServletResponse>()

    fun mockContext(matchedPath: String, jsonBody: String = ""): Context {
        val context: Context = ContextUtil.init(
            req,
            res,
            matchedPath,
            mapOf(matchedPath to "01FJJDJKDXN4K558FMCKEMQE6B")
        )
        every { req.getHeader(any<String>()) } returns "application\\json"
        every { req.inputStream } returns DelegatingServletInputStream(
            jsonBody.byteInputStream()
        )
        return context
    }
}

/**
 * @author ACMattos
 * @since 27/11/2021.
 */
class DelegatingServletInputStream(
    sourceStream: InputStream
): ServletInputStream() {
    /**
     * Return the underlying source stream (never `null`).
     */
    private val sourceStream: InputStream

    /**
     * Create a DelegatingServletInputStream for the given source stream.
     * sourceStream the source stream (never `null`)
     */
    init {
        this.sourceStream = sourceStream
    }

    @Throws(IOException::class)
    override fun read(): Int {
        return sourceStream.read()
    }

    override fun isFinished(): Boolean {
        return true
    }

    override fun isReady(): Boolean {
        return true
    }

    override fun setReadListener(readListener: ReadListener?) {
    }

    @Throws(IOException::class)
    override fun close() {
        super.close()
        sourceStream.close()
    }
}
