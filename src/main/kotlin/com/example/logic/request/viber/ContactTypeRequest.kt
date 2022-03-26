package com.example.logic.request.viber

import com.example.logic.ViberBotLogic
import com.example.logic.request.Lang
import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class ContactTypeRequest(override val state: ViberBotLogic.State) :
    UserRequest<ContactTypeRequest.ContactType>(state) {

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Як вам зручно спілкуватися зі спеціалістом/психологом:"
            Lang.RU -> "Как вам удобно общаться со специалистом/психологом:"
            Lang.EN -> "How convenient it is for you to communicate with a specialist / psychologist:"
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
        }
    }

    enum class ContactType : UserOption {
        PHONE_CALL, ZOOM_MEETING, VIBER_CHAT;

        companion object {
            fun get(state: ViberBotLogic.State): ContactType =
                valueOf(state.contactType!!)
        }
    }
}