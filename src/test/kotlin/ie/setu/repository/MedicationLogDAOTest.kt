package ie.setu.repository

import domain.Activity
import domain.db.Activities
import domain.repository.ActivityDAO
import ie.setu.domain.MedicationLog
import ie.setu.domain.db.MedicationLogs
import ie.setu.domain.repository.MedicationLogDAO
import ie.setu.helpers.activities
import ie.setu.helpers.medicationLogs
import ie.setu.helpers.populateMedicationLogTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

// Sample activity instances for testing
val medicationLog1 = medicationLogs[0]
val medicationLog2 = medicationLogs[1]
val medicationLog3 = medicationLogs[2]

class MedicationLogDAOTest {

    companion object {

        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateMedicationLogs {
        @Test
        fun `multiple medications added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val medicationLogDAO = populateMedicationLogTable()

                assertEquals(3, medicationLogDAO.getAll().size)
                assertEquals(medicationLog1, medicationLogDAO.findByMedicationLogId(medicationLog1.id))
                assertEquals(medicationLog2, medicationLogDAO.findByMedicationLogId(medicationLog2.id))
                assertEquals(medicationLog3, medicationLogDAO.findByMedicationLogId(medicationLog3.id))
            }
        }
    }

    @Nested
    inner class ReadMedicationLogs {
        @Test
        fun `getting all medications from a populated table returns all rows`() {
            transaction {
                SchemaUtils.create(MedicationLogs)
                val medicationLogDAO = MedicationLogDAO()

                assertEquals(0, medicationLogDAO.getAll().size)
            }
        }


        @Test
        fun `get medication by id that doesn't exist, results in no activity returned`() {
            transaction {
                val userDAO = populateUserTable()
                val medicationLogDAO = populateMedicationLogTable()

                assertEquals(1, medicationLogDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get medication by id that exists, results in correct medication returned`() {
            transaction {
                val userDAO = populateUserTable()
                val medicationLogDAO = populateMedicationLogTable()

                assertEquals(medicationLog1, medicationLogDAO.findByUserId(1)[0])
                assertEquals(medicationLog2, medicationLogDAO.findByUserId(2)[0])
                assertEquals(medicationLog3, medicationLogDAO.findByUserId(3)[0])
            }
        }

        @Test
        fun `get all medications over empty table returns none`() {
            transaction {
                SchemaUtils.create(MedicationLogs)
                val medicationLogDAO = MedicationLogDAO()

                assertEquals(0, medicationLogDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteMedicationLogs {

        @Test
        fun `deleting a non-existent medication in table results in no deletion`() {
            transaction {
                val userDAO = populateUserTable()
                val medicationLogDAO = populateMedicationLogTable()

                assertEquals(3, medicationLogDAO.getAll().size)
                medicationLogDAO.deleteByMedicationLogId(4)
                assertEquals(3, medicationLogDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing medication in table results in record being deleted`() {
            transaction {
                val userDAO = populateUserTable()
                val medicationLogDAO = populateMedicationLogTable()

                assertEquals(3, medicationLogDAO.getAll().size)
                medicationLogDAO.deleteByMedicationLogId(medicationLog3.id)
                assertEquals(2, medicationLogDAO.getAll().size)
            }
        }

        @Test
        fun `delete medications by user ID deletes all related medications`() {
            transaction {
                val userDAO = populateUserTable()
                val medicationLogDAO = populateMedicationLogTable()

                assertEquals(3, medicationLogDAO.getAll().size)
                medicationLogDAO.deleteMedicationLogsByUserId(1)
                assertEquals(2, medicationLogDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateMedicationLogs {

        @Test
        fun `updating existing medication in table results in successful update`() {
            transaction {
                val userDAO = populateUserTable()
                val medicationLogDAO = populateMedicationLogTable()

                val medicationLog3updated = MedicationLog(
                    id = 3, medicationName = "Ibuprofen", dosage = 200.0,
                    frequency = 3, started = DateTime.now(), ended = null,
                    notes = "Taken after meals", userId = 1
                )
                medicationLogDAO.updateSpecificMedicationLogById(medicationLog3updated.id, medicationLog3updated)
                assertEquals(medicationLog3updated, medicationLogDAO.findByMedicationLogId(3))
            }
        }
    }
    }