package com.example.logic.request.viber

import com.example.logic.ViberBotLogic
import com.example.logic.request.Lang
import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class CheckStateRequest(override val state: ViberBotLogic.State) :
    UserRequest<CheckStateRequest.UserState>(state) {

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Будь ласка, виберіть тип допомоги: екстрена допомога або можу продовжити"
            Lang.RU -> "Пожалуйста, выберите тип помощи : экстренная помощь либо могу продолжить"
            Lang.EN -> "Please select the type of assistance: emergency help  or I can continue"
        }
    }

    override fun getOptions(): Map<UserState, String> {
        return mapOf(
            Pair(UserState.FINE, getOptionMessage(UserState.FINE)),
            Pair(UserState.EMERGENCY, getOptionMessage(UserState.EMERGENCY))
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
            fun get(state: ViberBotLogic.State): UserState =
                valueOf(state.state!!)
        }
    }
}