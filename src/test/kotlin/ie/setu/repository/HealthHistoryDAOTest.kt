package ie.setu.repository

import domain.Activity
import domain.db.Activities
import domain.db.Users
import domain.repository.ActivityDAO
import ie.setu.domain.HealthHistory
import ie.setu.domain.db.HealthHistories
import ie.setu.domain.db.HealthHistories.bloodPressure
import ie.setu.domain.db.HealthHistories.bloodSugarLevels
import ie.setu.domain.db.HealthHistories.height
import ie.setu.domain.repository.HealthHistoryDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.*
import ie.setu.ie.setu.controllers.HealthHistoryController
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

// Sample activity instances for testing
val healthHistory1 = healthHistories[0]
val healthHistory2 = healthHistories[1]
val healthHistory3 = healthHistories[2]


class HealthHistoryDAOTest {
    companion object {

        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateHealthHistories {
        @Test
        fun `multiple histories added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val healthHistoryDAO = populateHealthHistoryTable()

                assertEquals(3, healthHistoryDAO.getAll().size)
                assertEquals(healthHistory1, healthHistoryDAO.findByHealthHistoryId(healthHistory1.id))
                assertEquals(healthHistory2, healthHistoryDAO.findByHealthHistoryId(healthHistory2.id))
                assertEquals(healthHistory3, healthHistoryDAO.findByHealthHistoryId(healthHistory3.id))
            }
        }
    }

    @Nested
    inner class ReadHealthHistories {
        @Test
        fun `getting all histories from a populated table returns all rows`() {
            transaction {
                SchemaUtils.create(HealthHistories)
                val healthHistoryDAO = HealthHistoryDAO()

                assertEquals(0, healthHistoryDAO.getAll().size)
            }
        }

        @Test
        fun `get history by id that doesn't exist, results in no activity returned`() {
            transaction {
                val userDAO = populateUserTable()
                val healthHistoryDAO = populateHealthHistoryTable()
                assertEquals(1, healthHistoryDAO.findByUserId(3).size)
            }
        }

//        @Test
//        fun `get history by id that exists, results in correct history returned`() {
//            transaction {
//                val userDAO = populateUserTable()
//                val healthHistoryDAO = populateHealthHistoryTable()
//
//                assertEquals(healthHistory1, healthHistoryDAO.findByUserId(1)[0])
//                assertEquals(healthHistory2, healthHistoryDAO.findByUserId(1)[1])
//                assertEquals(healthHistory3, healthHistoryDAO.findByUserId(2)[0])
//            }
//        }

        @Test
        fun `get all histories over empty table returns none`() {
            transaction {
                SchemaUtils.create(HealthHistories)
                val healthHistoryDAO = HealthHistoryDAO()

                assertEquals(0, healthHistoryDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteHealthHistories {

        @Test
        fun `deleting a non-existent history in table results in no deletion`() {
            transaction {
                val userDAO = populateUserTable()
                val healthHistoryDAO = populateHealthHistoryTable()

                assertEquals(3, healthHistoryDAO.getAll().size)
                healthHistoryDAO.deleteByHealthHistoryId(4)
                assertEquals(3, healthHistoryDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing history in table results in record being deleted`() {
            transaction {
                val userDAO = populateUserTable()
                val healthHistoryDAO = populateHealthHistoryTable()

                assertEquals(3, healthHistoryDAO.getAll().size)
                healthHistoryDAO.deleteByHealthHistoryId(healthHistory3.id)
                assertEquals(2, healthHistoryDAO.getAll().size)
            }
        }

        @Test
        fun `delete histories by user ID deletes all related histories`() {
            transaction {
                val userDAO = populateUserTable()
                val healthHistoryDAO = populateHealthHistoryTable()

                assertEquals(3, healthHistoryDAO.getAll().size)
                healthHistoryDAO.deleteHealthHistoryByUserId(1)
                assertEquals(2, healthHistoryDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateHealthHistories {

        @Test
        fun `updating existing history in table results in successful update`() {
            transaction {
                val userDAO = populateUserTable()
                val healthHistoryDAO = populateHealthHistoryTable()

                val history3updated = HealthHistory(
                    id =3, heartRate = 68, cholesterolLevels = 200.5, bloodSugarLevels = 105.5,
                    weight = 80.5, height = 5.8, bloodPressure = "130/90", dateOfRecord = DateTime.now(), userId = 3
                )
                healthHistoryDAO.updateSpecificHealthHistoryById(history3updated.id, history3updated)
                assertEquals(history3updated, healthHistoryDAO.findByHealthHistoryId(3))
            }
        }

        @Test
        fun `updating non-existent history in table results in no updates`() {
            transaction {
                val userDAO = populateUserTable()
                val healthHistoryDAO = populateHealthHistoryTable()

                val history4updated = HealthHistory(
                    id = 3, heartRate = 89, cholesterolLevels = 200.0, bloodSugarLevels = 100.4,
                    weight = 23.0, height = 1.70, bloodPressure = "120/80", dateOfRecord = DateTime.now(), userId = 3
                )
                healthHistoryDAO.updateSpecificHealthHistoryById(history4updated.id, history4updated)
                assertEquals(null, healthHistoryDAO.findByHealthHistoryId(4))
                assertEquals(3, healthHistoryDAO.getAll().size)
            }
        }
    }

    internal fun populateUserTable(): UserDAO {
        SchemaUtils.create(Users)
        val userDAO = UserDAO()
        userDAO.save(users[0])
        userDAO.save(users[1])
        userDAO.save(users[2])
        return userDAO
    }

    internal fun populateHealthHistoryTable(): HealthHistoryDAO {
        SchemaUtils.create(HealthHistories)
        val healthHistoryDAO = HealthHistoryDAO()
        healthHistoryDAO.save(healthHistories[0])
        healthHistoryDAO.save(healthHistories[1])
        healthHistoryDAO.save(healthHistories[2])
        return healthHistoryDAO
    }
}