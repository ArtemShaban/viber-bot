package com.example.logic.request.viber

import com.example.logic.ViberBotLogic
import com.example.logic.request.Lang
import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class ContactTypeRequest(override val state: ViberBotLogic.State) :
    UserRequest<ContactTypeRequest.ContactType>(state) {

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Наші спеціалісти працюють з 8 до 22 щодня ви завжди можете отримати підтримку у зручному для вас форматі у той час коли вам це потрібно." +
                    "\nЯк вам зручно спілкуватися зі спеціалістом/психологом:"
            Lang.RU -> "Наши специалисты работают с 8 до 22 ежедневно, вы всегда можете получить поддержку в удобном для вас формате в то время когда вам это нужно." +
                    "\nКак вам удобно общаться со специалистом/психологом:"
            Lang.EN -> "Our specialists work from 8 to 22 every day, you can always get support in a format convenient for you at the time you need it." +
                    "\nHow convenient it is for you to communicate with a specialist / psychologist:"
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
        VIBER_CHAT, ZOOM_MEETING, PHONE_CALL, VIBER_GROUP;

        companion object {
            fun get(state: ViberBotLogic.State): ContactType =
                valueOf(state.contactType!!)
        }
    }
}