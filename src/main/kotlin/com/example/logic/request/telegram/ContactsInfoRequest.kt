package com.example.logic.request.telegram

import com.example.logic.request.UserOption
import com.example.logic.request.UserRequest

class ContactsInfoRequest(state: Any) : UserRequest<ContactsInfoRequest.Option>(state) {

    enum class Option : UserOption {
        CHAT {
            override fun getUrl(): String {
                return "https://t.me/+iXteXhfM4YQ5MTVi"
            }
        },
        ZOOM {
            override fun getUrl(): String {
                return "https://us02web.zoom.us/j/86374750332?pwd=R1FNWWdSRzhIV0V1QmRzOG9GbFlEQT09"
            }
        },
        FACEBOOK {
            override fun getUrl(): String {
                return "https://www.facebook.com/peremogasupport"
            }
        },
        INSTAGRAM {
            override fun getUrl(): String {
                return "https://instagram.com/peremogasupport?utm_medium=copy_link"
            }
        }
    }

    override fun getMessage() =
        "Також ви можете швидко отримати консультацію через посилання на Zoom нижче, якщо виникне складність у  підключенні, напишіть “HELP” в чат Психологічної підтримки “Перемога”.\n\n" +
                "Бережіть себе, бо #Разом_ми_сила\n" +
                "Долучайтесь до нашої спільноти для корисної та практичної інформації від наших спеціалістів\n"

    override fun getOptions(): Map<Option, String> = mapOf(
        Pair(Option.ZOOM, "перейти в Zoom кімнату"),
        Pair(Option.CHAT, "чат Психологічної підтримки “Перемога”"),
        Pair(Option.FACEBOOK, "Facebook"),
        Pair(Option.INSTAGRAM, "Instagram")
    )
}