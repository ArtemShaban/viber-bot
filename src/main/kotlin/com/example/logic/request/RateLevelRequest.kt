package com.example.logic.request

import com.example.logic.BotLogicState

class RateLevelRequest(state: BotLogicState) : UserRequest<RateLevelRequest.Option>(state) {
    enum class Option : UserOption

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Для нас важливо, щоб у вас було розуміння, що ми керуємося тільки вашими інтересами та хочемо допомогти. Тому ми поважаємо і гарантуємо  приватність та конфіденційність нашої розмови.\n\n" +
                    "Оцініть, будь ласка, рівень вашого стресу/тривоги за шкалою від 0 до 10 на цю мить?"
            Lang.RU -> "Для нас важно, чтобы вы понимали, что мы руководствуемся только вашими интересами и хотим помочь. Поэтому мы уважаем и гарантируем приватность и конфиденциальность нашего разговора.\n\n" +
                    "Оцените, пожалуйста, уровень вашего стресса/тревоги по шкале от 0 до 10 на этот момент?"
            Lang.EN -> "It’s important for us to make you understand that we are guided by your interests and really want to help you. Therefore, we respect and guarantee the privacy and confidentiality of our conversation.\n\n" +
                    "Try to rate your stress/anxiety level on a scale from 0 to 10 for the current moment?"
        }
    }

    override fun getOptions(): Map<Option, String> {
        return emptyMap()
    }
}