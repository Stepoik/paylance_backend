package gorokhov.stepan.configurations

import com.kborowy.authprovider.firebase.firebase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import java.io.File

fun Application.configureSecurity() {
    install(Authentication) {
        val firebaseAdminResource = Thread.currentThread().contextClassLoader.getResourceAsStream("firebase-admin.json")
        firebase {
            adminInputStream = firebaseAdminResource
            realm = "My Server"
            validate { token ->
                AuthenticatedUser(id = token.uid)
            }
        }
    }
}

data class AuthenticatedUser(val id: String)