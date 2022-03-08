package com.example.logic.request

import com.example.logic.BotLogicState

class ContactTypeRequest(state: BotLogicState) : UserRequest<ContactTypeRequest.ContactType>(state) {

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
                Lang.UA -> "Телефоний дзвінок"
                Lang.RU -> "Телефонный звонок"
                Lang.EN -> "Phone call"
            }
            ContactType.ZOOM_MEETING -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "Zoom зустріч з психологом"
                Lang.RU -> "Zoom встреча с психологом"
                Lang.EN -> "Zoom meeting with a psychologist"
            }
        }
    }

    enum class ContactType : UserOption {
        VIBER_CHAT, PHONE_CALL, ZOOM_MEETING;

        companion object {
            fun get(state: BotLogicState): ContactType =
                valueOf(state.contactType!!)
        }
    }
}