package uk.co.mikecobra.wanigraphi.api.wanikani.model

import io.circe.Decoder

case class ApiResponse[A](userInformation: UserInformation, requestedInformation: A)

object ApiResponse {
  implicit def decoder[A: Decoder]: Decoder[ApiResponse[A]] = Decoder.forProduct2(
    "user_information",
    "requested_information"
  ) { (
    userInformation: UserInformation,
    requestedInformation: A
  ) =>
    ApiResponse[A](
      userInformation,
      requestedInformation
    )
  }
}
