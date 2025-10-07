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

package models.registration

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json._

class ReferenceTypeSpec extends AnyWordSpec with Matchers {

  "ReferenceType enum" should {

    "serialize to JSON correctly" in {
      Json.toJson(ReferenceType.TRN) shouldBe JsString("TRN")
      Json.toJson(ReferenceType.NINO) shouldBe JsString("NINO")
      Json.toJson(ReferenceType.SAUTR) shouldBe JsString("SAUTR")
    }

    "deserialize from JSON correctly" in {
      Json.parse("\"TRN\"").as[ReferenceType] shouldBe ReferenceType.TRN
      Json.parse("\"NINO\"").as[ReferenceType] shouldBe ReferenceType.NINO
      Json.parse("\"SAUTR\"").as[ReferenceType] shouldBe ReferenceType.SAUTR
    }


    "contain all expected values" in {
      ReferenceType.values should contain allOf (ReferenceType.TRN, ReferenceType.NINO, ReferenceType.SAUTR)
    }

  }
}