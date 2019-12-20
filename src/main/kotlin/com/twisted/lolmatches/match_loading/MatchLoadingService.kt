package com.twisted.lolmatches.match_loading

import com.twisted.lolmatches.entity.match.MatchRepository
import com.twisted.lolmatches.entity.match_loading.MatchLoadingDocument
import com.twisted.lolmatches.entity.match_loading.MatchLoadingRepository
import com.twisted.lolmatches.mapper.match_loading.getLoadingPendingMatches
import com.twisted.lolmatches.mapper.match_loading.mapMatchLoading
import com.twisted.lolmatches.riot.RiotService
import com.twisted.lolmatches.summoners.SummonersService
import com.twisted.lolmatches.summoners.dto.GetSummonerDto
import org.springframework.stereotype.Component

@Component
class MatchLoadingService(
        private val summonerService: SummonersService,
        private val riotApi: RiotService,
        private val repository: MatchRepository,
        private val loadingRepository: MatchLoadingRepository
) {
  private fun isLoadingMatch(game_id: Long, region: String): Boolean {
    val documents = this.loadingRepository.findMatch(game_id, region)
    val exists = this.repository.existsByGameId(game_id, region)
    return documents.count() > 0 || exists
  }

  private fun saveAndProcess(instance: MatchLoadingDocument) {
    if (instance.matches.count() == 0) return
    loadingRepository.save(instance)
  }

  fun reloadSummoner(params: GetSummonerDto) {
    val summoner = summonerService.getSummoner(params).get()
    val region = riotApi.parseRegion(params.region)
    val matchList = riotApi.getApi().getMatchListByAccountId(region, summoner.accountId).matches
    val loadingMatches = getLoadingPendingMatches(
            matchList = matchList,
            region = region.toString()
    ).filter { m -> !isLoadingMatch(m.game_id, region.toString()) }
    saveAndProcess(mapMatchLoading(
            id = summoner._id,
            region = region.toString(),
            matches = loadingMatches
    ))
  }
}