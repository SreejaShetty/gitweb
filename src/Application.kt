package com.suryadigital.automatedturk

import com.suryadigital.automatedturk.github.gitHub
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.features.UserAgent
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.features.ForwardedHeaderSupport
import io.ktor.features.XForwardedHeaderSupport
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.ShutDownUrl
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

internal val httpClient: HttpClient = HttpClient {
    install(Logging) {
        level = LogLevel.ALL
    }
    install(UserAgent) { agent = "Surya Digital Automated Turk" }
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(@Suppress("UNUSED_PARAMETER") testing: Boolean = false) {
    install(CallLogging) {
        level = Level.INFO
    }

    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
    }

    install(ForwardedHeaderSupport)
    install(XForwardedHeaderSupport)

    install(ShutDownUrl.ApplicationCallFeature) {
        shutDownUrl = "/ktor/application/shutdown"
        // A function that will be executed to get the exit code of the process
        exitCodeSupplier = { 0 }
    }

    routing {
        get("/") {
            call.respondText("HEALTHY", contentType = ContentType.Text.Plain)
        }
        route("/github") {
            gitHub()
        }
    }
}
