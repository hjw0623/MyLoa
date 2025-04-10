package com.hjw0623.character.presentation.character_overview.model.card

import com.hjw0623.character.domain.model.card.Card
import com.hjw0623.character.domain.model.card.CardEffect
import com.hjw0623.character.presentation.character_overview.util.shortCardEffect

fun Card.toCardUi(): CardUi {
    return CardUi(
        cardIcon = this.icon,
        cardName = this.name,
        cardLevel = this.awakeCount,
        cardGrade = this.grade
    )
}

fun CardEffect.toCardEffectUi(): CardEffectUi {
    val lastItemName = this.items.lastOrNull()?.name ?: "Unknown Effect"

    val effectName = lastItemName
        .replace(Regex("\\s\\d+세트"), "")
        .replace(Regex("\\s*\\(.*\\)"), "")

    val effectGrade = lastItemName
        .substringAfterLast("(")
        .substringBeforeLast("각성합계)")
        .toIntOrNull() ?: 0

    val effectDescriptions = this.items.associate { item ->
        val key = item.name.substringAfterLast(" ")
        key to item.description
    }

    return CardEffectUi(
        index = this.index,
        effectName = shortCardEffect(effectName),
        effectGrade = effectGrade,
        effectDescriptions = effectDescriptions
    )
}



