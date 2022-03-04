package com.example.logic

import com.example.email.EmailSender
import com.example.logic.request.*

class BotLogic(private val state: BotLogicState = BotLogicState()) {

    fun getNextUserRequest(): UserRequest<*> {
        return when {
            state.userLang == null -> WelcomeRequest(state)
            state.userName == null -> EnterNameRequest(state)
            state.stateFine == null -> CheckStateRequest(state)
            state.stressLevel == null -> RateLevelRequest(state)
            state.stressSource == null -> ChooseSourceRequest(state)
            state.contactType == null -> ContactTypeRequest(state)

            state.phoneNumber != null -> {
                sendEmail(state) //Send email if user asked help by phone call
                FinishBotRequest(state)
            }

            ContactTypeRequest.Option.getContactType(state) == ContactTypeRequest.Option.PHONE_CALL -> EnterPhoneRequest(
                state
            )

            ContactTypeRequest.Option.getContactType(state) == ContactTypeRequest.Option.VIBER_CHAT
                    || ContactTypeRequest.Option.getContactType(state) == ContactTypeRequest.Option.ZOOM_MEETING -> FinishBotRequest(
                state
            )
            else -> WelcomeRequest(state)
        }
    }


}

fun updateState(state: BotLogicState, newInput: String): BotLogicState {
    when {
        state.userLang == null -> state.userLang = newInput
        state.userName == null -> state.userName = newInput
        state.stateFine == null -> state.stateFine =
            if (CheckStateRequest.Option.FINE.name == newInput) true else null
        state.stressLevel == null -> state.stressLevel =
            newInput.toInt() //todo need to validate, if not int - send a message.
        state.stressSource == null -> state.stressSource = newInput
        state.contactType == null -> state.contactType = newInput
        state.phoneNumber == null -> state.phoneNumber = newInput
    }
    return state
}

private fun sendEmail(state: BotLogicState) {
    val stressSource = ChooseSourceRequest.Option.valueOf(state.stressSource!!)
    val stressSourceAnswer = ChooseSourceRequest(state).getOptions()[stressSource]

    val contactType = ContactTypeRequest.Option.valueOf(state.contactType!!)
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

data class BotLogicState(
    var userLang: String? = null,
    var userName: String? = null,
    var stateFine: Boolean? = null,
    var stressLevel: Int? = null,
    var stressSource: String? = null,
    var contactType: String? = null,
    var phoneNumber: String? = null,
)