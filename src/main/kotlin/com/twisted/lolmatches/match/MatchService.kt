package com.twisted.lolmatches.match

import com.twisted.dto.match_loading.MatchLoadingMatches
import com.twisted.lolmatches.entity.match.MatchRepository
import com.twisted.lolmatches.entity.match_loading.MatchLoadingDocument
import com.twisted.lolmatches.entity.match_loading.MatchLoadingRepository
import com.twisted.lolmatches.mapper.match.matchToDocument
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
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

@Component
class MatchService(
        private val summonerService: SummonersService,
        private val riotApi: RiotService,
        private val repository: MatchRepository,
        private val loadingRepository: MatchLoadingRepository
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

  private fun isLoadingMatch(game_id: Long, region: String): Boolean {
    val documents = this.loadingRepository.findMatch(game_id, region)
    val exists = this.repository.existsByGameId(game_id, region)
    return documents.count() > 0 || exists
  }

  private fun loadAllMatches(matchList: List<MatchReference>, region: Platform): Int {
    val newMatches = matchList.filter { m -> !existsByGameIdAndRegion(m) }
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
    val region = riotApi.parseRegion(params.region)
    val matchList = api.getMatchListByAccountId(region, summoner.accountId).matches
    return loadAllMatches(matchList, region)
  }

  fun loadSummonerMatches(params: GetSummonerDto): Int {
    val summoner = summonerService.getSummoner(params).get()
    val region = riotApi.parseRegion(params.region)
    val matchList = api.getMatchListByAccountId(region, summoner.accountId).matches
    val loadMatches = mutableListOf<MatchLoadingMatches>()
    for (match in matchList) {
      val isLoading = this.isLoadingMatch(match.gameId, region.toString())
      if (isLoading) continue
      loadMatches.add(MatchLoadingMatches(
              game_id = match.gameId
      ))
    }
    if (loadMatches.count() > 0) {
      this.loadingRepository.save(
              MatchLoadingDocument(
                      summoner = ObjectId(summoner._id),
                      region = region.toString(),
                      matches = loadMatches
              )
      )
    }
    return matchList.count()
  }
}