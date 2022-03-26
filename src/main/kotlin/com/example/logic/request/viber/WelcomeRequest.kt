package com.example.logic.request.viber

import com.example.logic.ViberBotLogic
import com.example.logic.request.Lang
import com.example.logic.request.UserRequest

class WelcomeRequest(state: ViberBotLogic.State) : UserRequest<Lang>(state) {
    override fun getMessage(): String {
        return "Доброго дня!" +
                "\nДякуємо, що звернулися до нашої служби #психологічної підтримки!" +
                "\nНаші спеціалісти готові вам надати допомогу в зручному для вас форматі, який допоможе визначити Я чат-бот." +
                "\n\nВибери мову спілкування" +
                "\nChoose language"
    }

    override fun getOptions(): Map<Lang, String> {
        return mapOf(
            Pair(Lang.UA, "Українська \uD83C\uDDFA\uD83C\uDDE6"),
            Pair(Lang.EN, "English"),
//            Pair(Lang.RU, "Русский"),
        )
    }
}