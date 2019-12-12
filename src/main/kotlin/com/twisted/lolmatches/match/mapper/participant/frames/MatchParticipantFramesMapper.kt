package com.twisted.lolmatches.match.mapper.participant.frames

import com.twisted.lolmatches.entity.match.participant.frames.MatchParticipantFrames
import com.twisted.lolmatches.entity.match.participant.frames.MatchParticipantFramesPosition
import net.rithms.riot.api.endpoints.match.dto.MatchFrame
import net.rithms.riot.api.endpoints.match.dto.MatchParticipantFrame

private fun parseFrame(frame: MatchParticipantFrame): MatchParticipantFrames =
        MatchParticipantFrames(
                position = MatchParticipantFramesPosition(
                        x = frame.position.x,
                        y = frame.position.y
                ),
                currentGold = frame.currentGold,
                totalGold = frame.totalGold,
                level = frame.level,
                xp = frame.xp,
                totalMinionsKilled = frame.minionsKilled + frame.jungleMinionsKilled,
                minionsKilled = frame.minionsKilled,
                jungleMinionsKilled = frame.jungleMinionsKilled,
                teamScore = frame.teamScore
        )

fun matchParticipantFrames(frames: List<MatchFrame>, participantId: Int): List<MatchParticipantFrames> {
  val response = mutableListOf<MatchParticipantFrames>()
  for (frame in frames) {
    val participantFrame = frame.participantFrames[participantId] ?: continue
    response.add(parseFrame(participantFrame))
  }
  return response
}