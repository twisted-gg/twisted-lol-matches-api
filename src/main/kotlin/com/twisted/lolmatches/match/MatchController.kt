package com.twisted.lolmatches.match

import com.twisted.lolmatches.summoners.dto.GetSummonerDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping("match")
class MatchController(
        private val service: MatchService
) {
  @GetMapping
  fun getMatches(params: GetSummonerDto): Mono<Int> =
          Mono.just(service.loadSummonerMatches(params))
}
