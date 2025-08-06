package forms

import forms.behaviours.OptionFieldBehaviours
import models.WhichInternalFeature
import play.api.data.FormError

class WhichInternalFeatureFormProviderSpec extends OptionFieldBehaviours {

  val form = new WhichInternalFeatureFormProvider()()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "whichInternalFeature.error.required"

    behave like optionsField[WhichInternalFeature](
      form,
      fieldName,
      validValues  = WhichInternalFeature.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
