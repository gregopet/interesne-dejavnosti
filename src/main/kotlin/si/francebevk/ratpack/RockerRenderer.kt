package si.francebevk.ratpack

import com.fizzed.rocker.RenderingException
import com.fizzed.rocker.runtime.ArrayOfByteArraysOutput
import com.fizzed.rocker.runtime.DefaultRockerModel
import io.netty.buffer.Unpooled
import ratpack.handling.Context
import ratpack.render.RenderException
import ratpack.render.RendererSupport

/** Renders Ratpack templates */
object RockerRenderer : RendererSupport<DefaultRockerModel>() {

    class RocketRendererRenderException(str: String?, throwable: Throwable): RenderException(str, throwable)

    // Code copied from https://github.com/fizzed/rocker/blob/master/rocker-test-java8/src/test/java/com/fizzed/rocker/bin/NettyMain.java
    override fun render(ctx: Context, t: DefaultRockerModel) {
        ctx.response.contentTypeIfNotSet("text/html")
        try {
            val out = t.render()
            val buf = if (out is ArrayOfByteArraysOutput) {
                Unpooled.wrappedBuffer(*out.arrays.toTypedArray())
            } else {
                // save to assume output is just a string w/ charset
                Unpooled.copiedBuffer(out.toString(), out.charset)
            }
            ctx.response.send(buf)
        } catch (e: RenderingException) {
            throw RocketRendererRenderException(e.message, e)
        }
    }
}
