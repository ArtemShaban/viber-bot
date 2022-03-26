package com.example.logic.request.viber

import com.example.logic.ViberBotLogic
import com.example.logic.request.Lang
import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class FinishBotRequest(override val state: ViberBotLogic.State) :
    UserRequest<FinishBotRequest.Option>(state) {
    enum class Option : UserOption {
        RESTART,
        ZOOM {
            override fun getUrl(): String {
                return "https://us02web.zoom.us/j/86374750332?pwd=R1FNWWdSRzhIV0V1QmRzOG9GbFlEQT09"
            }
        }
    }

    override fun getMessage(): String {
        return when (ContactTypeRequest.ContactType.get(state)) {
            ContactTypeRequest.ContactType.PHONE_CALL -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "Передаємо ваш номер психологу, який вийде на зв'язок в найближчу годину в телефонному режимі. Дякуємо будьте певні - Ми переможемо!"
                Lang.RU -> "Передаем ваш номер психологу, который выйдет на связь в ближайшее время в телефонном режиме. Благодарим и уверяем - Мы победим!"
                Lang.EN -> "We'll give your number to the psychologist, who will contact you in the near future by phone. Thank you and be sure."
            }
            ContactTypeRequest.ContactType.VIBER_CHAT -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "Передаємо ваш номер психологу, який вийде на зв'язок в найближчу годину в Viber. Дякуємо будьте певні - Ми переможемо!"
                Lang.RU -> "Передаем ваш номер психологу, который свяжется с вами ближайшее время в Viber. Благодарим и уверяем - Мы победим!"
                Lang.EN -> "We'll give your number to the psychologist, who will contact you in the near future by Viber. Thank you and be sure."
            }
            ContactTypeRequest.ContactType.ZOOM_MEETING -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "Просимо перейти до Zoom кімнати, де наш координатор направить вас до чергового психолога. Дякуємо будьте певні - Ми переможемо!"
                Lang.RU -> "Просим перейти в Zoom комнату, где наш координатор направит вас к дежурному психологу. Благодарим и уверяем - Мы победим!"
                Lang.EN -> "Please follow Zoom room, where our coordinator will direct you to the psychologist on duty. Thank you and be sure."
            }
        }
    }

    override fun getOptions(): Map<Option, String> {
        val option = when (ContactTypeRequest.ContactType.get(state)) {
            ContactTypeRequest.ContactType.ZOOM_MEETING -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> Pair(Option.ZOOM, "перейти в Zoom кімнату")
                Lang.RU -> Pair(Option.ZOOM, "перейти в Zoom комнату")
                Lang.EN -> Pair(Option.ZOOM, "follow Zoom room")
            }
            else -> null
        }
        val options = if (option != null) mutableMapOf(option) else mutableMapOf()
        options[Option.RESTART] = when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "почати спочатку"
            Lang.RU -> "начать с начала"
            Lang.EN -> "start from beginning"
        }
        return options
    }
}