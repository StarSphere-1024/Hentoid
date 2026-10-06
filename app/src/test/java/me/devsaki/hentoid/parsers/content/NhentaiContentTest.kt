package me.devsaki.hentoid.parsers.content

import me.devsaki.hentoid.enums.StatusContent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import pl.droidsonroids.jspoon.Jspoon
import java.io.ByteArrayInputStream
import java.net.URL

class NhentaiContentTest {
    @Test
    fun parsesGalleryThumbnailsFromCurrentAndLegacyMarkup() {
        val imageMarkup = listOf(
            "src=\"https://t2.nhentai.net/galleries/123/1t.webp\"",
            "data-src=\"https://t2.nhentai.net/galleries/123/1t.webp\""
        )

        imageMarkup.forEach { imageAttribute ->
            val url = "https://nhentai.net/g/456/"
            val html = """
                <html><head><meta property="og:title" content="Gallery"></head><body>
                  <div id="bigcontainer">
                    <div id="cover"><a href="/g/456/"><img src="https://t2.nhentai.net/galleries/123/cover.webp"></a></div>
                  </div>
                  <div id="thumbnail-container">
                    <img $imageAttribute>
                    <img src="https://t2.nhentai.net/galleries/123/2t.webp">
                  </div>
                </body></html>
            """.trimIndent()
            val adapter = Jspoon.create().adapter(NhentaiContent::class.java)
            val content = adapter.fromInputStream(
                ByteArrayInputStream(html.toByteArray()),
                URL(url)
            ).toContent(url)

            assertNotEquals(StatusContent.IGNORED, content.status)
            assertEquals(2, content.qtyPages)
        }
    }
}
