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

class AddressSpec extends AnyWordSpec with Matchers {


  "Address" should {

    "serialize to JSON correctly" in {
      val address = Address(
        line1 = "123 Main Street",
        line2 = Some("Apt 4B"),
        town = "London",
        county = Some("Greater London"),
        postcode = Postcode("SW1A 1AA")
      )

      val json = Json.toJson(address)
      val expectedJson = Json.parse(
        """{
          | "line1": "123 Main Street",
          | "line2": "Apt 4B",
          | "town": "London",
          | "county": "Greater London",
          | "postcode": { "value": "SW1A 1AA" }
          |}""".stripMargin
      )

      json shouldBe expectedJson
    }

    "deserialize from JSON correctly" in {
      val json = Json.parse(
        """{
          | "line1": "221B Baker Street",
          | "line2": null,
          | "town": "London",
          | "county": "Greater London",
          | "postcode": { "value": "NW1 6XE" }
          |}""".stripMargin
      )

      val address = json.as[Address]
      val expected = Address(
        line1 = "221B Baker Street",
        line2 = None,
        town = "London",
        county = Some("Greater London"),
        postcode = Postcode("NW1 6XE")
      )

      address shouldBe expected
    }

    "format to string correctly when all fields are present" in {
      val address = Address(
        line1 = "10 Downing Street",
        line2 = Some("Westminster"),
        town = "London",
        county = Some("Greater London"),
        postcode = Postcode("SW1A 2AA")
      )

      address.toString shouldBe "10 Downing Street, Westminster, London, Greater London, SW1A 2AA"
    }

    "format to string correctly when optional fields are missing" in {
      val address = Address(
        line1 = "1 Infinite Loop",
        line2 = None,
        town = "Cupertino",
        county = None,
        postcode = Postcode("95014")
      )

      address.toString shouldBe "1 Infinite Loop, , Cupertino, , 95014"
    }
  }
}