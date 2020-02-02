package com.twisted.lolmatches.summoners

import com.twisted.dto.summoner.GetMultipleSummonerNameById
import com.twisted.dto.summoner.GetSummonerRequest
import com.twisted.dto.summoner.SummonerDocument
import com.twisted.lolmatches.dto.match.GetSummonerMatchesRequest
import com.twisted.lolmatches.errors.NotFoundException
import org.bson.types.ObjectId
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.concurrent.CompletableFuture

@Component
class SummonersService {
  private val baseUrl = System.getenv("SUMMONERS_SERVICE")
  private val rest = RestTemplate()

  @Async
  fun getSummoner(param: GetSummonerMatchesRequest): CompletableFuture<SummonerDocument> {
    val query = GetSummonerRequest(
            summonerName = param.summonerName,
            region = param.region
    )
    return getSummoner(query)
  }

  @Async
  fun getSummoner(param: GetSummonerRequest): CompletableFuture<SummonerDocument> {
    val url = UriComponentsBuilder.fromHttpUrl(this.baseUrl)
            .queryParam("summonerName", param.summonerName)
            .queryParam("region", param.region)
            .queryParam("accountID", param.accountID)
            .toUriString()
    return CompletableFuture.supplyAsync {
      this.rest.getForObject<SummonerDocument>(url, SummonerDocument::class.java)
              ?: throw NotFoundException()
    }
  }

  @Async
  fun getMultipleSummonerName(ids: List<ObjectId>): GetMultipleSummonerNameById {
    var url = UriComponentsBuilder.fromHttpUrl("${this.baseUrl}/summonerName/multi")
    for (id in ids) {
      url.queryParam("id", id.toString())
    }
    return this.rest.getForObject<GetMultipleSummonerNameById>(url.toUriString(), GetMultipleSummonerNameById::class.java)
            ?: throw NotFoundException()
  }
}