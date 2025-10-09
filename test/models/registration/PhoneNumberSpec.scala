/*
 * Copyright 2024 HM Revenue & Customs
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
import org.scalatest.matchers.must.Matchers
import play.api.libs.json._

class PhoneNumberSpec extends AnyWordSpec with Matchers {

  "PhoneNumber" should {

    "serialize to JSON correctly" in {
      val phone = PhoneNumber("07700900123")
      val json = Json.toJson(phone)
      json mustBe Json.obj("value" -> "07700900123")
    }

    "deserialize from JSON correctly" in {
      val json = Json.obj("value" -> "02079460000")
      val result = json.as[PhoneNumber]
      result mustBe PhoneNumber("02079460000")
    }
    
    "return correct value from .value method" in {
      val phone = PhoneNumber("03001234567")
      phone.value mustBe "03001234567"
    }
  }
}