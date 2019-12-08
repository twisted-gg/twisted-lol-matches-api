package com.twisted.lolmatches.match.mapper

import com.twisted.lolmatches.entity.match.participant.MatchParticipantTimeline
import net.rithms.riot.api.endpoints.match.dto.ParticipantTimeline

/**
 * Participant timeline
 * Convert riot response to required document
 */
fun participantTimeline(timeline: ParticipantTimeline): MatchParticipantTimeline {
  return MatchParticipantTimeline(
          lane = timeline.lane,
          role = timeline.role,
          csDiffPerMinDeltas = timeline.csDiffPerMinDeltas,
          creepsPerMinDeltas = timeline.creepsPerMinDeltas,
          damageTakenDiffPerMinDeltas = timeline.damageTakenDiffPerMinDeltas,
          damageTakenPerMinDeltas = timeline.damageTakenPerMinDeltas,
          goldPerMinDeltas = timeline.goldPerMinDeltas,
          xpDiffPerMinDeltas = timeline.xpDiffPerMinDeltas,
          xpPerMinDeltas = timeline.xpPerMinDeltas
  )
}
