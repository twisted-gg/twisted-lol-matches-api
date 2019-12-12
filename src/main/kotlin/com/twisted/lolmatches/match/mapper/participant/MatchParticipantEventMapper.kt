package com.twisted.lolmatches.match.mapper.participant

import net.rithms.riot.api.endpoints.match.dto.MatchEvent
import net.rithms.riot.api.endpoints.match.dto.MatchTimeline

private fun isEventFromParticipant(event: MatchEvent, participantId: Int) =
        event.participantId == participantId

fun matchParticipantEventMapper(participantId: Int, frames: MatchTimeline) {
  val participantEvents = mutableMapOf<String, Int>()
  for (frame in frames.frames) {
    for (event in frame.events) {
      if (isEventFromParticipant(event, participantId)) {
        participantEvents[event.type] = (participantEvents[event.type] ?: 0) + 1
      }
    }
  }
  // print(participantEvents)
}