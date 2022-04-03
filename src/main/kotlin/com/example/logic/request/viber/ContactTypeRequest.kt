package com.example.logic.request.viber

import com.example.logic.ViberBotLogic
import com.example.logic.request.Lang
import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class ContactTypeRequest(override val state: ViberBotLogic.State) :
    UserRequest<ContactTypeRequest.ContactType>(state) {

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Для більш оперативної відповіді ви можете звернутися за підтримкою в зручному для вас форматі, спеціалісти платформи працюють з 8 до 22 щодня!" +
                    "\nОберіть ваш варіант і ви автоматично перейдете в Zoom, Viber або очікуйте дзвінок в найближчий час за номером який ви вказали."
            Lang.RU -> "Для более оперативного ответа вы можете обратиться за поддержкой в удобном для вас формате, специалисты платформы работают с 8 до 22 ежедневно!" +
                    "\nВыберите ваш вариант и вы автоматически перейдете в Zoom, Viber или ждите звонок в ближайшее время по номеру, который вы указали."
            Lang.EN -> "For a more prompt response you can ask for support in a format convenient for you, the platform specialists work from 8 to 22 daily!" +
                    "\nChoose your option and you will automatically switch to Zoom, Viber or wait call soon at the number you specified."
        }
    }

    override fun getOptions(): Map<ContactType, String> {
        return ContactType.values().associateWith { getOptionMessage(it) }
    }

    private fun getOptionMessage(contactType: ContactType): String {
        return when (contactType) {
            ContactType.VIBER_CHAT -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "Viber чат в текстовому форматі"
                Lang.RU -> "Viber чат в текстовом формате"
                Lang.EN -> "Viber chat in text format"
            }
            ContactType.PHONE_CALL -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "Телефонний дзвінок"
                Lang.RU -> "Телефонный звонок"
                Lang.EN -> "Phone call"
            }
            ContactType.ZOOM_MEETING -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "Zoom зустріч з психологом (8.00-22.00)"
                Lang.RU -> "Zoom встреча с психологом (8.00-22.00)"
                Lang.EN -> "Zoom meeting with a psychologist (8.00-22.00)"
            }
            ContactType.VIBER_GROUP -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "Viber група для швидкої відповіді"
                Lang.RU -> "Viber группа для быстрого ответа"
                Lang.EN -> "Viber group for quick answer"
            }
        }
    }

    enum class ContactType : UserOption {
        ZOOM_MEETING, VIBER_GROUP, VIBER_CHAT, PHONE_CALL;

        companion object {
            fun get(state: ViberBotLogic.State): ContactType =
                valueOf(state.contactType!!)
        }
    }
}