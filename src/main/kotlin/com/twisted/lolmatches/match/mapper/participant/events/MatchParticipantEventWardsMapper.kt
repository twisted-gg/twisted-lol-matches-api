package com.twisted.lolmatches.match.mapper.participant.events

import com.twisted.lolmatches.contants.match.MatchEventsEnum
import com.twisted.lolmatches.entity.match.participant.events.MatchParticipantEventsWard
import net.rithms.riot.api.endpoints.match.dto.MatchEvent

fun isWardEvent(event: MatchEvent): Boolean =
        MatchEventsEnum.WARD_KILL.toString() == event.type || MatchEventsEnum.WARD_PLACED.toString() == event.type

fun parseWardEvent(event: MatchEvent) =
        MatchParticipantEventsWard(
                type = event.type,
                timestamp = event.timestamp,
                wardType = event.wardType
        )
