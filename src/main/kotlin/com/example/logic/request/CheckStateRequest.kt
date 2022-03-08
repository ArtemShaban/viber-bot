package com.example.logic.request

import com.example.logic.BotLogicState

class CheckStateRequest(state: BotLogicState) : UserRequest<CheckStateRequest.UserState>(state) {

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Будь ласка, виберіть тип допомоги: екстрена допомога або можу продовжити"
            Lang.RU -> "Пожалуйста, выберите тип помощи : экстренная помощь либо могу продолжить"
            Lang.EN -> "Please select the type of assistance: emergency help  or I can continue"
        }
    }

    override fun getOptions(): Map<UserState, String> {
        return mapOf(
            Pair(UserState.EMERGENCY, getOptionMessage(UserState.EMERGENCY)),
            Pair(UserState.FINE, getOptionMessage(UserState.FINE))
        )
    }

    private fun getOptionMessage(userState: UserState): String {
        return when (userState) {
            UserState.EMERGENCY -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "екстренна допомога"
                Lang.RU -> "экстренная помощь"
                Lang.EN -> "emergency help"
            }
            UserState.FINE -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "я ок, тримаюся"
                Lang.RU -> "я ок, держусь"
                Lang.EN -> "I'm OK, I'm holding on button"
            }
        }
    }

    enum class UserState : UserOption {
        EMERGENCY,
        FINE;

        companion object {
            fun get(state: BotLogicState): UserState =
                valueOf(state.state!!)
        }
    }
}