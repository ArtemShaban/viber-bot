package com.example.logic

import com.example.google.sheets.SpreadsheetSender
import com.example.logic.request.viber.CheckStateRequest
import com.example.logic.request.viber.ChooseSourceRequest
import com.example.logic.request.viber.ContactTypeRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SpreadsheetLogic {
    private val spreadsheetSender = SpreadsheetSender()

    fun addViberUserDataToSpreadsheet(state: ViberBotLogic.State, userInfo: UserInfo?) {
        val userCondition = state.state?.let { CheckStateRequest.UserState.valueOf(it) }
        val userConditionAnswer = CheckStateRequest(state).getOptions()[userCondition].orEmpty()

        val chosenLang = state.userLang.orEmpty()
        val enteredUserName = state.userName.orEmpty()
        val enteredPhoneNumber = state.phoneNumber.orEmpty()

        val stressSource = state.stressSource?.let { ChooseSourceRequest.Option.valueOf(it) }
        val stressSourceAnswer = ChooseSourceRequest(state).getOptions()[stressSource].orEmpty()

        val contactType = state.contactType?.let { ContactTypeRequest.ContactType.valueOf(it) }
        val contactTypeAnswer = ContactTypeRequest(state).getOptions()[contactType].orEmpty()

        val stressLevel = state.stressLevel?.toString().orEmpty()

        append(
            userInfo,
            enteredUserName,
            userConditionAnswer,
            stressLevel,
            stressSourceAnswer,
            contactTypeAnswer,
            enteredPhoneNumber,
            chosenLang,
        )
    }

    fun addTelegramUserDataToSpreadsheet(state: TelegramBotLogic.State, userInfo: UserInfo?) {
        val enteredUserName = state.userName.orEmpty()
        val enteredPhoneNumber = state.phoneNumber.orEmpty()

        append(
            userInfo,
            enteredUserName,
            "",
            "",
            "",
            "",
            enteredPhoneNumber,
            "",
        )
    }


    private fun append(
        userInfo: UserInfo?,
        enteredUserName: String,
        userConditionAnswer: String,
        stressLevel: String,
        stressSourceAnswer: String,
        contactTypeAnswer: String,
        enteredPhoneNumber: String,
        chosenLang: String,
    ) {

        val messengerUserName = userInfo?.name.orEmpty()
        val messengerInnerUserId = userInfo?.id.orEmpty()
        val messengerAvatar = userInfo?.avatar.orEmpty()
        val messengerCountry = userInfo?.country.orEmpty()
        val messengerName = userInfo?.messenger.toString()

        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/M/y H:m:ss"))

        spreadsheetSender.append(
            listOf(
                /*status*/            "Новый",
                /*date*/              date,
                /*Messenger*/         messengerName,
                /*TG/Viber username*/ messengerUserName,
                /*entered name*/      enteredUserName,
                /*condition*/         userConditionAnswer,
                /*stress level*/      stressLevel,
                /*stress source*/     stressSourceAnswer,
                /*contact type*/      contactTypeAnswer,
                /*phone number*/      enteredPhoneNumber,
                /*chosen lang*/       chosenLang,

                /*Inner user id*/     messengerInnerUserId,
                /*Viber avatar*/      messengerAvatar,
                /*Viber country*/     messengerCountry
            )
        )
    }

}