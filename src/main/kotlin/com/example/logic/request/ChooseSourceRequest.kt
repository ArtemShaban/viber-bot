package com.example.logic.request

import com.example.logic.BotLogicState

class ChooseSourceRequest(state: BotLogicState) : UserRequest<ChooseSourceRequest.Option>(state) {

    override fun getMessage(): String {
        return when (Lang.valueOf(state.userLang!!)) {
            Lang.UA -> "Що вас зараз найбільше турбує, оберіть варіант:"
            Lang.RU -> "Что вас сейчас больше всего беспокоит, выберите вариант:"
            Lang.EN -> "What worries you the most, select an option:"
        }
    }

    override fun getOptions(): Map<Option, String> {
        return Option.values().associate { Pair(it, getOptionMessage(it)) }
    }


    private fun getOptionMessage(option: Option): String {
        return when (option) {
            Option.PHYSICAL -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "фізичні / фізіологічні / тілесні складнощі"
                Lang.RU -> "физические / физиологические / телесные сложности"
                Lang.EN -> "physical/physiological bodily difficulties"
            }
            Option.EMOTIONALLY -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "емоціно гострі переживання"
                Lang.RU -> "эмоционально острые переживания"
                Lang.EN -> "emotionally intense experience"
            }
            Option.CERTAIN -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "порушення певних функцій в сприйнятті: галюцинації, шум в голові, звуки/дзвін в вухах …"
                Lang.RU -> "нарушение определенных функций в восприятии: галлюцинации, шум в голове, звуки/звон в ушах …"
                Lang.EN -> "impaired certain function in perception: hallucinations, noise in the head, sounds/ringing in the ears…"
            }
            Option.ANXIOUS -> when (Lang.valueOf(state.userLang!!)) {
                Lang.UA -> "просто тривожно"
                Lang.RU -> "просто тревожно"
                Lang.EN -> "Just anxious"
            }
        }
    }

    enum class Option : UserOption {
        PHYSICAL, EMOTIONALLY, CERTAIN, ANXIOUS
    }
}