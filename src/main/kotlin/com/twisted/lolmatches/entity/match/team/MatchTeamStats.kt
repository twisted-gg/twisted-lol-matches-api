package com.twisted.lolmatches.entity.match.team

data class MatchTeamStats(
        private val baronKills: Int,
        private val dominionVictoryScore: Int,
        private val dragonKills: Int,
        private val inhibitorKills: Int,
        private val riftHeraldKills: Int,
        private val towerKills: Int,
        private val vilemawKills: Int,
        private val firstBaron: Boolean,
        private val firstBlood: Boolean,
        private val firstDragon: Boolean,
        private val firstInhibitor: Boolean,
        private val firstRiftHerald: Boolean,
        private val firstTower: Boolean
)