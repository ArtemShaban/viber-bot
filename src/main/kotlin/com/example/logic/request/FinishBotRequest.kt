package com.example.logic.request

import com.example.logic.BotLogicState

class FinishBotRequest(state: BotLogicState) : UserRequest<FinishBotRequest.Option>(state) {
    enum class Option {
        OK, VIBER, ZOOM
    }

    override fun getMessage(): String {
        return when (ContactTypeRequest.Option.getContactType(state)) {
            ContactTypeRequest.Option.PHONE_CALL -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UK -> "передаємо ваш номер психологу, який вийде на зв'язок в найближчу годину в телефонному режимі"
                Lang.RU -> "передаем ваш номер психологу, который выйдет на связь в ближайшее время в телефонном режиме"
                Lang.EN -> "We'll give your number to the psychologist, who will contact you in the near future by phone"
            }
            ContactTypeRequest.Option.VIBER_CHAT -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UK -> "просимо перейти за посилання в Viber кімнату де є можливість поговорити з нашими спеціалістами"
                Lang.RU -> "просим перейти за ссылку в Viber комнату, где есть возможность поговорить с нашими специалистами"
                Lang.EN -> "Please follow link to the Viber room where you can talk to our experts"
            }
            ContactTypeRequest.Option.ZOOM_MEETING -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UK -> "просимо перейти за посилання в Zoom кімнату де є можливість поговорити з нашими спеціалістами"
                Lang.RU -> "просим перейти за ссылку в Zoom комнату, где есть возможность поговорить с нашими специалистами"
                Lang.EN -> "Please follow link to the zoom room where you can talk to our experts"
            }
        }
    }

    override fun getOptions(): Map<Option, String> {
        return when (ContactTypeRequest.Option.getContactType(state)) {
            ContactTypeRequest.Option.PHONE_CALL -> mapOf(Pair(Option.OK, "OK"))
            ContactTypeRequest.Option.VIBER_CHAT -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UK -> mapOf(Pair(Option.VIBER, "перейти в Viber кімнату"))
                Lang.RU -> mapOf(Pair(Option.VIBER, "перейти в Viber комнату"))
                Lang.EN -> mapOf(Pair(Option.VIBER, "follow Viber room"))
            }
            ContactTypeRequest.Option.ZOOM_MEETING -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UK -> mapOf(Pair(Option.ZOOM, "перейти в Zoom кімнату"))
                Lang.RU -> mapOf(Pair(Option.ZOOM, "перейти в Zoom комнату"))
                Lang.EN -> mapOf(Pair(Option.ZOOM, "follow Zoom room"))
            }
        }
    }
}