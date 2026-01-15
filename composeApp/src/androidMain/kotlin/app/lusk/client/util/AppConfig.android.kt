package app.lusk.client.util

import app.lusk.client.shared.BuildKonfig

actual object AppConfig {
    actual val isDebug: Boolean = BuildKonfig.DEBUG
}
