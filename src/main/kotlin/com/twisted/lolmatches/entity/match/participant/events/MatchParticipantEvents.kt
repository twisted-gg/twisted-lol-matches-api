package com.twisted.lolmatches.entity.match.participant.events

data class MatchParticipantEvents(
        val ward: List<MatchParticipantEventsWard>,
        val kill: Int,
        val item: List<MatchParticipantEventsItem>,
        val skillLevelUp: List<MatchParticipantEventsSkillLevelUp>
)
