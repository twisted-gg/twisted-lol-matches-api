package com.twisted.lolmatches.match

import com.twisted.dto.match_loading.dto.MatchLoadingSummonerStatus
import com.twisted.dto.summoner.GetSummonerRequest
import com.twisted.lolmatches.dto.match.GetSummonerMatchesRequest
import com.twisted.lolmatches.match_loading.MatchLoadingService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
@RequestMapping("match")
class MatchController(
        private val service: MatchService,
        private val loadingService: MatchLoadingService
) {
  @PostMapping("update")
  fun updateMatches(@RequestBody params: GetSummonerRequest): Mono<MatchLoadingSummonerStatus> =
          Mono.just(loadingService.reloadSummoner(params))

  @GetMapping("summoner/status")
  fun summonerStatus(params: GetSummonerRequest): Mono<MatchLoadingSummonerStatus> = Mono.just(loadingService.summonerStatus(params))

  @GetMapping("summoner")
  fun getSummonerMatches(param: GetSummonerMatchesRequest) = Mono.just(service.getMatches(param))

  @GetMapping("{id}")
  fun getMatch(@PathVariable("id") id: String) = Mono.just(service.get(id))

  @GetMapping("{id}/details")
  fun getMatchDetails(@PathVariable("id") id: String) = Mono.just(service.getMatchDetails(id))

  @GetMapping("{id}/analysis")
  fun getMatchAnalysis(@PathVariable("id") id: String) = Mono.just(service.getTeamAnalysis(id))

  @GetMapping("{id}/frames")
  fun getMatchFrames(@PathVariable("id") id: String) = Mono.just(service.getMatchFrames(id))

  @GetMapping("{id}/graph")
  fun getTeamGraph(@PathVariable("id") id: String) = Mono.just(service.getTeamGraph(id))
}
