package com.twisted.lolmatches.match

import com.twisted.lolmatches.entity.match.MatchRepository
import com.twisted.lolmatches.match.mapper.matchToDocument
import com.twisted.lolmatches.riot.RiotService
import com.twisted.lolmatches.summoners.SummonersService
import com.twisted.lolmatches.summoners.dto.GetSummonerDto
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.constant.Platform
import org.springframework.stereotype.Component

@Component
class MatchService(
        private val summonerService: SummonersService,
        private val riotApi: RiotService,
        private val repository: MatchRepository
) {
  private val api = riotApi.getApi()

  private fun matchDetails(region: Platform, matchId: Long): Match {
    val match = api.getMatch(region, matchId)
    val matchFrames = api.getTimelineByMatchId(region, match.gameId)
    val document = matchToDocument(match, matchFrames)
    repository.save(document)
    return match
  }

  fun getSummonerMatches(params: GetSummonerDto): Match {
    val summoner = summonerService.getSummoner(params)
    val region = riotApi.parseRegion(params.region)
    val matchList = api.getMatchListByAccountId(region, summoner.accountId).matches
    return matchDetails(region, matchList[0].gameId)
  }
}