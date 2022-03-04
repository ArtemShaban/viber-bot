package com.example.logic.request

import com.example.logic.BotLogicState

class ContactTypeRequest(state: BotLogicState) : UserRequest<ContactTypeRequest.Option>(state) {

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UK -> "Як вам зручно спілкуватися зі спеціалістом/психологом:"
            Lang.RU -> "Как вам удобно общаться со специалистом/психологом:"
            Lang.EN -> "How convenient it is for you to communicate with a specialist / psychologist:"
        }
    }

    override fun getOptions(): Map<Option, String> {
        return Option.values().associate { Pair(it, getOptionMessage(it)) }
    }

    private fun getOptionMessage(option: Option): String {
        return when (option) {
            Option.VIBER_CHAT -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UK -> "Viber чат в текстовому форматі"
                Lang.RU -> "Viber чат в текстовом формате"
                Lang.EN -> "Viber chat in text format"
            }
            Option.PHONE_CALL -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UK -> "Телефоний дзвінок"
                Lang.RU -> "Телефонный звонок"
                Lang.EN -> "Phone call"
            }
            Option.ZOOM_MEETING -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UK -> "Zoom зустріч з психологом"
                Lang.RU -> "Zoom встреча с психологом"
                Lang.EN -> "Zoom meeting with a psychologist"
            }
        }
    }

    enum class Option {
        VIBER_CHAT, PHONE_CALL, ZOOM_MEETING
    }
}