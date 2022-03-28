package com.example.logic.request.viber

import com.example.logic.ViberBotLogic
import com.example.logic.request.Lang
import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class EnterPhoneRequest(override val state: ViberBotLogic.State) :
    UserRequest<EnterPhoneRequest.Option>(state) {
    enum class Option : UserOption

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Для швидкої відповіді нашого спеціаліста, просимо вас залишити ваш номер телефону, для того щоб наш спеціаліст міг зв'язатися з вами в приватному чаті."
            Lang.RU -> "Для быстрого ответа на нашего специалиста, просим вас оставить ваш контактный номер или на следующем шаге перейти в чат Viber группы."
            Lang.EN -> "For a quick response from our specialist, please leave your contact number or in the next step go to the Viber group chat."
        }
    }

    override fun getOptions(): Map<Option, String> {
        return emptyMap()
    }
}