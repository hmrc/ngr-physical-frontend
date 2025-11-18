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

package pages

import helpers.TestData
import models.UseOfSpaces.Rearrangedtheuseofspace
import models.{ChangeToUseOfSpace, UserAnswers}
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.must.Matchers
import org.scalatest.{OptionValues, TryValues}

class HaveYouChangedSpacePageSpec extends AnyFreeSpec with Matchers with TestData {

  "HaveYouChangedSpacePageSpec" - {
    "cleanup" - {
      "must clear ChangeToUseOfSpacePage data when the flag is changed from 'yes' to 'No'" in {

        val result = UserAnswers("id")
          .set(HaveYouChangedSpacePage(assessmentId), true)
          .success
          .value
          .set(ChangeToUseOfSpacePage(assessmentId), ChangeToUseOfSpace(Set(Rearrangedtheuseofspace), true, Some("details")))
          .success
          .value
          .set(HaveYouChangedSpacePage(assessmentId), false)
          .success
          .value

        result.get(ChangeToUseOfSpacePage(assessmentId)) must not be defined
      }

      "must retain ChangeToUseOfSpacePage data when the flag is changed from 'No' to 'Yes'" in {

        val result = UserAnswers("id")
          .set(HaveYouChangedSpacePage(assessmentId), false)
          .success
          .value
          .set(HaveYouChangedSpacePage(assessmentId), true)
          .success
          .value
          .set(ChangeToUseOfSpacePage(assessmentId), ChangeToUseOfSpace(Set(Rearrangedtheuseofspace), true, Some("details")))
          .success
          .value

        result.get(ChangeToUseOfSpacePage(assessmentId)) mustBe defined
      }
    }
  }
}
