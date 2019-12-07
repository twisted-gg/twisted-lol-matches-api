package com.twisted.lolmatches.match

import com.twisted.lolmatches.summoners.dto.GetSummonerDto
import com.twisted.lolmatches.entity.match.MatchRepository
import com.twisted.lolmatches.riot.RiotService
import com.twisted.lolmatches.summoners.SummonersService
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.constant.Platform
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MatchService(
        private val summonerService: SummonersService,
        private val riotApi: RiotService,
        @Autowired private val repository: MatchRepository
) {
  private val api = riotApi.getApi()

  private fun matchDetails(region: Platform, matchId: Long): Match {
    val match = api.getMatch(region, matchId)
    repository.save(matchToDocument(match))
    return match
  }

  fun getSummonerMatches(params: GetSummonerDto): Match {
    val summoner = summonerService.getSummoner(params)
    val region = riotApi.parseRegion(params.region)
    val matchList = api.getMatchListByAccountId(region, summoner.accountId).matches
    return matchDetails(region, matchList[1].gameId)
  }
}