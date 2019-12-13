package com.twisted.lolmatches.riot

import com.twisted.lolmatches.summoners.dto.ListRegions
import net.rithms.riot.api.ApiConfig
import net.rithms.riot.api.RiotApi
import net.rithms.riot.constant.Platform
import org.springframework.stereotype.Component

@Component
class RiotService {
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

  fun getApi() =
          RiotApi(ApiConfig().setKey(apiKey))
}