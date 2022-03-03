package com.example.logic.request

import com.example.logic.BotLogicState

class WelcomeRequest(state: BotLogicState) : UserRequest<Lang, Lang>(state) {
    override fun getMessage(): String {
        return "Доброго дня! Дякуємо, що звернулися до нашої служби #психологічної підтримки!\n" +
                "Наші спеціалісти готові вам надати допомогу в зручному для вас форматі, який допоможе визначити Я чат-бот. \n"
    }

    override fun getOptions(): Map<Lang, String> {
        return mapOf(
            Pair(Lang.UK, "Укр"),
            Pair(Lang.RU, "Рус"),
            Pair(Lang.EN, "Eng")
        )
    }

    override fun createResponse(responseData: Lang): Response<Lang> {
        return Response(this, responseData)
    }
}