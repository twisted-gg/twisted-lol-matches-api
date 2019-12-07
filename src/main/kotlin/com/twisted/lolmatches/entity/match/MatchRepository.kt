package com.twisted.lolmatches.entity.match

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MatchRepository : MongoRepository<MatchDocument, String>
