package com.twisted.lolmatches.match

import com.twisted.lolmatches.dto.GetSummonerDto
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
    repository.save(matchToDocument(match, region))
    return match
  }

  private fun matchToDocument(match: Match, region: Platform): MatchDocument {
    return MatchDocument(
            region = region,
            game_id = match.gameId,
            creation = match.gameCreation,
            mode = match.gameMode,
            type = match.gameType,
            version = match.gameVersion,
            map_id = match.mapId,
            queue = match.queueId,
            season = match.seasonId
    )
  }

  fun getSummonerMatches(params: GetSummonerDto): Match {
    val summoner = summonerService.getSummoner(params)
    val region = riotApi.parseRegion(params.region)
    val matchList = api.getMatchListByAccountId(region, summoner.accountId).matches
    return matchDetails(region, matchList[0].gameId)
  }
}