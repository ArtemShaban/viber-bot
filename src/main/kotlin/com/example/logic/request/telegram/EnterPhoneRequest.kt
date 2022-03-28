package com.example.logic.request.telegram

import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class EnterPhoneRequest(state: Any) : UserRequest<EnterPhoneRequest.Option>(state) {
    enum class Option : UserOption

    override fun getMessage() =
        "Нам важлива ваша безпека, тому ми поважаємо і гарантуємо приватність та конфіденційність нашої розмови." +
                "\n\nВаш контактний номер телефону - запорука того, що наш спеціаліст зможе зв'язатись із вами." +
                "\nНапишіть в наступному повідомленні цей контактний номер. Якщо бажаєте - запропонуйте інший зручний для вас канал зв'язку."

    override fun getOptions(): Map<Option, String> = emptyMap()
}