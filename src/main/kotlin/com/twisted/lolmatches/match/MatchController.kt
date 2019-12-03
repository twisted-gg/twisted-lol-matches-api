package com.twisted.lolmatches.match

import com.twisted.lolmatches.dto.GetSummonerDto
import com.twisted.lolmatches.dto.SummonerDto
import com.twisted.lolmatches.summoners.SummonersService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping("match")
class MatchController {
  @GetMapping
  fun getMatches(params: GetSummonerDto, summonerService: SummonersService): Mono<SummonerDto> {
    return Mono.just(summonerService.getSummoner(params))
  }
}
