package uk.co.mikecobra.wanigraphi.api.config

import com.typesafe.config.{Config, ConfigFactory}

class WaniGraphiApiConfig() {
  
  private val config: Config = ConfigFactory.load()

  object HttpConfig {
    private val httpConfig = config.getConfig("http")

    val hostIp: String = httpConfig.getString("host_ip")
    val port: Int = httpConfig.getInt("port")
  }

}
