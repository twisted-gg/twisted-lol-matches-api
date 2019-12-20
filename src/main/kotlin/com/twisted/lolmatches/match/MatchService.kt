package com.twisted.lolmatches.match

import com.twisted.lolmatches.entity.match.MatchRepository
import com.twisted.lolmatches.entity.match_loading.MatchLoadingRepository
import com.twisted.lolmatches.riot.RiotService
import com.twisted.lolmatches.summoners.SummonersService
import org.springframework.stereotype.Component

@Component
class MatchService(
        private val summonerService: SummonersService,
        private val riotApi: RiotService,
        private val repository: MatchRepository,
        private val loadingRepository: MatchLoadingRepository
) {
  private val api = riotApi.getApi()
}