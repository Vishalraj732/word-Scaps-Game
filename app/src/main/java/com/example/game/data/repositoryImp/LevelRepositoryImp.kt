package com.example.game.data.repositoryImp

import android.content.Context
import com.example.game.domain.model.game.Level
import com.example.game.domain.repository.game.LevelRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class LevelRepositoryImp @Inject constructor(
    @ApplicationContext private val context: Context,
): LevelRepository {

    override suspend fun loadLevelsFromJson(): List<Level> {
        return try {
            val inputStream = context.assets.open("levels.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            Json.decodeFromString(jsonString)

        } catch (e: Exception) {
            e.printStackTrace()
            listOf(
                Level(1, listOf('E', 'R', 'R'), emptyList())
            )
        }
    }
}