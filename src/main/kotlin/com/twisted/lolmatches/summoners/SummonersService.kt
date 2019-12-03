package com.twisted.lolmatches.summoners

import com.twisted.lolmatches.dto.GetSummonerDto
import com.twisted.lolmatches.dto.SummonerDto
import com.twisted.lolmatches.errors.NotFoundException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

class SummonersService {
  private val baseUrl = System.getenv("SUMMONERS_SERVICE")
  private val rest = RestTemplate()

  fun getSummoner(param: GetSummonerDto): SummonerDto {
    val url = UriComponentsBuilder.fromHttpUrl(this.baseUrl)
            .queryParam("summonerName", param.summonerName)
            .queryParam("region", param.region)
            .toUriString()
    return this.rest.getForObject<SummonerDto>(url, SummonerDto::class.java)
            ?: throw NotFoundException()
  }
}