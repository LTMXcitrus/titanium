package com.xrouge.mot.titanium.bot

import com.xrouge.mot.titanium.bot.story.greetings
import com.xrouge.mot.titanium.bot.story.thankyou
import fr.vsct.tock.bot.definition.bot


val testBot = bot(
        "test-bot-slack",
        stories = listOf(greetings, thankyou),
        hello = greetings
)