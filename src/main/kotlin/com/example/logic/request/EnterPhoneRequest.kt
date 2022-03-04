package com.example.logic.request

import com.example.logic.BotLogicState

class EnterPhoneRequest(state: BotLogicState) : UserRequest<EnterPhoneRequest.Option>(state) {
    enum class Option

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UK -> "Залиште ваш контактний номер і зараз ми передаємо ваш номер психологу, який вийде на зв'язок в найближчу годину в телефонному режимі."
            Lang.RU -> "Оставьте ваш контактный номер и сейчас мы передаем ваш номер психологу, который выйдет на связь в ближайшее время в телефонном режиме."
            Lang.EN -> "Please leave your contact information and we will give your number to the psychologist, who will contact you in the near future by phone."
        }
    }

    override fun getOptions(): Map<Option, String> {
        return emptyMap()
    }
}