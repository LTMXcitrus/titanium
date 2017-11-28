package com.bot.slack.testbot.story

import fr.vsct.tock.bot.connector.slack.*
import fr.vsct.tock.bot.connector.slack.model.SlackEmoji
import fr.vsct.tock.bot.definition.story

val thankyou = story("thankyou") { bus ->

    with(bus) {
        val attachments = arrayOf(attachmentField("title", "value"), attachmentField("title", "value"))
        withSlack {
            multiLineMessage(listOf("line 1", "line 2", "line 3"))
            end()
            attachmentMessage(
                    *attachments,
                    fallback = "fallback",
                    text = "text"
            )
            end()
            emojiMessage(SlackEmoji.SMILE)
            end()
            textMessage("hello! ${emoji(SlackEmoji.SMILE)}")
        }
        end()
    }
}