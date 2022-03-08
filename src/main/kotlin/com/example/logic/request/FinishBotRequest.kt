package com.example.logic.request

import com.example.logic.BotLogicState

class FinishBotRequest(state: BotLogicState) : UserRequest<FinishBotRequest.Option>(state) {
    enum class Option : UserOption {
        RESTART,
        VIBER {
            override fun getUrl(): String {
                return "https://invite.viber.com/?g2=AQAGX5EYp8g%2Fx07ONU9p%2B12mDlPDHB1LVH2OUAzqjH81OLrAx54%2FLk4JEeELq5Jk"
            }
        },
        ZOOM {
            override fun getUrl(): String {
                return "https://us02web.zoom.us/j/86374750332?pwd=R1FNWWdSRzhIV0V1QmRzOG9GbFlEQT09"
            }
        }
    }

    override fun getMessage(): String {
        return when (ContactTypeRequest.ContactType.get(state)) {
            ContactTypeRequest.ContactType.PHONE_CALL -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "Передаємо ваш номер психологу, який вийде на зв'язок в найближчу годину в телефонному режимі"
                Lang.RU -> "Передаем ваш номер психологу, который выйдет на связь в ближайшее время в телефонном режиме"
                Lang.EN -> "We'll give your number to the psychologist, who will contact you in the near future by phone"
            }
            ContactTypeRequest.ContactType.VIBER_CHAT -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "Передаємо ваш номер психологу, який вийде на зв'язок в найближчу годину в Viber"
                Lang.RU -> "Передаем ваш номер психологу, который свяжется с вами ближайшее время в Viber"
                Lang.EN -> "We'll give your number to the psychologist, who will contact you in the near future by Viber"
            }
            ContactTypeRequest.ContactType.ZOOM_MEETING -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "Просимо перейти за посилання в Zoom кімнату де є можливість поговорити з нашими спеціалістами"
                Lang.RU -> "Просим перейти по ссылке в Zoom комнату, где есть возможность поговорить с нашими специалистами"
                Lang.EN -> "Please follow link to the zoom room where you can talk to our experts"
            }
        }
    }

    override fun getOptions(): Map<Option, String> {
        val option = when (ContactTypeRequest.ContactType.get(state)) {
            ContactTypeRequest.ContactType.VIBER_CHAT -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> Pair(Option.VIBER, "перейти в Viber кімнату")
                Lang.RU -> Pair(Option.VIBER, "перейти в Viber комнату")
                Lang.EN -> Pair(Option.VIBER, "follow Viber room")
            }
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