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

package utils

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers.mustBe

class StringUtilsSpec extends AnyFreeSpec {

  "StringUtils" - {
    "hyphenToCamelCase" - {
      "must convert hyphenated strings to camelCase" in {
        StringUtils.hyphenToCamelCase("sure-want-remove-change") mustBe "sureWantRemoveChange"
        StringUtils.hyphenToCamelCase("example-string-test") mustBe "exampleStringTest"
        StringUtils.hyphenToCamelCase("singleword") mustBe "singleword"
        StringUtils.hyphenToCamelCase("") mustBe ""
      }
    }

    "camelCaseToHyphen" - {
      "must convert camelCase strings to hyphenated" in {
        StringUtils.camelCaseToHyphen("sureWantRemoveChange") mustBe "sure-want-remove-change"
        StringUtils.camelCaseToHyphen("exampleStringTest") mustBe "example-string-test"
        StringUtils.camelCaseToHyphen("singleword") mustBe "singleword"
        StringUtils.camelCaseToHyphen("") mustBe ""
      }
    }
  }

}
