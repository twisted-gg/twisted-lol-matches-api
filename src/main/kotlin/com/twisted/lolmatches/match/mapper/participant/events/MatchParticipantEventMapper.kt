package com.twisted.lolmatches.match.mapper.participant.events

import com.twisted.lolmatches.entity.match.participant.events.*
import com.twisted.lolmatches.summoners.dto.SummonerDto
import net.rithms.riot.api.endpoints.match.dto.Match
import net.rithms.riot.api.endpoints.match.dto.MatchEvent
import net.rithms.riot.api.endpoints.match.dto.MatchTimeline

private fun isParticipantEvent(event: MatchEvent, participantId: Int): Boolean =
        event.participantId == participantId || event.killerId == participantId || event.creatorId == participantId

private fun getEvents(frames: MatchTimeline, participantId: Int): List<MatchEvent> =
        frames.frames.map { f -> f.events }
                .flatten()
                .filter { e -> isParticipantEvent(e, participantId) }
                .sortedBy { e -> e.timestamp }

fun matchParticipantEventMapper(match: Match, participantId: Int, frames: MatchTimeline, participants: List<SummonerDto>): MatchParticipantEvents {
  val wardEvents = mutableListOf<MatchParticipantEventsWard>()
  val itemEvents = mutableListOf<MatchParticipantEventsItem>()
  val skillLevelUpEvents = mutableListOf<MatchParticipantEventsSkillLevelUp>()
  val championKillEvents = mutableListOf<MatchParticipantEventsChampionKill>()
  val events = getEvents(frames, participantId)
  for (event in events) {
    when {
      isWardEvent(event) -> wardEvents.add(parseWardEvent(event))
      isItemEvent(event) -> itemEvents.add(parseItemEvent(event))
      isSkillLevelUpEvent(event) -> skillLevelUpEvents.add(parseSkillLevelUpEvent(event))
      isChampionKillEvent(event) -> championKillEvents.add(parseChampionKillEvent(event, match, participants))
    }
  }
  return MatchParticipantEvents(
          ward = wardEvents,
          kill = championKillEvents,
          item = itemEvents,
          skillLevelUp = skillLevelUpEvents
  )
}