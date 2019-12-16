package com.twisted.lolmatches.match

import com.twisted.lolmatches.entity.match.MatchRepository
import com.twisted.lolmatches.match.mapper.matchToDocument
import com.twisted.lolmatches.riot.RiotService
import com.twisted.lolmatches.summoners.SummonersService
import com.twisted.lolmatches.summoners.dto.GetSummonerDto
import com.twisted.lolmatches.summoners.dto.SummonerDto
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchReference
import net.rithms.riot.api.endpoints.match.dto.MatchTimeline
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

  private fun getMatchesTimeline(matchList: List<MatchReference>, region: Platform): Map<Long, MatchTimeline> {
    val response = mutableMapOf<Long, MatchTimeline>()
    val asyncApi = riotApi.getAsynApi()
    for (match in matchList) {
      asyncApi.getTimelineByMatchId(region, match.gameId).also {
        it.addListeners(object : RequestAdapter() {
          override fun onRequestSucceeded(request: AsyncRequest) {
            response[match.gameId] = request.getDto<MatchTimeline>()
          }
        })
      }
    }
    asyncApi.awaitAll()
    return response
  }

  private fun existsByGameIdAndRegion(match: MatchReference) = repository.findByIdAndRegion(gameId = match.gameId, region = match.platformId.toString()) != null

  private fun getSummonerMatches(summoner: SummonerDto) = repository.findSummonerMatches(summoner._id)

  private fun getAllMatchesDetails(matchList: List<MatchReference>, region: Platform): List<Match> {
    val asyncApi = riotApi.getAsynApi()
    val allMatchDetails = mutableListOf<Match>()
    for (match in matchList) {
      asyncApi.getMatch(region, match.gameId).also {
        it.addListeners(object : RequestAdapter() {
          override fun onRequestSucceeded(request: AsyncRequest) {
            allMatchDetails.add(request.getDto<Match>())
          }
        })
      }
    }
    asyncApi.awaitAll()
    return allMatchDetails
  }

  private fun loadAllMatches(matchList: List<MatchReference>, region: Platform): Int {
    val newMatches = listOf(matchList[0]) // matchList.filter { m -> !existsByGameIdAndRegion(m) }
    val matchesDetails = getAllMatchesDetails(newMatches, region)
    val matchesTimeline = getMatchesTimeline(newMatches, region)
    for (match in matchesDetails) {
      val timeline = matchesTimeline[match.gameId] ?: continue
      val document = matchToDocument(
              match = match,
              matchTimeline = timeline
      )
      repository.save(document)
    }
    return newMatches.size
  }

  fun getSummonerMatches(params: GetSummonerDto): Int {
    val summoner = summonerService.getSummoner(params).get()
    val summonerMatches = getSummonerMatches(summoner)
    val region = riotApi.parseRegion(params.region)
    val matchList = api.getMatchListByAccountId(region, summoner.accountId).matches
    return loadAllMatches(matchList, region)
  }
}