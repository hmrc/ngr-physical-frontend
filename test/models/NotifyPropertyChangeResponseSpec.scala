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

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers.mustBe

class NotifyPropertyChangeResponseSpec extends AnyFreeSpec {
  "NotifyPropertyChangeResponse" - {
    "serialization and deserialization" - {
      import play.api.libs.json.{Json, JsString}

      "should deserialize JSON to NotifyPropertyChangeResponse" in {
        val json =  Json.arr(
            Json.obj(
              "code" -> "500",
              "reason" -> "Internal Server Error"
          )
        )
        val response = json.as[NotifyPropertyChangeResponse]
        val expectedResponse = NotifyPropertyChangeResponse(Some(Seq(ApiFailure("500", "Internal Server Error"))))
        response mustBe expectedResponse
      }
    }
  }

}
