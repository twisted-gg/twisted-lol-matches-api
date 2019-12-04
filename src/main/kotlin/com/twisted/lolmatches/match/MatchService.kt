package com.twisted.lolmatches.match

import com.twisted.lolmatches.dto.GetSummonerDto
import com.twisted.lolmatches.riot.RiotService
import com.twisted.lolmatches.summoners.SummonersService
import net.rithms.riot.api.endpoints.match.dto.MatchList
import org.springframework.stereotype.Component

@Component
class MatchService(
        private val summonerService: SummonersService,
        private val riotApi: RiotService
) {
  private val api = riotApi.getApi()

  fun getSummonerMatches(params: GetSummonerDto): MatchList {
    val summoner = summonerService.getSummoner(params)
    val region = riotApi.parseRegion(params.region)
    return api.getMatchListByAccountId(region, summoner.accountId)
  }
}