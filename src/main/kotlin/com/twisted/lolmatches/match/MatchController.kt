package com.twisted.lolmatches.match

import com.twisted.dto.match_loading.dto.MatchLoadingSummonerStatus
import com.twisted.lolmatches.match_loading.MatchLoadingService
import com.twisted.lolmatches.summoners.dto.GetSummonerDto
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
@RequestMapping("match")
class MatchController(
        private val service: MatchService,
        private val loadingService: MatchLoadingService
) {
  @PostMapping("update")
  fun updateMatches(@RequestBody params: GetSummonerDto): Mono<Unit> =
          Mono.just(loadingService.reloadSummoner(params))

  @GetMapping("summoner/status")
  fun summonerStatus(params: GetSummonerDto): Mono<MatchLoadingSummonerStatus> = Mono.just(loadingService.summonerStatus(params))
}
