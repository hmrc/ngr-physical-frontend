package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import play.api.libs.json.{JsError, JsString, Json}

class WhichInternalFeatureSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "WhichInternalFeature" - {

    "must deserialise valid values" in {

      val gen = Gen.oneOf(WhichInternalFeature.values.toSeq)

      forAll(gen) {
        whichInternalFeature =>

          JsString(whichInternalFeature.toString).validate[WhichInternalFeature].asOpt.value mustEqual whichInternalFeature
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!WhichInternalFeature.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[WhichInternalFeature] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = Gen.oneOf(WhichInternalFeature.values.toSeq)

      forAll(gen) {
        whichInternalFeature =>

          Json.toJson(whichInternalFeature) mustEqual JsString(whichInternalFeature.toString)
      }
    }
  }
}
