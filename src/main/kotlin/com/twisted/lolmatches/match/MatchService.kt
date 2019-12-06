package com.twisted.lolmatches.match

import com.twisted.lolmatches.dto.GetSummonerDto
import com.twisted.lolmatches.dto.ListRegions
import com.twisted.lolmatches.riot.RiotService
import com.twisted.lolmatches.summoners.SummonersService
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchReference
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
    return api.getMatch(region, matchId)
  }

  private fun matchToDocument(match: MatchReference, region: ListRegions): MatchDocument {
    return MatchDocument(
            game_creation = match.timestamp,
            game_id = match.gameId,
            region = region
    )
  }

  fun getSummonerMatches(params: GetSummonerDto): Match {
    val summoner = summonerService.getSummoner(params)
    val region = riotApi.parseRegion(params.region)
    val matchList = api.getMatchListByAccountId(region, summoner.accountId).matches
    repository.save(matchToDocument(matchList[0], params.region))
    return matchDetails(region, matchList[0].gameId)
  }
}