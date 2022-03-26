package com.example.logic.request.viber

import com.example.logic.ViberBotLogic
import com.example.logic.request.Lang
import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class EmergencyHelpBotRequest(override val state: ViberBotLogic.State) :
    UserRequest<EmergencyHelpBotRequest.Option>(state) {

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Щоб оперативно отримати допомогу, перейдіть в Вайбер групу за кнопкою нижче, і напишіть коротке повідомлення." +
                    "\nНаприклад: \"Хочу щоб мені допомогли . Мій контактний телефон: xxx-xx-xxxxxx\""
            Lang.RU -> "Чтобы оперативно получить помощь, перейдите в Вайбер группу по кнопке ниже, и напишите короткое сообщение." +
                    "\nНапример:\"Хочу получить консультацию. Мой номер телефона: xxx-xx-xxxxxx\""
            Lang.EN -> "To get help quickly, go to the Viber group by clicking the button below and write any message." +
                    "\nFor example: \"Hello, I need emergency assistance. My contact phone: xxx-xx-xxxxxx\""
        }
    }

    override fun getOptions(): Map<Option, String> {
        return Option.values().associateWith { getOptionMessage(it) }
    }

    private fun getOptionMessage(option: Option): String {
        return when (option) {
            Option.VIBER ->
                when (Lang.valueOf(state.userLang!!)) {
                    Lang.UA -> "перейти в Viber групу"
                    Lang.RU -> "перейти в Viber группу"
                    Lang.EN -> "follow Viber group"
                }
            Option.RESTART ->
                when (Lang.valueOf(state.userLang!!)) {
                    Lang.UA -> "почати спочатку"
                    Lang.RU -> "начать с начала"
                    Lang.EN -> "start from beginning"
                }
        }
    }

    enum class Option : UserOption {
        VIBER {
            override fun getUrl(): String {
                return "https://invite.viber.com/?g2=AQAGX5EYp8g%2Fx07ONU9p%2B12mDlPDHB1LVH2OUAzqjH81OLrAx54%2FLk4JEeELq5Jk"
            }
        },
        RESTART
    }
}