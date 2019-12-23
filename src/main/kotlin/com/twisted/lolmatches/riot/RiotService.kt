package com.twisted.lolmatches.riot

import com.twisted.enum.common.ListRegions
import net.rithms.riot.api.ApiConfig
import net.rithms.riot.api.RiotApi
import net.rithms.riot.api.endpoints.match.dto.MatchReference
import net.rithms.riot.api.request.ratelimit.RateLimitException
import net.rithms.riot.constant.Platform
import org.springframework.stereotype.Component

const val MAX_THREADS = 4

@Component
class RiotService {
  private val apiKey = System.getenv("API_KEY") ?: ""

  private fun waitRateLimit(rateLimitException: RateLimitException) {
    val seconds = (rateLimitException.retryAfter * 1000).toLong()
    Thread.sleep(seconds)
  }

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
    val config = ApiConfig()
            .setKey(apiKey)
            .setMaxAsyncThreads(MAX_THREADS)
    return RiotApi(config)
  }

  fun getMatchListing(region: Platform, accountId: String): List<MatchReference> {
    return try {
      getApi().getMatchListByAccountId(region, accountId).matches
    } catch (e: RateLimitException) {
      waitRateLimit(e)
      getMatchListing(region, accountId)
    }
  }
}