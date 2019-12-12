package com.twisted.lolmatches.match.mapper.participant

import com.twisted.lolmatches.contants.match.MatchEventsEnum
import com.twisted.lolmatches.entity.match.participant.MatchParticipantEvents
import net.rithms.riot.api.endpoints.match.dto.MatchEvent
import net.rithms.riot.api.endpoints.match.dto.MatchTimeline

private fun isParticipantEvent(event: MatchEvent, participantId: Int): Boolean =
        event.participantId == participantId || event.killerId == participantId || event.creatorId == participantId

private fun getEvents(frames: MatchTimeline, participantId: Int): List<MatchEvent> =
        frames.frames.map { f -> f.events }
                .flatten()
                .filter { e -> isParticipantEvent(e, participantId) }

private fun fillEvent(event: MatchEvent, response: MatchParticipantEvents): MatchParticipantEvents {
  when (event.type) {
    MatchEventsEnum.CHAMPION_KILL.toString(),
    MatchEventsEnum.ELITE_MONSTER_KILL.toString(),
    MatchEventsEnum.BUILDING_KILL.toString() ->
      response.kill += 1
    MatchEventsEnum.WARD_KILL.toString(),
    MatchEventsEnum.WARD_PLACED.toString() ->
      response.ward += 1
    MatchEventsEnum.ITEM_DESTROYED.toString(),
    MatchEventsEnum.ITEM_PURCHASED.toString(),
    MatchEventsEnum.ITEM_SOLD.toString(),
    MatchEventsEnum.ITEM_UNDO.toString() ->
      response.item += 1
    MatchEventsEnum.SKILL_LEVEL_UP.toString() ->
      response.skillLevelUp += 1

  }
  return response
}

fun matchParticipantEventMapper(participantId: Int, frames: MatchTimeline): MatchParticipantEvents {
  var response = MatchParticipantEvents(
          ward = 0,
          kill = 0,
          item = 0,
          skillLevelUp = 0
  )
  val events = getEvents(frames, participantId)
  for (event in events) {
    response = fillEvent(
            event = event,
            response = response
    )
  }
  return response
}