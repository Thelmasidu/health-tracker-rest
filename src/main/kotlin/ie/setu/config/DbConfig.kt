package ie.setu.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.postgresql.util.PSQLException

class DbConfig {

    private val logger = KotlinLogging.logger {}
    private lateinit var dbConfig: Database

    fun getDbConnection(): Database {

        val PGHOST = "dpg-cs778eogph6c73ffdfm0-a.frankfurt-postgres.render.com"
        val PGPORT = "5432"
        val PGUSER = "health_tracker_app_8lka_user"
        val PGPASSWORD = "vKQXbkggjtOCFbhatYoaIrWaHiMli3P1"
        val PGDATABASE = "health_tracker_app_8lka"

        //url format should be jdbc:postgresql://host:port/database
        val dbUrl = "jdbc:postgresql://$PGHOST:$PGPORT/$PGDATABASE"

        try {
            logger.info { "Starting DB Connection...$dbUrl" }
            dbConfig = Database.connect(
                url = dbUrl, driver = "org.postgresql.Driver",
                user = PGUSER, password = PGPASSWORD
            )
            logger.info { "DB Connected Successfully..." + dbConfig.url }
        } catch (e: PSQLException) {
            logger.info { "Error in DB Connection...${e.printStackTrace()}" }
        }

        return dbConfig

    }
}