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

import org.scalatestplus.play.PlaySpec
import play.api.libs.json._
import models.registration.AgentStatus

class AgentStatusSpec extends PlaySpec {

  "AgentStatus" should {

    "serialize to JSON correctly" in {
      Json.toJson(AgentStatus.Agent) mustBe JsString("Agent")
      Json.toJson(AgentStatus.Autonomous) mustBe JsString("Autonomous")
    }

    "deserialize from JSON correctly" in {
      Json.fromJson[AgentStatus](JsString("Agent")).get mustBe AgentStatus.Agent
      Json.fromJson[AgentStatus](JsString("Autonomous")).get mustBe AgentStatus.Autonomous
    }
    
    "fail to deserialize invalid JSON" in {
      val result = Json.fromJson[AgentStatus](JsString("InvalidStatus"))
      result.isError mustBe true
    }

    "contain all expected values" in {
      AgentStatus.values must contain allOf (AgentStatus.Agent, AgentStatus.Autonomous)
      AgentStatus.values.size mustBe 2
    }
  }
}