package com.bot.slack.testbot

import com.bot.slack.testbot.story.greetings
import com.bot.slack.testbot.story.thankyou
import fr.vsct.tock.bot.definition.bot


val testBot = bot(
        "test-bot-slack",
        stories = listOf(greetings, thankyou),
        hello = greetings
)