package com.bot.slack.testbot.story

import com.bot.slack.testbot.oneOf
import fr.vsct.tock.bot.connector.slack.emoji
import fr.vsct.tock.bot.connector.slack.model.SlackEmoji
import fr.vsct.tock.bot.definition.story


val greetings = story("hello") { bus ->
    with(bus) {
        resetDialogState()
        send(oneOf("Hello !",
                "Salut toi ${emoji(SlackEmoji.GRINNING)}",
                "oh, salut!",
                "Coucou!"
                ))
    }
}