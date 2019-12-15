package com.twisted.lolmatches.match.mapper.participant.events

import com.twisted.lolmatches.contants.match.MatchEventsEnum
import com.twisted.lolmatches.entity.match.participant.events.MatchParticipantEventsChampionKill
import com.twisted.lolmatches.summoners.dto.SummonerDto
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchEvent

fun isChampionKillEvent(event: MatchEvent): Boolean =
        MatchEventsEnum.CHAMPION_KILL.toString() == event.type

private fun findTargetId(event: MatchEvent, match: Match, participants: List<SummonerDto>) = findSummonerByParticipantId(event.victimId, match, participants)

fun parseChampionKillEvent(event: MatchEvent, match: Match, participants: List<SummonerDto>): MatchParticipantEventsChampionKill {
  val target = findTargetId(event, match, participants)
  val assistingIds = findAssistingIds(event, match, participants)
  val position = getPosition(event)
  return MatchParticipantEventsChampionKill(
          timestamp = event.timestamp,
          position = position,
          target = target,
          assistingIds = assistingIds
  )
}
