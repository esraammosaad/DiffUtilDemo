package com.example.diffutildemo.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.example.diffutildemo.Response
import com.example.diffutildemo.data.dto.CharacterDto
import com.example.diffutildemo.data.repository.CharacterRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val characterRepository: CharacterRepository) : ViewModel() {

    private var _characters: MutableStateFlow<Response> = MutableStateFlow(Response.Loading)
    val characters = _characters.asStateFlow()

    init {
        addCharacters()
        getCharacters()
    }

    fun addCharacters() {
        characterRepository.addCharacters()
    }

    fun addCharacter(character: CharacterDto) {
        viewModelScope.launch {
            try {
                val response = characterRepository.addCharacter(character)
                response.catch {
                    _characters.value = Response.Failure(it.message.toString())
                }.collect {
                    _characters.value = Response.Success(it)
                }
            } catch (e: Exception) {
                _characters.value = Response.Failure(e.message.toString())
            }
        }
    }

    fun getCharacters() {
        viewModelScope.launch {
            try {
                val response = characterRepository.getCharacters()
                response.catch {
                    _characters.value = Response.Failure(it.message.toString())
                }.collect {
                    delay(2000)
                    _characters.value = Response.Success(it)
                }
            } catch (e: Exception) {
                _characters.value = Response.Failure(e.message.toString())
            }
        }
    }

    fun deleteCharacter(character: CharacterDto) {
        viewModelScope.launch {
            try {
                val response = characterRepository.deleteCharacter(character)
                response.catch {
                    _characters.value = Response.Failure(it.message.toString())
                }.collect {
                    _characters.value = Response.Success(it)
                }
            } catch (e: Exception) {
                _characters.value = Response.Failure(e.message.toString())
            }
        }
    }
}

class HomeViewModelFactory(private val characterRepository: CharacterRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(characterRepository) as T
    }
}