package com.hjw0623.character.domain

import com.hjw0623.character.domain.model.gear.Gear
import com.hjw0623.character.domain.model.profile.CharacterProfile
import com.hjw0623.core.domain.util.DataError
import com.hjw0623.core.domain.util.Result

interface CharacterRepository {
    suspend fun getCharacterProfile(characterName: String): Result<CharacterProfile, DataError.Network>
    suspend fun getGear(characterName: String): Result<List<Gear>, DataError.Network>
}