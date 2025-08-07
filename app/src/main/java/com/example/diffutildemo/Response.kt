package com.example.diffutildemo

sealed class Response {
    object Loading : Response()
    data class Success(val data: Any) : Response()
    data class Failure(val message: String) : Response()
}