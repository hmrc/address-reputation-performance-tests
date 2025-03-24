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

package uk.gov.hmrc.perftests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import play.api.libs.json.Json
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import uk.gov.hmrc.perftests.model.{Address, CacheAddress, CacheRequest, InsightsRequest}

object AddressReputationRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("address-reputation")
  val route: String   = "/address-reputation"

  val postInsights: HttpRequestBuilder = {

    val request = InsightsRequest(
      address = Address(
        addressLine1 = "${addressLine1}",
        addressLine2 = Some("${addressLine2}"),
        addressLine3 = Some("${addressLine3}"),
        addressLine4 = Some("${addressLine4}"),
        addressLine5 = Some("${addressLine5}"),
        postcode = Some("${postcode}"),
        uprn = Some("${uprn}"),
        country = "${country}"
      )
    )

    http("POST insights")
      .post(s"$baseUrl$route/reputation/sa-reg")
      .body(StringBody(Json.toJson(request).toString()))
      .asJson
      .check(status.is(200))
  }

  val postCache: HttpRequestBuilder = {
    val request = new CacheRequest(
      address = CacheAddress(
        addressLine1 = "${addressLine1}",
        addressLine2 = Some("${addressLine2}"),
        addressLine3 = Some("${addressLine3}"),
        addressLine4 = Some("${addressLine4}"),
        addressLine5 = Some("${addressLine5}"),
        uprn = Some("${uprn}"),
        postcode = Some("${postcode}"),
        country = "${country}",
        addressContext = Some("${addressContext}"),
        eventType = Some("${eventType}"),
        userId = Some("${userId}")
      ),
      caseId = "${caseId}"
    )

    http("POST cache")
      .post(s"$baseUrl$route/cache")
      .body(StringBody(Json.toJson(request).toString()))
      .asJson
      .check(status.is(204))
  }
}
