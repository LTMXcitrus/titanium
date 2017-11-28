package com.bot.slack.testbot.story

import fr.vsct.tock.bot.definition.story

val whatabout = story("whatabout") { bus ->
    with(bus) {
        send("Je regarde ça...")
    }
}