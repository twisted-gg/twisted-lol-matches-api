package com.twisted.lolmatches.entity.match.team

data class MatchTeamStats(
        val baronKills: Int,
        val dominionVictoryScore: Int,
        val dragonKills: Int,
        val inhibitorKills: Int,
        val riftHeraldKills: Int,
        val towerKills: Int,
        val vilemawKills: Int,
        val firstBaron: Boolean,
        val firstBlood: Boolean,
        val firstDragon: Boolean,
        val firstInhibitor: Boolean,
        val firstRiftHerald: Boolean,
        val firstTower: Boolean
)