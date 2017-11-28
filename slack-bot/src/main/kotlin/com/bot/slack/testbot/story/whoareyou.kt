package com.bot.slack.testbot.story

import com.bot.slack.testbot.oneOf
import fr.vsct.tock.bot.connector.slack.emoji
import fr.vsct.tock.bot.connector.slack.model.SlackEmoji
import fr.vsct.tock.bot.definition.story


val whoareyou = story("whoareyou") { bus ->
    with(bus) {
        send(oneOf("Je suis le bot du mot!",
                "Moi ? Je suis un bot, et toi ? ${emoji(SlackEmoji.SLIGHT_SMILE)}"))
    }
}