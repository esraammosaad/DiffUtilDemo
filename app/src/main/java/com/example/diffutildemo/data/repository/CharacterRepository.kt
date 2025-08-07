package com.example.diffutildemo.data.repository

import com.example.diffutildemo.R
import com.example.diffutildemo.data.dto.CharacterDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CharacterRepository {
    private val characters = mutableListOf<CharacterDto>()

    fun addCharacters() {
        characters.addAll(
            listOf(
                CharacterDto(
                    1,
                    "Gumball",
                    "The main character, funny and curious.",
                    R.drawable.gumball
                ),
                CharacterDto(
                    2,
                    "Darwin",
                    "Gumball’s loyal brother, kind and sweet.",
                    R.drawable.darwin
                ),
                CharacterDto(
                    3,
                    "Anais", "The smart sister.", R.drawable.anais
                ),
                CharacterDto(
                    4,
                    "Richard", "The lazy dad.", R.drawable.richard
                ),
                CharacterDto(
                    5,
                    "Nicole", "The strong hardworking mom.", R.drawable.nicole
                )
            )
        )
    }

    fun addCharacter(character: CharacterDto) : Flow<List<CharacterDto>>{
        characters.add(character)
        return flowOf(characters.toList())
    }

    fun getCharacters(): Flow<List<CharacterDto>> {
        return flowOf(characters.toList())
    }

    fun deleteCharacter(
        character: CharacterDto
    ): Flow<List<CharacterDto>> {
        characters.remove(character)
        return flowOf(characters.toList())
    }
}