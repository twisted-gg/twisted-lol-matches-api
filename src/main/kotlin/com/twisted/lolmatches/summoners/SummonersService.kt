package com.twisted.lolmatches.summoners

import com.twisted.lolmatches.summoners.dto.GetSummonerDto
import com.twisted.lolmatches.summoners.dto.SummonerDto
import com.twisted.lolmatches.errors.NotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class SummonersService {
  private val baseUrl = System.getenv("SUMMONERS_SERVICE")
  private val rest = RestTemplate()

  fun getSummoner(param: GetSummonerDto): SummonerDto {
    val url = UriComponentsBuilder.fromHttpUrl(this.baseUrl)
            .queryParam("summonerName", param.summonerName)
            .queryParam("region", param.region)
            .queryParam("accountID", param.accountID)
            .toUriString()
    return this.rest.getForObject<SummonerDto>(url, SummonerDto::class.java)
            ?: throw NotFoundException()
  }
}