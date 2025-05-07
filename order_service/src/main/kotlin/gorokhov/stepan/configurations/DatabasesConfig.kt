package gorokhov.stepan.configurations

import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val databaseURL = "jdbc:postgresql://postgres:5432/mydb"
    val databaseUser = "myuser"
    val databasePassword = "mypass"
    Database.connect(
        url = databaseURL,
        user = databaseUser,
        driver = "org.postgresql.Driver",
        password = databasePassword,
    )

    Flyway.configure()
        .dataSource(databaseURL, databaseUser, databasePassword)
        .locations("classpath:migrations")
        .load()
        .migrate()
}
