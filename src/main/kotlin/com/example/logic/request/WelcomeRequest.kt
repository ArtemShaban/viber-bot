package com.example.logic.request

import com.example.logic.BotLogicState

class WelcomeRequest(state: BotLogicState) : UserRequest<Lang, Lang>(state) {
    override fun getMessage(): String {
        return "Доброго дня! Дякуємо, що звернулися до нашої служби #психологічної підтримки!\n" +
                "Наші спеціалісти готові вам надати допомогу в зручному для вас форматі, який допоможе визначити Я чат-бот. \n"
    }

    override fun getOptions(): Map<Lang, String> {
        return mapOf(
            Pair(Lang.UK, "Укр(\uD83C\uDDFA\uD83C\uDDE6)"),
            Pair(Lang.RU, "Рус(\uD83C\uDDF7\uD83C\uDDFA)"),
            Pair(Lang.EN, "Eng(\uD83C\uDDFA\uD83C\uDDF8)")
        )
    }

    override fun createResponse(responseData: Lang): Response<Lang> {
        return Response(this, responseData)
    }
}