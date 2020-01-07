package com.twisted.lolmatches.match

import com.twisted.lolmatches.dto.match.GetSummonerMatchesRequest
import com.twisted.lolmatches.entity.match.MatchRepository
import com.twisted.lolmatches.summoners.SummonersService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class MatchService(
        private val summonerService: SummonersService,
        private val repository: MatchRepository
) {
  fun getMatches(query: GetSummonerMatchesRequest): Int {
    val summoner = summonerService.getSummoner(query).get()
    val summonerId = summoner._id
    val matches = repository.findSummonerMatches(summonerId, PageRequest.of(query.page, query.limit))
    val total = repository.countTotalMatches(summonerId)
    return matches.count()
  }
}