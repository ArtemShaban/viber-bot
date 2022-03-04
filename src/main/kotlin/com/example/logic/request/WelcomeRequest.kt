package com.example.logic.request

import com.example.logic.BotLogicState

class WelcomeRequest(state: BotLogicState) : UserRequest<Lang, Lang>(state) {
    override fun getMessage(): String {
        return "Доброго дня!" +
                "\nДякуємо, що звернулися до нашої служби #психологічної підтримки!" +
                "\nНаші спеціалісти готові вам надати допомогу в зручному для вас форматі, який допоможе визначити Я чат-бот." +
                "\n\nВибери мову спілкування" +
                "\nChoose language"
    }

    override fun getOptions(): Map<Lang, String> {
        return mapOf(
            Pair(Lang.UK, "Українська \uD83C\uDDFA\uD83C\uDDE6"),
            Pair(Lang.EN, "English \uD83C\uDDFA\uD83C\uDDF8"),
            Pair(Lang.RU, "Русский \uD83C\uDDF7\uD83C\uDDFA"),
        )
    }

    override fun createResponse(responseData: Lang): Response<Lang> {
        return Response(this, responseData)
    }
}