package com.xrouge.mot.titanium

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Vertx


fun main(args: Array<String>) {
    Vertx.vertx().deployVerticle(App)
}

object App: AbstractVerticle() {


    override fun start(future: Future<Void>) {
        val router = Router(vertx).buildRouter()
        vertx.createHttpServer().requestHandler(router::accept).listen(8080)
    }
}