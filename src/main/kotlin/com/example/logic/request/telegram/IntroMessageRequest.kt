package com.example.logic.request.telegram

import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class IntroMessageRequest(state: Any) : UserRequest<IntroMessageRequest.Option>(state) {
    enum class Option : UserOption { CONTINUE }

    override fun getMessage() = "Доброго дня. Зараз кожному з нас може бути:\n" +
            "✅ Страшно\n" +
            "✅ Тревожно\n" +
            "✅ Незрозуміло\n" +
            "Але це нормальна реакція на ненормальні обставини! \n" +
            "Саме тому спеціалісти служби Психологічної підтримки “Перемога” працюють в режимі \n" +
            "Безкоштовно / щодня / з 8 до 22. \n" +
            "І кожен може скористатися можливістю отримати підтримку. \n" +
            "Зробіть декілька наступних кроків, щоб допомогти нам швидко з вами зв'язатися.\n"

    override fun getOptions() = mapOf(Pair(Option.CONTINUE, "Продовжити"))
}