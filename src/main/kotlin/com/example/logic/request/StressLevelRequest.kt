package com.example.logic.request

import com.example.logic.BotLogicState

class StressLevelRequest(state: BotLogicState) : UserRequest<StressLevelRequest.Option>(state) {
    enum class Option : UserOption

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Нам важлива ваша безпека, тому поважаємо і гарантуємо  приватність та конфіденційність нашої розмови" +
                    "\n\nОцініть, будь ласка, рівень вашого стресу/тривоги за шкалою від 1 до 10 (ціле число) на цю мить?" +
                    "\n1 - паніка; 10 - спокій"
            Lang.RU -> "Для нас важна ваша безопасность, поэтому мы уважаем и гарантируем приватность и конфиденциальность нашего разговора." +
                    "\n\nОцените, пожалуйста, уровень вашего стресса/тревоги по шкале от 1 до 10 на этот момент?" +
                    "\n1 - паника; 10 - комфорт"
            Lang.EN -> "Your safety - the most important for us, therefore we respect and guarantee the privacy and confidentiality of our conversation." +
                    "\n\nTry to rate your stress/anxiety level on a scale from 1 to 10 for the current moment" +
                    "\n1 - panic; 10 - comfort"
        }
    }

    override fun getOptions(): Map<Option, String> {
        return emptyMap()
    }
}