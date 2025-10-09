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

import org.mongodb.scala.result
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.must.Matchers.*
import play.api.libs.json.*

class NinoSpec extends AnyWordSpec {

  "Nino" should {

    "serialize to JSON correctly" in {
      val nino = Nino("AB123456C")
      val json = Json.toJson(nino)
      json mustBe Json.obj("nino" -> "AB123456C")
    }

    "deserialize from JSON correctly" in {
      val json = Json.obj("nino" -> "CD987654E")
      val result = json.as[Nino]
      result mustBe Nino("CD987654E")
    }
    
    "return correct value from .value method" in {
      val nino = Nino("QQ123456A")
      nino.value mustBe "QQ123456A"
    }
  }
}