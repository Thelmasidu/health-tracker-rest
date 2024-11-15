package ie.setu.repository

import domain.db.Activities
import domain.repository.ActivityDAO
import ie.setu.domain.db.MedicationLogs
import ie.setu.domain.repository.MedicationLogDAO
import ie.setu.helpers.activities
import ie.setu.helpers.medicationLogs
import ie.setu.helpers.populateMedicationLogTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
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
        }

    }
}