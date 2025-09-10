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

package forms.mappings

import play.api.data.FormError
import play.api.data.format.Formatter
import play.api.i18n.Messages
import java.time.{LocalDate, Month}
import scala.util.{Failure, Success, Try}

private[mappings] class LocalDateFormatter(
                                            invalidKey: String,
                                            allRequiredKey: String,
                                            twoRequiredKey: String,
                                            requiredKey: String,
                                            args: Seq[String] = Seq.empty
                                          )(implicit messages: Messages) extends Formatter[LocalDate] with Formatters {

  private val fieldKeys: List[String] = List("day", "month", "year")

  private def toDate(key: String, day: Int, month: Int, year: Int): Either[Seq[FormError], LocalDate] = {
    def isLeapYear(y: Int): Boolean = java.time.Year.isLeap(y)

    def maxDayInMonth(m: Int, y: Int): Option[Int] = m match {
      case 1 | 3 | 5 | 7 | 8 | 10 | 12 => Some(31)
      case 4 | 6 | 9 | 11             => Some(30)
      case 2                          => Some(if (isLeapYear(y)) 29 else 28)
      case _                          => None
    }

    Try(LocalDate.of(year, month, day)) match {
      case Success(date) => Right(date)
      case Failure(_) =>
        val invalidFieldKey = {
          if (month < 1 || month > 12) s"$key.month"
          else if (year < 1900 || year > 9999) s"$key.year"
          else maxDayInMonth(month, year) match {
            case Some(maxDay) if day < 1 || day > maxDay => s"$key.day"
            case None => s"$key.month"
            case _ => key
          }
        }
        Left(Seq(FormError(invalidFieldKey, invalidKey, args)))
    }
  }


  private def formatDate(key: String, data: Map[String, String]): Either[Seq[FormError], LocalDate] = {

    val int = intFormatter(
      requiredKey = invalidKey,
      wholeNumberKey = invalidKey,
      nonNumericKey = invalidKey,
      args
    )

    val month = new MonthFormatter(invalidKey, args)

    for {
      day   <- int.bind(s"$key.day", data)
      month <- month.bind(s"$key.month", data)
      year  <- int.bind(s"$key.year", data)
      date  <- toDate(key, day, month, year)
    } yield date
  }

  override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], LocalDate] = {
    val fields: Map[String, Option[String]] = fieldKeys.map { field =>
      field -> data.get(s"$key.$field").filter(_.nonEmpty)
    }.toMap

    val missingFieldKeys = fields.collect { case (field, None) => field }.toSeq
    val missingMessages = missingFieldKeys.map(field => messages(s"date.error.$field"))

    fields.count(_._2.isDefined) match {
      case 3 =>
        formatDate(key, data)

      case 2 =>
        val firstMissing = missingFieldKeys.head
        Left(List(FormError(s"$key.$firstMissing", requiredKey, missingMessages ++ args)))

      case 1 =>
        val firstError = FormError(s"$key.${missingFieldKeys.head}", twoRequiredKey, missingMessages ++ args)
        val secondErrorOpt = missingFieldKeys.lift(1).map { secondMissing =>
          FormError(s"$key.$secondMissing", "", args)
        }
        Left(firstError :: secondErrorOpt.toList)

      case _ =>
        Left(List(FormError(key, allRequiredKey, args)))
    }
  }


  override def unbind(key: String, value: LocalDate): Map[String, String] =
    Map(
      s"$key.day" -> value.getDayOfMonth.toString,
      s"$key.month" -> value.getMonthValue.toString,
      s"$key.year" -> value.getYear.toString
    )
}

private class MonthFormatter(invalidKey: String, args: Seq[String] = Seq.empty) extends Formatter[Int] with Formatters {

  private val baseFormatter = stringFormatter(invalidKey, args)

  override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Int] = {

    val months = Month.values.toList

    baseFormatter
      .bind(key, data)
      .flatMap {
        str =>
          months
            .find(m => m.getValue.toString == str.replaceAll("^0+", "") || m.toString == str.toUpperCase || m.toString.take(3) == str.toUpperCase)
            .map(x => Right(x.getValue))
            .getOrElse(Left(List(FormError(key, invalidKey, args))))
      }
  }

  override def unbind(key: String, value: Int): Map[String, String] =
    Map(key -> value.toString)
}
