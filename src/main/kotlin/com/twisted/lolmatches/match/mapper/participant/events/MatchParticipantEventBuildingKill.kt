package com.twisted.lolmatches.match.mapper.participant.events

import com.twisted.lolmatches.contants.match.MatchEventsEnum
import com.twisted.lolmatches.entity.match.participant.events.MatchParticipantEventsBuildingKill
import com.twisted.lolmatches.summoners.dto.SummonerDto
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchEvent

fun isBuildingKillEvent(event: MatchEvent): Boolean =
        MatchEventsEnum.BUILDING_KILL.toString() == event.type

fun parseBuildingKillEvent(event: MatchEvent, match: Match, participants: List<SummonerDto>): MatchParticipantEventsBuildingKill {
  val assistingIds = findAssistingIds(event, match, participants)
  val position = getPosition(event)
  return MatchParticipantEventsBuildingKill(
          timestamp = event.timestamp,
          position = position,
          assistingIds = assistingIds,
          buildingType = event.buildingType,
          laneType = event.laneType
  )
}
