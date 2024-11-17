package ie.setu.domain.repository

import ie.setu.domain.SleepRecord
import ie.setu.domain.db.SleepRecords
import ie.setu.domain.db.UserSleepRecords
import ie.setu.helpers.TestUtilities
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SleepRecordDAOTest {

    private val sleepRecordDAO = SleepRecordDAO()
    private val testUtilities = TestUtilities()
    private var userId: Int? = null
    private var sleepRecord1: SleepRecord? = null
    private var sleepRecord2: SleepRecord? = null

    @BeforeAll
    fun setup() {
        // Set up the database connection for testing
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

        transaction {
            // Create the tables
            SchemaUtils.create(SleepRecords)
            SchemaUtils.create(UserSleepRecords)

            // Create a test user and get their ID
            userId = testUtilities.addUser(name = "Test User", email = "test@test.com")

            // Create test sleep records
            sleepRecord1 = SleepRecord(
                id = -1,
                date = DateTime.now(),
                sleepDuration = 8.0,
                sleepQuality = 5,
                wakeUpTime = LocalTime(8, 0) // 8:00 AM
            )

            sleepRecord2 = SleepRecord(
                id = -1,
                date = DateTime.now().minusDays(1),
                sleepDuration = 7.5,
                sleepQuality = 4,
                wakeUpTime = LocalTime(7, 30) // 7:30 AM
            )
        }
    }

    @Nested
    inner class CreateSleepRecords {
        @Test
        fun `add sleep record for user success`() {
            val recordId = sleepRecordDAO.addSleepRecordForUser(userId!!, sleepRecord1!!)
            assertNotNull(recordId)

            val retrievedRecord = sleepRecordDAO.findBySleepRecordId(recordId)
            assertNotNull(retrievedRecord)
            assertEquals(sleepRecord1!!.sleepDuration, retrievedRecord.sleepDuration)
            assertEquals(sleepRecord1!!.sleepQuality, retrievedRecord.sleepQuality)
            assertEquals(sleepRecord1!!.wakeUpTime, retrievedRecord.wakeUpTime)
        }
    }

    @Nested
    inner class ReadSleepRecords {
        @Test
        fun `get all sleep records when table has records`() {
            val recordId1 = sleepRecordDAO.addSleepRecordForUser(userId!!, sleepRecord1!!)
            val recordId2 = sleepRecordDAO.addSleepRecordForUser(userId!!, sleepRecord2!!)

            val records = sleepRecordDAO.getAll()
            assertEquals(5, records.size)
        }

        @Test
        fun `get sleep record by id when record exists`() {
            val recordId = sleepRecordDAO.addSleepRecordForUser(userId!!, sleepRecord1!!)
            val retrievedRecord = sleepRecordDAO.findBySleepRecordId(recordId!!)
            assertNotNull(retrievedRecord)
            assertEquals(sleepRecord1!!.sleepDuration, retrievedRecord.sleepDuration)
        }

        @Test
        fun `get sleep record by id when record does not exist`() {
            val retrievedRecord = sleepRecordDAO.findBySleepRecordId(-1)
            assertNull(retrievedRecord)
        }

        @Test
        fun `get sleep records by user id when records exist`() {
            sleepRecordDAO.addSleepRecordForUser(userId!!, sleepRecord1!!)
            sleepRecordDAO.addSleepRecordForUser(userId!!, sleepRecord2!!)

            val records = sleepRecordDAO.findByUserId(userId!!)
            assertEquals(3, records.size)
        }

        @Test
        fun `get sleep records by user id when no records exist`() {
            val records = sleepRecordDAO.findByUserId(-1)
            assertEquals(0, records.size)
        }
    }

    @Nested
    inner class DeleteSleepRecords {
        @Test
        fun `delete sleep record for user when record exists`() {
            val recordId = sleepRecordDAO.addSleepRecordForUser(userId!!, sleepRecord1!!)
            val result = sleepRecordDAO.deleteSleepRecordForUser(userId!!, recordId!!)
            assertEquals(1, result)

            val retrievedRecord = sleepRecordDAO.findBySleepRecordId(recordId)
            assertNull(retrievedRecord)
        }

        @Test
        fun `delete sleep record for user when record does not exist`() {
            val result = sleepRecordDAO.deleteSleepRecordForUser(userId!!, -1)
            assertEquals(0, result)
        }

        @Test
        fun `delete sleep record should clean up both tables`() {
            // Add a record
            val recordId = sleepRecordDAO.addSleepRecordForUser(userId!!, sleepRecord1!!)
            assertNotNull(recordId)

            // Delete the record
            val result = sleepRecordDAO.deleteSleepRecordForUser(userId!!, recordId!!)
            assertEquals(1, result)

            // Verify it's gone from both tables
            val retrievedRecord = sleepRecordDAO.findBySleepRecordId(recordId)
            assertNull(retrievedRecord)

            val userRecords = sleepRecordDAO.findByUserId(userId!!)
            assertEquals(1, userRecords.size)
        }
    }

    @Nested
    inner class ValidationTests {
        @Test
        fun `sleep quality should be between 1 and 5`() {
            val invalidRecord = SleepRecord(
                id = -1,
                date = DateTime.now(),
                sleepDuration = 8.0,
                sleepQuality = 6, // Invalid quality
                wakeUpTime = LocalTime(8, 0)
            )

            // This test assumes your DAO validates the sleep quality
            // If it doesn't, you might want to add validation
            try {
                sleepRecordDAO.addSleepRecordForUser(userId!!, invalidRecord)
                assert(false) { "Should have thrown an exception for invalid sleep quality" }
            } catch (e: IllegalArgumentException) {
                // Expected
            }
        }
    }
}