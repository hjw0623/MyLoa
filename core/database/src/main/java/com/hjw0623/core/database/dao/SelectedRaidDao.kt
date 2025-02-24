package com.hjw0623.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hjw0623.core.database.entity.SelectedRaidEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedRaidDao {
    @Upsert
    suspend fun upsertSelectedRaid(raid: SelectedRaidEntity)

    @Query("SELECT * FROM selected_raid WHERE characterId = :characterId")
    fun getSelectedRaidsForCharacter(characterId: String): Flow<List<SelectedRaidEntity>>

    @Query("DELETE FROM selected_raid WHERE characterId = :characterId")
    suspend fun deleteSelectedRaids(characterId: String)
}
