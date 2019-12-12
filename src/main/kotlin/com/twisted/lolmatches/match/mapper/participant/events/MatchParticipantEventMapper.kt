package com.twisted.lolmatches.match.mapper.participant.events

import com.twisted.lolmatches.entity.match.participant.events.MatchParticipantEvents
import com.twisted.lolmatches.entity.match.participant.events.MatchParticipantEventsItem
import com.twisted.lolmatches.entity.match.participant.events.MatchParticipantEventsSkillLevelUp
import com.twisted.lolmatches.entity.match.participant.events.MatchParticipantEventsWard
import net.rithms.riot.api.endpoints.match.dto.MatchEvent
import net.rithms.riot.api.endpoints.match.dto.MatchTimeline

private fun isParticipantEvent(event: MatchEvent, participantId: Int): Boolean =
        event.participantId == participantId || event.killerId == participantId || event.creatorId == participantId

private fun getEvents(frames: MatchTimeline, participantId: Int): List<MatchEvent> =
        frames.frames.map { f -> f.events }
                .flatten()
                .filter { e -> isParticipantEvent(e, participantId) }
                .sortedBy { e -> e.timestamp }

fun matchParticipantEventMapper(participantId: Int, frames: MatchTimeline): MatchParticipantEvents {
  val wardEvents = mutableListOf<MatchParticipantEventsWard>()
  val itemEvents = mutableListOf<MatchParticipantEventsItem>()
  val skillLevelUpEvents = mutableListOf<MatchParticipantEventsSkillLevelUp>()
  val events = getEvents(frames, participantId)
  for (event in events) {
    when {
      isWardEvent(event) -> wardEvents.add(parseWardEvent(event))
      isItemEvent(event) -> itemEvents.add(parseItemEvent(event))
      isSkillLevelUpEvent(event) -> skillLevelUpEvents.add(parseSkillLevelUpEvent(event))
    }
  }
  return MatchParticipantEvents(
          ward = wardEvents,
          kill = 0,
          item = itemEvents,
          skillLevelUp = skillLevelUpEvents
  )
}