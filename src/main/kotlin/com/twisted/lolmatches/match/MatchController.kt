package com.twisted.lolmatches.match

import com.twisted.lolmatches.dto.GetSummonerDto
import net.rithms.riot.api.endpoints.match.dto.Match
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
  fun getMatches(params: GetSummonerDto): Mono<Match> {
    val matches = service.getSummonerMatches(params)
    return Mono.just(matches)
  }
}
