package com.twisted.lolmatches.riot

import com.twisted.lolmatches.dto.ListRegions
import net.rithms.riot.api.ApiConfig
import net.rithms.riot.api.RiotApi
import net.rithms.riot.constant.Platform
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class RiotService {
  private val rest = RestTemplate()
  private val apiKey = System.getenv("API_KEY") ?: ""

  fun parseRegion(value: ListRegions): Platform {
    var region: Platform
    when (value) {
      ListRegions.LA1 -> region = Platform.LAN
      else -> {
        throw Exception("Invalid region $value")
      }
    }
    return region
  }

  fun getApi(): RiotApi {
    val config = ApiConfig().setKey(apiKey)
    return RiotApi(config)
  }
}