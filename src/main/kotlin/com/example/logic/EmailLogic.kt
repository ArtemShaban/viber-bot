package com.example.logic

import com.example.email.EmailSender
import com.example.logic.request.viber.CheckStateRequest
import com.example.logic.request.viber.ChooseSourceRequest
import com.example.logic.request.viber.ContactTypeRequest

class EmailLogic {
    private val emailSender = EmailSender()

    fun sendEmail(state: ViberBotLogic.State) {
        val userCondition = CheckStateRequest.UserState.valueOf(state.state!!)
        val userConditionAnswer = CheckStateRequest(state).getOptions()[userCondition]

        val stressSource = state.stressSource?.let { ChooseSourceRequest.Option.valueOf(it) }
        val stressSourceAnswer = ChooseSourceRequest(state).getOptions()[stressSource]

        val contactType = state.contactType?.let { ContactTypeRequest.ContactType.valueOf(it) }
        val contactTypeAnswer = ContactTypeRequest(state).getOptions()[contactType]

        emailSender.sendEmail(
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

    private fun getTitle(state: ViberBotLogic.State): String {
        return when (
            ContactTypeRequest.ContactType.valueOf(state.contactType!!)) {
            ContactTypeRequest.ContactType.PHONE_CALL -> "[звонок] Запрос на психологическую помощь из viber чат бота"
            ContactTypeRequest.ContactType.VIBER_CHAT -> "[viber] Пользователь запросил помощь через viber из viber чат бота"
            ContactTypeRequest.ContactType.ZOOM_MEETING -> "[zoom] Пользователь запросил помощь через zoom из viber чат бота"
            ContactTypeRequest.ContactType.VIBER_GROUP -> "[viber group] Пользователь запросил помощь через viber group из viber чат бота"
        }
    }
}