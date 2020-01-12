package com.twisted.lolmatches.mapper.match_listing

import com.twisted.dto.match_listing.MatchListing
import com.twisted.dto.summoner.SummonerDocument
import com.twisted.lolmatches.dto.match.GetSummonerMatchesRequest
import com.twisted.lolmatches.entity.match.MatchDocument

fun mapMatchListing(query: GetSummonerMatchesRequest, summoner: SummonerDocument, matches: List<MatchDocument>, total: Int): MatchListing {
  return MatchListing(
          page = query.page,
          limit = query.limit,
          total = total,
          matches = mapperMatchListingMatches(summoner, matches)
  )
}