package uk.co.mikecobra.wanigraphi.api.config

import org.specs2.mutable.Specification

class WaniGraphiApiConfigTest  extends Specification {

  "WaniGraphiApiConfig" should {
    "return http port as given" in {
      val config = new WaniGraphiApiConfig()

      config.HttpConfig.port shouldEqual 1111
    }

    "return http host IP as given" in {
      val config = new WaniGraphiApiConfig()

      config.HttpConfig.hostIp shouldEqual "127.0.0.1"
    }
  }

}
