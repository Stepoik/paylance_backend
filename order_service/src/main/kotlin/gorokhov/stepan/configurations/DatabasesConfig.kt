package gorokhov.stepan.configurations

import gorokhov.stepan.features.projects.data.database.Contracts
import gorokhov.stepan.features.projects.data.database.ProjectResponses
import gorokhov.stepan.features.projects.data.database.Projects
import gorokhov.stepan.features.users.data.tables.FreelancerInfos
import gorokhov.stepan.features.users.data.tables.Reviews
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val databaseURL = "jdbc:postgresql://postgres:5432/mydb"
    val databaseUser = "myuser"
    val databasePassword = "mypass"
    val database = Database.connect(
        url = databaseURL,
        user = databaseUser,
        driver = "org.postgresql.Driver",
        password = databasePassword,
    )

    transaction(database) {
        create(
            Projects,
            FreelancerInfos,
            Reviews,
            Contracts,
            ProjectResponses
        )
    }


//    Flyway.configure()
//        .dataSource(databaseURL, databaseUser, databasePassword)
//        .locations("classpath:migrations")
//        .load()
//        .migrate()
}
