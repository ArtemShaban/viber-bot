package com.example.logic.request

import com.example.logic.BotLogicState

class CheckStateRequest(state: BotLogicState) : UserRequest<CheckStateRequest.Option>(state) {

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Якщо ви зараз відчуваєте що у вас гострий стан і спілкування через бот ще 2-3 хвилини ви не витримаєте, натисніть кнопку \"${
                getOptionMessage(
                    Option.EMERGENCY
                )
            }\" або \"${getOptionMessage(Option.FINE)}\""
            Lang.RU -> "Если вы сейчас чувствуете, что у вас острое состояние и общение через бот еще 2-3 минуты вы не выдержите, нажмите кнопку \"${
                getOptionMessage(
                    Option.EMERGENCY
                )
            }\" или \"${getOptionMessage(Option.FINE)}\""
            Lang.EN -> "If you feel like you have an acute condition and you can't stand communication though the bot for another 2-3 minutes, click the \"${
                getOptionMessage(
                    Option.EMERGENCY
                )
            }\" or \"${getOptionMessage(Option.FINE)}\""
        }
    }

    override fun getOptions(): Map<Option, String> {
        return mapOf(
            Pair(Option.EMERGENCY, getOptionMessage(Option.EMERGENCY)),
            Pair(Option.FINE, getOptionMessage(Option.FINE))
        )
    }

    private fun getOptionMessage(option: Option): String {
        return when (option) {
            Option.EMERGENCY -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "екстренна допомога"
                Lang.RU -> "экстренная помощь"
                Lang.EN -> "emergency help"
            }
            Option.FINE -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "я ок, тримаюся"
                Lang.RU -> "я ок, держусь"
                Lang.EN -> "I'm OK, I'm holding on button"
            }
        }
    }

    enum class Option : UserOption {
        EMERGENCY {
            override fun getUrl(): String {
                return "https://invite.viber.com/?g2=AQAGX5EYp8g%2Fx07ONU9p%2B12mDlPDHB1LVH2OUAzqjH81OLrAx54%2FLk4JEeELq5Jk"
            }
        },
        FINE
    }
}