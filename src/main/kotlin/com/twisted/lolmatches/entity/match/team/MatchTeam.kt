package com.twisted.lolmatches.entity.match.team

data class MatchTeam(
        private val teamId: Int,
        private val win: Boolean,
        private val stats: MatchTeamStats,
        private val bans: List<MatchTeamBans>
)
