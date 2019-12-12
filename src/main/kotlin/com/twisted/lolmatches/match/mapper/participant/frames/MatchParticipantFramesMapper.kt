package com.twisted.lolmatches.match.mapper.participant.frames

import com.twisted.lolmatches.entity.match.participant.frames.MatchParticipantFrames
import com.twisted.lolmatches.entity.match.participant.frames.MatchParticipantFramesPosition
import net.rithms.riot.api.endpoints.match.dto.MatchFrame
import net.rithms.riot.api.endpoints.match.dto.MatchParticipantFrame

private fun parseFrame(event: MatchParticipantFrame, frame: Int): MatchParticipantFrames =
        MatchParticipantFrames(
                position = MatchParticipantFramesPosition(
                        x = event.position.x,
                        y = event.position.y
                ),
                currentGold = event.currentGold,
                totalGold = event.totalGold,
                level = event.level,
                xp = event.xp,
                totalMinionsKilled = event.minionsKilled + event.jungleMinionsKilled,
                minionsKilled = event.minionsKilled,
                jungleMinionsKilled = event.jungleMinionsKilled,
                teamScore = event.teamScore,
                frame = frame.toByte()
        )

fun matchParticipantFrames(frames: List<MatchFrame>, participantId: Int): List<MatchParticipantFrames> {
  val response = mutableListOf<MatchParticipantFrames>()
  for ((i, frame) in frames.withIndex()) {
    val participantFrame = frame.participantFrames[participantId] ?: continue
    response.add(parseFrame(participantFrame, i))
  }
  return response
}