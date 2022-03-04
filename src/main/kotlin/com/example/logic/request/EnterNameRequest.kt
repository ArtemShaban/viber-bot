package com.example.logic.request

import com.example.logic.BotLogicState

class EnterNameRequest(state: BotLogicState) : UserRequest<EnterNameRequest.Option>(state) {

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Як я можу до вас звертатись? \n" +
                    "(Вкажи нижче своє імʼя або нік)\n"
            Lang.RU -> "Как я могу к вам обращаться? \n" +
                    "(напишите пожалуйста, ваше имя либо ник) "
            Lang.EN -> "Please write your name to continue"
        }
    }

    override fun getOptions(): Map<Option, String> {
        return emptyMap()
    }

    enum class Option : UserOption
}