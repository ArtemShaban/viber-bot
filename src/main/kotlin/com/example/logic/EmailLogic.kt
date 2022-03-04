package com.example.logic

import com.example.email.EmailSender
import com.example.logic.request.CheckStateRequest
import com.example.logic.request.ChooseSourceRequest
import com.example.logic.request.ContactTypeRequest

class EmailLogic {

    fun sendEmail(state: BotLogicState) {
        val userCondition = CheckStateRequest.Option.valueOf(state.state!!)
        val userConditionAnswer = CheckStateRequest(state).getOptions()[userCondition]

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
                    состояние - $userConditionAnswer
                    уровень стресса - ${state.stressLevel}
                    источник стресса - $stressSourceAnswer 
                    тип связи - $contactTypeAnswer
                    номер телефона - ${state.phoneNumber}
                    язык - ${state.userLang}    
                    """
            )
    }
}