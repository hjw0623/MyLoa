package com.hjw0623.core.database.mapper

import com.hjw0623.core.database.entity.CharacterEntity
import com.hjw0623.core.database.entity.SelectedRaidEntity
import com.hjw0623.core.domain.character.Character
import com.hjw0623.core.domain.character.SelectedRaid

fun CharacterEntity.toCharacter(): Character {
    return Character(
        server = server,
        characterName = characterName,
        className = className,
        avgItemLevel = avgItemLevel
    )
}

fun Character.toCharacterEntity(): CharacterEntity {
    return CharacterEntity(
        characterName = characterName,
        className = className,
        server = server,
        avgItemLevel = avgItemLevel
    )
}

fun SelectedRaidEntity.toSelectedRaid(): SelectedRaid {
    return SelectedRaid(
        id = id,
        characterId = characterId,
        raidName = raidName,
        difficulty = difficulty,
        maxGoldReward = maxGoldReward,
        isCompleted = isCompleted
    )
}

fun SelectedRaid.toSelectedRaidEntity(): SelectedRaidEntity {
    return SelectedRaidEntity(
        id = id,
        characterId = characterId,
        raidName = raidName,
        difficulty = difficulty,
        maxGoldReward = maxGoldReward,
        isCompleted = isCompleted
    )
}
