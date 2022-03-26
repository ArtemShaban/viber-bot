package com.example.logic.request.viber

import com.example.logic.ViberBotLogic
import com.example.logic.request.Lang
import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class ChooseSourceRequest(override val state: ViberBotLogic.State) :
    UserRequest<ChooseSourceRequest.Option>(state) {

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Що вас турбує, оберіть один варіант відповідно до стану зараз:"
            Lang.RU -> "Что вас беспокоит, выберите один вариант по состоянию на сейчас:"
            Lang.EN -> "What worries you the most, select an option:"
        }
    }

    override fun getOptions(): Map<Option, String> {
        return Option.values().associate { Pair(it, getOptionMessage(it)) }
    }

    private fun getOptionMessage(option: Option): String {
        return when (option) {
            Option.PHYSICAL -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "фізичні, тілесні симптоми/нездужання"
                Lang.RU -> "физические, телесные симптомы/недомогания"
                Lang.EN -> "physical, bodily symptoms/diseases"
            }
            Option.EMOTIONALLY -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "емоційні переживання"
                Lang.RU -> "эмоциональные переживания "
                Lang.EN -> "emotional experiences"
            }
            Option.CERTAIN -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "всі разом"
                Lang.RU -> "всё вместе"
                Lang.EN -> "together"
            }
            Option.ANXIOUS -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "просто тривожно"
                Lang.RU -> "просто тревожно"
                Lang.EN -> "just anxious"
            }
        }
    }

    enum class Option : UserOption {
        PHYSICAL, EMOTIONALLY, CERTAIN, ANXIOUS
    }
}