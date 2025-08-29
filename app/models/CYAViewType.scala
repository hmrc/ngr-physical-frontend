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

import play.api.mvc.{JavascriptLiteral, QueryStringBindable}

sealed trait CYAViewType
case object CYAInternal extends CYAViewType
case object CYAExternal extends CYAViewType

object CYAViewType {
  implicit val jsLiteral: JavascriptLiteral[CYAViewType] = {
    case CYAInternal => "CYAInternal"
    case CYAExternal => "CYAExternal"
  }
  def titleKey(viewType: CYAViewType): String = viewType match {
    case CYAInternal => "internalCheckYourAnswers.title"
    case CYAExternal => "externalCheckYourAnswers.title"
  }
  def anotherKey(viewType: CYAViewType): String = viewType match {
    case CYAInternal => "internalCheckYourAnswers.another"
    case CYAExternal => "externalCheckYourAnswers.another"
  }


  implicit def queryStringBindable(implicit stringBinder: QueryStringBindable[String]): QueryStringBindable[CYAViewType] =
    new QueryStringBindable[CYAViewType] {
      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, CYAViewType]] =
        stringBinder.bind(key, params).map {
          case Right("CYAInternal") => Right(CYAInternal)
          case Right("CYAExternal") => Right(CYAExternal)
          case Right(other) => Left(s"Unknown CYAViewType: $other")
          case Left(error) => Left(error)
        }

      override def unbind(key: String, value: CYAViewType): String = {
        val stringValue = value match {
          case CYAInternal => "CYAInternal"
          case CYAExternal => "CYAExternal"
        }
        stringBinder.unbind(key, stringValue)
      }
    }

}
