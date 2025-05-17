package gorokhov.stepan.configurations

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.*

fun createFirebaseApp(): FirebaseApp {
    val firebaseAdminResource = Thread.currentThread().contextClassLoader.getResourceAsStream("firebase-admin.json")
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(firebaseAdminResource))
        .build()
    return FirebaseApp.initializeApp(options, "ADMIN")
}