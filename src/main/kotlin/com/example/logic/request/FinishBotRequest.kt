package com.example.logic.request

import com.example.email.EmailSender
import com.example.logic.BotLogicState

class FinishBotRequest(state: BotLogicState) : UserRequest<FinishBotRequest.Option>(state) {
    enum class Option : UserOption {
        OK,
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

    init {
        //Send email if user asked help by phone call
        if (ContactTypeRequest.Option.PHONE_CALL == ContactTypeRequest.Option.getContactType(state)) {
            sendEmail(state)
        }
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

    private fun sendEmail(state: BotLogicState) {
        EmailSender()
            .sendEmail(
//                "my.psycholog.help@gmail.com",
                "artemshaban@gmail.com",
                "Запрос на психологическую помощь из viber чат бота",
                "${state.userName} запросил помощь через чат-бот. Номер телефона: ${state.phoneNumber}"
            )
    }
}