package com.twisted.lolmatches.match

import com.twisted.dto.match_listing.MatchListing
import com.twisted.lolmatches.dto.match.GetSummonerMatchesRequest
import com.twisted.lolmatches.entity.match.MatchRepository
import com.twisted.lolmatches.errors.NotFoundException
import com.twisted.lolmatches.mapper.match_details.matchDetailsMapper
import com.twisted.lolmatches.mapper.match_listing.mapMatchListing
import com.twisted.lolmatches.summoners.SummonersService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class MatchService(
        private val summonerService: SummonersService,
        private val repository: MatchRepository
) {
  fun getMatches(query: GetSummonerMatchesRequest): MatchListing {
    val summoner = summonerService.getSummoner(query).get()
    val summonerId = summoner._id
    val sortBy = "creation"
    val matches = repository.findSummonerMatches(summonerId, PageRequest.of(query.page, query.limit, Sort.by(Sort.Direction.DESC, sortBy)))
    val total = repository.countTotalMatches(summonerId)
    return mapMatchListing(query, summoner, matches, total)
  }

  fun get(id: String) = repository.findMatch(id) ?: throw NotFoundException()

  fun getMatchDetails(id: String) = matchDetailsMapper(get(id))
}