package com.hjw0623.character.presentation.components.tab.gear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hjw0623.character.presentation.mockup.mockAbilityStoneContent
import com.hjw0623.character.presentation.mockup.mockBraceletContent
import com.hjw0623.character.presentation.mockup.mockElixirContent
import com.hjw0623.character.presentation.mockup.mockEquipmentContent
import com.hjw0623.character.presentation.mockup.mockGemContent
import com.hjw0623.character.presentation.mockup.mockJewelryContent
import com.hjw0623.character.presentation.mockup.mockTranscendenceUi
import com.hjw0623.character.presentation.model.gear.AbilityStoneUi
import com.hjw0623.character.presentation.model.gear.BraceletUi
import com.hjw0623.character.presentation.model.gear.GearUi
import com.hjw0623.character.presentation.model.gear.AccessoriesUi
import com.hjw0623.character.presentation.model.gear.ElixirUi
import com.hjw0623.character.presentation.model.gear.GemsUi
import com.hjw0623.character.presentation.model.gear.TranscendenceUi
import com.hjw0623.core.presentation.designsystem.LostarkTheme

@Composable
fun GearList(
    gearUis: List<GearUi>,
    accessoriesUis: List<AccessoriesUi>,
    abilityStoneUi: AbilityStoneUi,
    braceletUi: BraceletUi,
    elixirUi: ElixirUi,
    transcendenceUi: TranscendenceUi,
    gemsList: List<GemsUi>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val order = listOf("투구", "어깨", "상의", "하의", "장갑", "무기")
                gearUis.sortedBy { order.indexOf(it.type) }
                    .forEach { item ->
                        EquipmentListItem(item)
                    }
            }
            Column(
                modifier = Modifier
                    .weight(0.5f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                accessoriesUis.forEach { item ->
                    AccessoriesListItem(item)
                }
                AbilityStoneItem(abilityStoneUi)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1.4f)
            ) {
                BraceletItem(braceletUi)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                ElixirItem(elixirUi)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                TranscendenceItem(transcendenceUi)
            }
        }
        GemsList(gemsList)
    }
}


@Preview
@Composable
fun GearListPreview() {
    val equipmentList = listOf(
        mockEquipmentContent(),
        mockEquipmentContent(),
        mockEquipmentContent(),
        mockEquipmentContent(),
        mockEquipmentContent(),
        mockEquipmentContent()
    )
    val jewelryList = listOf(
        mockJewelryContent(),
        mockJewelryContent(),
        mockJewelryContent(),
        mockJewelryContent(),
        mockJewelryContent()
    )
    val gemList = listOf(
        mockGemContent(),
        mockGemContent(),
        mockGemContent(),
        mockGemContent(),
        mockGemContent()
    )

    LostarkTheme {
        GearList(
            gearUis = equipmentList,
            accessoriesUis = jewelryList,
            abilityStoneUi = mockAbilityStoneContent(),
            braceletUi = mockBraceletContent(),
            elixirUi = mockElixirContent(),
            transcendenceUi = mockTranscendenceUi(),
            gemsList = gemList
        )
    }
}


