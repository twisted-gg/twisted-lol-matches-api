package com.twisted.lolmatches.match_loading

import com.twisted.dto.match_loading.dto.MatchLoadingSummonerStatus
import com.twisted.dto.summoner.GetSummonerRequest
import com.twisted.lolmatches.entity.match.MatchRepository
import com.twisted.lolmatches.entity.match_loading.MatchLoadingDocument
import com.twisted.lolmatches.entity.match_loading.MatchLoadingRepository
import com.twisted.lolmatches.mapper.match_loading.getLoadingPendingMatches
import com.twisted.lolmatches.mapper.match_loading.mapMatchLoading
import com.twisted.lolmatches.mapper.match_loading.mapSummonerLoadingMatchesStatus
import com.twisted.lolmatches.riot.RiotService
import com.twisted.lolmatches.summoners.SummonersService
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class MatchLoadingService(
        private val summonerService: SummonersService,
        private val riotApi: RiotService,
        private val repository: MatchRepository,
        private val loadingRepository: MatchLoadingRepository
) {
  private val url: String = System.getenv("LOADER_SERVICE")
  private val rest = RestTemplate()

  private fun isLoadingMatch(game_id: Long, region: String): Boolean {
    val documents = this.loadingRepository.findMatch(game_id, region)
    val exists = this.repository.existsByGameId(game_id, region)
    return documents.count() > 0 || exists
  }

  private fun sendMatchesToProcessing(id: String): Unit {
    val finalUrl = "$url/load/$id"
    val url = UriComponentsBuilder.fromHttpUrl(finalUrl)
            .toUriString()
    rest.getForEntity(
            url,
            String.javaClass
    )
  }

  private fun saveAndProcess(instance: MatchLoadingDocument) {
    if (instance.matches.count() == 0) return
    val newInstance = loadingRepository.save(instance)
    val id = newInstance.id.toString()
    try {
      sendMatchesToProcessing(id)
    } catch (e: Exception) {
      newInstance.healthy = false
      loadingRepository.save(newInstance)
    }
  }

  fun reloadSummoner(params: GetSummonerRequest) {
    val summoner = summonerService.getSummoner(params).get()
    val region = riotApi.parseRegion(params.region)
    val matchList = riotApi.getMatchListing(region, summoner.accountId)
    val loadingMatches = getLoadingPendingMatches(matchList)
            .filter { m -> !isLoadingMatch(m.game_id, region.toString()) }
    saveAndProcess(mapMatchLoading(
            id = summoner._id,
            region = region.toString(),
            matches = loadingMatches
    ))
  }

  fun summonerStatus(params: GetSummonerRequest): MatchLoadingSummonerStatus {
    val summoner = summonerService.getSummoner(params).get()
    val loading = loadingRepository.findSummonerLoadingMatches(summoner._id)
    return mapSummonerLoadingMatchesStatus(summoner._id, loading)
  }
}
