package com.twisted.lolmatches.entity.match.participant.events

data class MatchParticipantEvents(
        val ward: List<MatchParticipantEventsWard>,
        val championKill: List<MatchParticipantEventsChampionKill>,
        val item: List<MatchParticipantEventsItem>,
        val skillLevelUp: List<MatchParticipantEventsSkillLevelUp>,
        val buildingKill: List<MatchParticipantEventsBuildingKill>,
        val eliteMonsterKill: List<MatchParticipantEventsEliteMonsterKill>
)
