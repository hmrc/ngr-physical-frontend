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
import org.scalatest.matchers.must.Matchers
import play.api.libs.json._

class UserTypeSpec extends AnyWordSpec with Matchers {

  "UserType" should {

    "serialize to JSON correctly" in {
      Json.toJson(UserType.Individual) mustBe JsString("Individual")
      Json.toJson(UserType.Organisation) mustBe JsString("Organisation")
    }

    "deserialize from JSON correctly" in {
      Json.fromJson[UserType](JsString("Individual")).get mustBe UserType.Individual
      Json.fromJson[UserType](JsString("Organisation")).get mustBe UserType.Organisation
    }
  }
}