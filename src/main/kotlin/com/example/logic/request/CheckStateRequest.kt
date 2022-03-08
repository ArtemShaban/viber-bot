package com.example.logic.request

import com.example.logic.BotLogicState

class CheckStateRequest(state: BotLogicState) : UserRequest<CheckStateRequest.Option>(state) {

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Будь ласка, виберіть тип допомоги: екстрена допомога або можу продовжити"
            Lang.RU -> "Пожалуйста, выберите тип помощи : экстренная помощь либо могу продолжить"
            Lang.EN -> "Please select the type of assistance: emergency help  or I can continue"
        }
    }

    override fun getOptions(): Map<Option, String> {
        return mapOf(
            Pair(Option.EMERGENCY, getOptionMessage(Option.EMERGENCY)),
            Pair(Option.FINE, getOptionMessage(Option.FINE))
        )
    }

    private fun getOptionMessage(option: Option): String {
        return when (option) {
            Option.EMERGENCY -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "екстренна допомога"
                Lang.RU -> "экстренная помощь"
                Lang.EN -> "emergency help"
            }
            Option.FINE -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "я ок, тримаюся"
                Lang.RU -> "я ок, держусь"
                Lang.EN -> "I'm OK, I'm holding on button"
            }
        }
    }

    enum class Option : UserOption {
        EMERGENCY,
        FINE
    }
}