package com.twisted.lolmatches.match.mapper.participant.timeline

import com.twisted.lolmatches.entity.match.participant.timeline.MatchParticipantTimeline
import net.rithms.riot.api.endpoints.match.dto.ParticipantTimeline

fun validation(value: Map<String, Double>?): Map<String, Double> {
  if (value == null) {
    return mapOf()
  }
  return value
}

/**
 * Participant timeline
 * Convert riot response to required document
 */
fun participantTimeline(timeline: ParticipantTimeline): MatchParticipantTimeline {
  return MatchParticipantTimeline(
          lane = timeline.lane,
          role = timeline.role,
          csDiffPerMinDeltas = validation(timeline.csDiffPerMinDeltas),
          creepsPerMinDeltas = validation(timeline.creepsPerMinDeltas),
          damageTakenDiffPerMinDeltas = validation(timeline.damageTakenDiffPerMinDeltas),
          damageTakenPerMinDeltas = validation(timeline.damageTakenPerMinDeltas),
          goldPerMinDeltas = validation(timeline.goldPerMinDeltas),
          xpDiffPerMinDeltas = validation(timeline.xpDiffPerMinDeltas),
          xpPerMinDeltas = validation(timeline.xpPerMinDeltas)
  )
}
