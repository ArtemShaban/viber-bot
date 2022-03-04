package com.example.logic

import com.example.email.EmailSender
import com.example.logic.request.ChooseSourceRequest
import com.example.logic.request.ContactTypeRequest

class EmailLogic {

    fun sendEmail(state: BotLogicState) {
        val stressSource = ChooseSourceRequest.Option.valueOf(state.stressSource!!)
        val stressSourceAnswer = ChooseSourceRequest(state).getOptions()[stressSource]

        val contactType = ContactTypeRequest.ContactType.valueOf(state.contactType!!)
        val contactTypeAnswer = ContactTypeRequest(state).getOptions()[contactType]

        EmailSender()
            .sendEmail(
                "my.psycholog.help@gmail.com",
                "Запрос на психологическую помощь из viber чат бота",
                """
                    ${state.userName} запросил помощь через чат-бот. Номер телефона: ${state.phoneNumber}
                    
                    Анкета:
                    имя - ${state.userName}
                    состояние - ${if (state.stateFine!!) "я ок, держусь" else "экстренная помощь"}
                    уровень стресса - ${state.stressLevel}
                    источник стресса - $stressSourceAnswer 
                    тип связи - $contactTypeAnswer
                    номер телефона - ${state.phoneNumber}
                    язык - ${state.userLang}    
                    """
            )
    }
}