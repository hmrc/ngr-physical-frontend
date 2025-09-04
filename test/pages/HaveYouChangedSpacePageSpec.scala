package pages

import models.UseOfSpaces.Rearrangedtheuseofspace
import models.{ChangeToUseOfSpace, UserAnswers}
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.must.Matchers
import org.scalatest.{OptionValues, TryValues}

class HaveYouChangedSpacePageSpec extends AnyFreeSpec with Matchers {

  "HaveYouChangedSpacePageSpec" - {
    "cleanup" - {
      "must clear ChangeToUseOfSpacePage data when the flag is changed from 'yes' to 'No'" in {

        val result = UserAnswers("id")
          .set(HaveYouChangedSpacePage, true)
          .success
          .value
          .set(ChangeToUseOfSpacePage, ChangeToUseOfSpace(Set(Rearrangedtheuseofspace), true, Some("details")))
          .success
          .value
          .set(HaveYouChangedSpacePage, false)
          .success
          .value

        result.get(ChangeToUseOfSpacePage) must not be defined
      }

      "must retain ChangeToUseOfSpacePage data when the flag is changed from 'No' to 'Yes'" in {

        val result = UserAnswers("id")
          .set(HaveYouChangedSpacePage, false)
          .success
          .value
          .set(HaveYouChangedSpacePage, true)
          .success
          .value
          .set(ChangeToUseOfSpacePage, ChangeToUseOfSpace(Set(Rearrangedtheuseofspace), true, Some("details")))
          .success
          .value

        result.get(ChangeToUseOfSpacePage) mustBe defined
      }
    }
  }
}
