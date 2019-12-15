package com.twisted.lolmatches.match

import com.twisted.lolmatches.entity.match.MatchRepository
import com.twisted.lolmatches.match.mapper.matchToDocument
import com.twisted.lolmatches.riot.RiotService
import com.twisted.lolmatches.summoners.SummonersService
import com.twisted.lolmatches.summoners.dto.GetSummonerDto
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchReference
import net.rithms.riot.api.request.AsyncRequest
import net.rithms.riot.api.request.RequestAdapter
import net.rithms.riot.constant.Platform
import org.springframework.stereotype.Component


@Component
class MatchService(
        private val summonerService: SummonersService,
        private val riotApi: RiotService,
        private val repository: MatchRepository
) {
  private val api = riotApi.getApi()

  private fun matchDetails(match: Match, region: Platform): Match {
    val matchFrames = api.getTimelineByMatchId(region, match.gameId)
    val document = matchToDocument(match, matchFrames)
    repository.save(document)
    return match
  }

  private fun existsByGameIdAndRegion(match: MatchReference) = repository.findByIdAndRegion(gameId = match.gameId, region = match.platformId.toString()) != null

  private fun loadAllMatches(matchList: List<MatchReference>, region: Platform): Int {
    val asynApi = api.asyncApi ?: throw Exception()
    val uniqueMatchList = matchList.filter { m -> !existsByGameIdAndRegion(m) }
    for (match in uniqueMatchList) {
      asynApi.getMatch(region, match.gameId).also {
        it.addListeners(object : RequestAdapter() {
          override fun onRequestSucceeded(request: AsyncRequest) {
            val response = request.getDto<Match>()
            matchDetails(response, region)
          }
        })
      }
    }
    asynApi.awaitAll()
    return uniqueMatchList.size
  }

  fun getSummonerMatches(params: GetSummonerDto): Int {
    val summoner = summonerService.getSummoner(params).get()
    val region = riotApi.parseRegion(params.region)
    val matchList = api.getMatchListByAccountId(region, summoner.accountId).matches
    return loadAllMatches(matchList, region)
  }
}