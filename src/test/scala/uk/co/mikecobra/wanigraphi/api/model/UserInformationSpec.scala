package uk.co.mikecobra.wanigraphi.api.model

import java.time.{LocalDateTime, ZoneOffset}

import io.circe.parser._
import org.specs2.mutable.Specification

class UserInformationSpec extends Specification {
  val userInformationJson: String = """
    |{
    |    "username": "MikeCobra",
    |    "gravatar": "6f072b92be8107f47522f2e8c359c7b0",
    |    "level": 14,
    |    "title": "Turtles",
    |    "about": "",
    |    "website": null,
    |    "twitter": null,
    |    "topics_count": 0,
    |    "posts_count": 0,
    |    "creation_date": 1480679357,
    |    "vacation_date": 1480643132
    |  }
  """.stripMargin

  "UserInformation" >> {
    "should parse JSON correctly" >> {
      val expected = UserInformation(
        "MikeCobra",
        "6f072b92be8107f47522f2e8c359c7b0",
        14,
        "Turtles",
        "",
        None,
        None,
        0,
        0,
        LocalDateTime.ofEpochSecond(1480679357, 0, ZoneOffset.UTC),
        Some(LocalDateTime.ofEpochSecond(1480643132, 0, ZoneOffset.UTC))
      )

      val result = decode[UserInformation](userInformationJson)

      result shouldEqual Right(expected)
    }
  }
}
