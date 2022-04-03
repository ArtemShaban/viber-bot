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
            Lang.UA -> "Наш спеціаліст зможе швидко відповісти вам, якщо ви вкажете ваш номер телефону. Ми дуже хочемо вам допомогти, тому і просимо написати контактний номер телефону."
            Lang.RU -> "Наш специалист сможет быстро ответить вам, если вы укажете номер телефона. Мы очень хотим вам помочь, потому и просим написать контактный номер телефона."
            Lang.EN -> "Our specialist will be able to answer you quickly if you provide your phone number. We really want to help you, so please write your contact phone number."
        }
    }

    override fun getOptions(): Map<Option, String> {
        return emptyMap()
    }
}