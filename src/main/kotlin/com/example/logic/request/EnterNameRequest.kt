package com.example.logic.request

class EnterNameRequest(private val lang: Lang) : UserRequest<EnterNameRequest.Option, String> {

    override fun getMessage(): String {
        return when (lang) {
            Lang.UK -> "Як я можу до вас звертатись? \n" +
                    "(Вкажи нижче своє імʼя або нік)\n"
            Lang.RU -> "Как я могу к вам обращаться? \n" +
                    "(напишите пожалуйста, ваше имя либо ник) "
            Lang.EN -> "Please write your name to continue"
        }
    }

    override fun getOptions(): Map<Option, String> {
        return emptyMap()
    }

    enum class Option : UserMessage

    override fun createResponse(responseData: String): Response<String> {
        return Response(this, responseData)
    }
}