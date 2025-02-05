package com.hjw0623.character.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hjw0623.character.domain.CharacterRepository
import com.hjw0623.character.presentation.mockup.emptyAbilityStoneUi
import com.hjw0623.character.presentation.mockup.emptyBraceletUi
import com.hjw0623.character.presentation.model.gear.ElixirUi
import com.hjw0623.character.presentation.model.gear.categorizeGears
import com.hjw0623.character.presentation.model.gear.toAbilityStoneUi
import com.hjw0623.character.presentation.model.gear.toAccessoriesUi
import com.hjw0623.character.presentation.model.gear.toBraceletUi
import com.hjw0623.character.presentation.model.gear.toGearUi
import com.hjw0623.character.presentation.model.profile.toCharacterProfileUi
import com.hjw0623.core.domain.util.onError
import com.hjw0623.core.domain.util.onSuccess
import com.hjw0623.core.presentation.ui.UiText
import com.hjw0623.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.whileSelect
import timber.log.Timber

class CharacterViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterState())
    var state = _state
        .onStart { loadAllData() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CharacterState()
        )

    private val _events = Channel<CharacterEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var hasErrorOccurred = false

    private fun loadAllData() {
        loadCharacterProfile()
        loadGear()
    }

    private fun loadCharacterProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isCharacterProfileLoading = true) }

            characterRepository
                .getCharacterProfile(state.value.searchedCharacterName)
                .onSuccess { profile ->
                    _state.update {
                        it.copy(
                            isCharacterProfileLoading = false,
                            characterProfile = profile.toCharacterProfileUi()
                        )
                    }
                    Timber.d("Successfully loaded characterProfile: $profile")
                }
                .onError { error ->
                    Timber.e("$error", "Failed to load characterProfile")
                    sendError(error.asUiText())
                }
        }
    }

    private fun loadGear() {
        viewModelScope.launch {
            _state.update { it.copy(isGearLoading = true) }
            characterRepository.getGear(state.value.searchedCharacterName)
                .onSuccess { gearList ->
                    val categorizedGears = categorizeGears(gearList)
                    val onlyEquipment = categorizedGears["장비"] ?: emptyList()
                    val onlyAccessories = categorizedGears["장신구"] ?: emptyList()
                    val onlyAbilityStone = categorizedGears["어빌리티 스톤"] ?: emptyList()
                    val onlyBracelet = categorizedGears["팔찌"] ?: emptyList()
                    val gearUiList = onlyEquipment.map { it.toGearUi() }

                    val elixirList = gearUiList.map { it.elixirList }
                    Timber.tag("elixir").d(elixirList.toString())
                    val allElixirs = gearUiList.flatMap { it.elixirList ?: emptyList() }
                        .filter { it.isNotBlank() }

                    val totalElixirSum = gearUiList.sumOf { it.elixirSum }
                    val elixirEffect = findElixirEffect(allElixirs)
                    val effectLevel = when {
                        totalElixirSum >= 40 -> "2단계"
                        totalElixirSum >= 35 -> "1단계"
                        else -> ""
                    }
                    val activeEffect = if (elixirEffect.isNotEmpty() && effectLevel.isNotEmpty()) {
                        "$elixirEffect $effectLevel"
                    } else {
                        ""
                    }

                    _state.update {
                        it.copy(
                            isGearLoading = false,
                            gearList = gearUiList,
                            accessoriesList = onlyAccessories.map { it.toAccessoriesUi() },
                            abilityStone = onlyAbilityStone.firstOrNull()?.toAbilityStoneUi()
                                ?: emptyAbilityStoneUi,
                            bracelet = onlyBracelet.firstOrNull()?.toBraceletUi()
                                ?: emptyBraceletUi,
                            elixir = ElixirUi(
                                total = totalElixirSum,
                                activeEffect = activeEffect
                            )
                        )
                    }
                    Timber.d("Successfully loaded characterProfile: $gearList")
                }
        }
    }


    private fun sendError(message: UiText) {
        viewModelScope.launch {
            if (!hasErrorOccurred) {
                hasErrorOccurred = true
                _events.send(CharacterEvent.Error(message))
            }
        }
    }

    private fun findElixirEffect(elixirList: List<String>): String {
        Timber.tag("elixir_debug").d("Received Elixirs: $elixirList")

        val effectPairs = mapOf(
            "강맹 (질서)" to "강맹 (혼돈)", "달인 (질서)" to "달인 (혼돈)", "선각자 (질서)" to "선각자 (혼돈)",
            "선봉대 (질서)" to "선봉대 (혼돈)", "신념 (질서)" to "신념 (혼돈)", "진군 (질서)" to "진군 (혼돈)",
            "칼날 방패 (질서)" to "칼날 방패 (혼돈)", "행운 (질서)" to "행운 (혼돈)", "회심 (질서)" to "회심 (혼돈)"
        )

        val normalizedElixirs = elixirList.map { it.replace(Regex("\\sLv\\.\\d+"), "").trim() }
        Timber.tag("elixir_debug").d("Normalized Elixirs: $normalizedElixirs")

        for ((order, chaos) in effectPairs) {
            if (normalizedElixirs.any { it.trim() == order } && normalizedElixirs.any { it.trim() == chaos }) {
                Timber.tag("elixir_debug").d("Matched Effect: ${order.substringBefore(" (")}")
                return order.substringBefore(" (")
            }
        }
        return ""
    }

}