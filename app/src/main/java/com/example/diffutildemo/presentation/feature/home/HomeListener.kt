package com.example.diffutildemo.presentation.feature.home

import com.example.diffutildemo.data.dto.CharacterDto

fun interface HomeListener {
    fun onDeleteButtonClicked(character: CharacterDto)
}