package com.example.logic

import com.example.google.sheets.SpreadsheetSender
import com.example.logic.request.viber.CheckStateRequest
import com.example.logic.request.viber.ChooseSourceRequest
import com.example.logic.request.viber.ContactTypeRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SpreadsheetLogic {
    private val spreadsheetSender = SpreadsheetSender()

    fun addUserDataToSpreadsheet(state: ViberBotLogic.State, userInfo: UserInfo?) {
        val userCondition = CheckStateRequest.UserState.valueOf(state.state!!)
        val userConditionAnswer = CheckStateRequest(state).getOptions()[userCondition]

        val stressSource = state.stressSource?.let { ChooseSourceRequest.Option.valueOf(it) }
        val stressSourceAnswer = ChooseSourceRequest(state).getOptions()[stressSource]

        val contactType = state.contactType?.let { ContactTypeRequest.ContactType.valueOf(it) }
        val contactTypeAnswer = ContactTypeRequest(state).getOptions()[contactType]

        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/M/y H:m:ss"))

        spreadsheetSender.append(listOf(
            /*status*/          "Новый",
            /*date*/             date,
            /*Viber username*/  (userInfo?.name ?: ""),
            /*entered name*/    state.userName.orEmpty(),
            /*condition*/       userConditionAnswer.orEmpty(),
            /*stress level*/    state.stressLevel?.toString() ?: "",
            /*stress source*/   stressSourceAnswer.orEmpty(),
            /*contact type*/    contactTypeAnswer.orEmpty(),
            /*phone number*/    state.phoneNumber.orEmpty(),
            /*chosen lang*/     state.userLang.orEmpty(),

            /*Viber user id*/   (userInfo?.id ?: ""),
            /*Viber avatar*/    (userInfo?.avatar ?: ""),
            /*Viber country*/   (userInfo?.country ?: "")
        ))
    }


}