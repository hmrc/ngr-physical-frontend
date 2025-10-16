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

package models

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import play.api.mvc.JavascriptLiteral

class CYAViewTypeSpec extends AnyWordSpec with Matchers {

  "CYAViewType.jsLiteral" should {
    "convert CYAInternal to 'CYAInternal'" in {
      CYAViewType.jsLiteral.to(CYAInternal) shouldBe "CYAInternal"
    }

    "convert CYAExternal to 'CYAExternal'" in {
      CYAViewType.jsLiteral.to(CYAExternal) shouldBe "CYAExternal"
    }
  }
}