package com.example.logic

import com.example.email.EmailSender
import com.example.logic.request.CheckStateRequest
import com.example.logic.request.ChooseSourceRequest
import com.example.logic.request.ContactTypeRequest

class EmailLogic {

    fun sendEmail(state: BotLogicState) {
        val userCondition = CheckStateRequest.Option.valueOf(state.state!!)
        val userConditionAnswer = CheckStateRequest(state).getOptions()[userCondition]

        val stressSource = state.stressSource?.let { ChooseSourceRequest.Option.valueOf(it) }
        val stressSourceAnswer = ChooseSourceRequest(state).getOptions()[stressSource]

        val contactType = state.contactType?.let { ContactTypeRequest.ContactType.valueOf(it) }
        val contactTypeAnswer = ContactTypeRequest(state).getOptions()[contactType]

        EmailSender()
            .sendEmail(
                "my.psycholog.help@gmail.com",
                getTitle(state),
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

    private fun getTitle(state: BotLogicState): String {
        if (!state.stateFine!!) {
            return "[экстренная помощь] Пользователь запросил экстренную помощь"
        }

        val contactType = ContactTypeRequest.ContactType.valueOf(state.contactType!!)
        return when (contactType) {
            ContactTypeRequest.ContactType.PHONE_CALL -> "[звонок] Запрос на психологическую помощь из viber чат бота"
            ContactTypeRequest.ContactType.VIBER_CHAT -> "[viber] пользователь запросил помощь через viber"
            ContactTypeRequest.ContactType.ZOOM_MEETING -> "[zoom] пользователь запросил помощь через zoom"
        }
    }
}