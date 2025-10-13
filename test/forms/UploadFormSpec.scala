/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package forms

import play.api.data.FormError
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UploadFormSpec extends AnyWordSpec with Matchers {

  "UploadForm" should {

    "bind valid input" in {
      val form = new UploadForm().apply()
      val bound = form.bind(Map("file" -> "  hello\u0000 "))
      bound.value mustBe Some("hello")
    }

    "bind valid input directly" in {
      val formatter = new UploadForm().stringFormatter("uploadFile.error.noFileSelected")
      val result = formatter.bind("file", Map("file" -> "  hello\u0000 "))
      result mustBe Right("hello")
    }

    "bind empty input and return error" in {
      val form = new UploadForm().apply()
      val bound = form.bind(Map("file" -> "   \u0000 "))
      bound.errors.head.message mustBe "uploadFile.error.noFileSelected"
    }

    "bind empty input directly" in {
      val formatter = new UploadForm().stringFormatter("uploadFile.error.noFileSelected")
      val result = formatter.bind("file", Map("file" -> "   \u0000 "))
      result mustBe Left(Seq(FormError("file", "uploadFile.error.noFileSelected")))
    }

    "unbind value correctly" in {
      val form = new UploadForm().apply()
      val data = form.fill("  hello ").data
      data("file") mustBe "hello"
    }

    "return error when key is missing" in {
      val form = new UploadForm().apply()
      val bound = form.bind(Map.empty)
      bound.errors.head.message mustBe "uploadFile.error.noFileSelected"
    }
  }
}