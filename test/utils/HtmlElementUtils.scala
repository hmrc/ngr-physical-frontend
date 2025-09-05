package utils

import org.jsoup.nodes.Document

object HtmlElementUtils {
  
  def elementExists(selector: String)(document: Document): Boolean =
    !document.select(selector).isEmpty
}