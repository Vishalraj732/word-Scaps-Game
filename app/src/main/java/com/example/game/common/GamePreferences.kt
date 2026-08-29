package com.example.game.common

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max

val Context.dataStore by preferencesDataStore(name = "game_prefs")

@Singleton
class GamePreferences @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val LEVEL_INDEX = intPreferencesKey("level_index")
        val MAX_UNLOCKED_KEY = intPreferencesKey("max_unlocked_index")
        val FOUND_WORDS = stringSetPreferencesKey("found_words")
    }

    val gameProgressFlow = context.dataStore.data.map { prefs ->
        val currentIndex = prefs[LEVEL_INDEX] ?: 0
        val maxUnlocked = prefs[MAX_UNLOCKED_KEY] ?: 0
        val words = prefs[FOUND_WORDS] ?: emptySet()

        Triple(currentIndex, maxUnlocked, words)
    }

    suspend fun saveProgress(levelIndex: Int, foundWords: Set<String>) {
        dataStore.edit { prefs ->
            prefs[LEVEL_INDEX] = levelIndex
            prefs[FOUND_WORDS] = foundWords
            val currentMax = prefs[MAX_UNLOCKED_KEY] ?: 0
            prefs[MAX_UNLOCKED_KEY] = max(currentMax, levelIndex)
        }
    }
}