package com.twisted.lolmatches.match

import com.twisted.lolmatches.match_loading.MatchLoadingService
import com.twisted.lolmatches.summoners.dto.GetSummonerDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
}
