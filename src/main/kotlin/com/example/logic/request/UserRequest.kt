package com.example.logic.request

abstract class UserRequest<TOption>(open val state: Any) where TOption : Enum<TOption>, TOption : UserOption {
    abstract fun getOptions(): Map<TOption, String>
    abstract fun getMessage(): String
}