package com.example.logic.request.telegram

import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class EnterPhoneRequest(state: Any) : UserRequest<EnterPhoneRequest.Option>(state) {
    enum class Option : UserOption

    override fun getMessage() =
        "Нам важлива ваша безпека, тому поважаємо і гарантуємо  приватність та конфіденційність нашої розмови. \n\n" +
                "Ваш контактний номер телефону допоможе спеціалісту оперативно з вами зв'язатись, будь ласка, напишіть номер або уточніть спосіб як з вами краще зв'язатися та альтернативний контакт для цього."

    override fun getOptions(): Map<Option, String> = emptyMap()
}