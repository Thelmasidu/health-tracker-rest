package ie.setu.repository

import domain.Activity
import domain.db.Activities
import domain.db.Users
import domain.repository.ActivityDAO
import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.activities
import ie.setu.helpers.users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ie.setu.helpers.nonExistingEmail
import org.jetbrains.exposed.sql.jodatime.dateTimeLiteral
import org.joda.time.DateTime

//retrieving some test data from Fixtures
val activity1 = activities[0]
val activity2 = activities[1]
val activity3 = activities[2]

class ActivityDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateActivities {
        @Test
        fun `multiple activities added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                assertEquals(activity1, activityDAO.findByActivityId(activity1.id))
                assertEquals(activity2, activityDAO.findByActivityId(activity2.id))
                assertEquals(activity3, activityDAO.findByActivityId(activity3.id))
            }
        }
    }

    @Nested
    inner class ReadActivities {
        @Test
        fun `getting all activities from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                SchemaUtils.create(Activities)
                val activityDAO = ActivityDAO()

                //Act & Assert
                assertEquals(0, activityDAO.getAll().size)
            }
        }
        @Test
        fun `get activity by id that doesn't exist, results in no activity returned`() {
            transaction {

                //Arrange - create and populate table with three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(0, activityDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get activity by id that exists, results in a correct activity returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals( activity1, activityDAO.findByUserId(1)[0])
                assertEquals( activity2, activityDAO.findByUserId(1)[1])
                assertEquals( activity3, activityDAO.findByUserId(2)[0])
            }

        }

        @Test
        fun `get all activities over empty table returns none`() {
            transaction {

                //Arrange - create and setup userDAO object
                SchemaUtils.create(Activities)
                val activityDAO = ActivityDAO()

                //Act & Assert
                assertEquals(0, activityDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteActivities {

            @Test
            fun `deleting a non-existant activity in table results in no deletion`() {
                transaction {

                    //Arrange - create and populate table with three users
                    val userDAO = populateUserTable()
                    val activityDAO = populateActivityTable()

                    //Act & Assert
                    assertEquals(3, activityDAO.getAll().size)
                    activityDAO.deleteByActivityId(4)
                    assertEquals(3, activityDAO.getAll().size)
                }
            }
        @Test
        fun `deleting an existing activity in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                activityDAO.deleteByActivityId(4)
                assertEquals(3, activityDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateActivities {

        @Test
        fun `updating existing activity in table results in successful update`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                val activity3updated = Activity(id = 3, description = "Cardio", duration = 42.0,
                    calories = 220, started = DateTime.now(), userId = 2)
                activityDAO.updateSpecificActivityById(activity3updated.id, activity3updated)
                assertEquals(activity3updated, activityDAO.findByActivityId(3))
            }
        }
        @Test
        fun `updating non-existant activity in table results in no updates`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                val activity4updated = Activity(id =4, description = "Praying",  duration = 42.0,
                    calories = 220, started = DateTime.now(), userId = 2)
                activityDAO.updateSpecificActivityById(activity4updated.id, activity4updated)
                assertEquals(null, activityDAO.findByActivityId(4))
                assertEquals(3, activityDAO.getAll().size)
            }
        }
    }

    internal fun populateUserTable(): UserDAO{
        SchemaUtils.create(Users)
        val userDAO = UserDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        return userDAO
    }
    internal fun populateActivityTable(): ActivityDAO{
        SchemaUtils.create(Activities)
        val activityDAO = ActivityDAO()
        activityDAO.save( activities[0])
        activityDAO.save( activities[1])
        activityDAO.save( activities[2])
        return activityDAO
    }
}