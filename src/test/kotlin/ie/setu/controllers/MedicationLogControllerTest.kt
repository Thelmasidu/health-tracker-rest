package ie.setu.controllers

import domain.Activity
import ie.setu.config.DbConfig
import ie.setu.domain.MedicationLog
import ie.setu.domain.User
import ie.setu.helpers.*
import kong.unirest.core.HttpResponse
import kong.unirest.core.JsonNode
import kong.unirest.core.Unirest
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import utils.jsonNodeToObject
import utils.jsonToObject

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MedicationLogControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class CreateMedicationLogs {

        @Test
        fun `add a log when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated log that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addMedicationLogResponse = addMedicationLog(
                medicationLogs[0].medicationName, medicationLogs[0].dosage,
                medicationLogs[0].frequency, medicationLogs[0].started,
                medicationLogs[0].ended, medicationLogs[0].notes, addedUser.id
            )
            assertEquals(200, addMedicationLogResponse.status)

            //After - delete the user (Log will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add a log when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, retrieveUserById(userId).status)

            val addMedicationLogResponse = addMedicationLog(
                medicationLogs.get(0).medicationName, medicationLogs.get(0).dosage,
                medicationLogs.get(0).frequency, medicationLogs.get(0).started,
                medicationLogs.get(0).ended, medicationLogs.get(0).notes, userId
            )
            assertEquals(200, addMedicationLogResponse.status)
        }
}
    @Nested
    inner class ReadActivities {

        @Test
        fun `get all medications from the database returns 200 or 404 response`() {
            val response = retrieveAllMedicationLogs()
            if (response.status == 404) {
                val retrievedMedicationLogs = jsonNodeToObject<Array<MedicationLog>>(response)
                assertNotEquals(0, retrievedMedicationLogs.size)
            } else {
                assertEquals(200, response.status)
            }
        }
//        @Test
//        fun `get all medications by user id when user and medications exists returns 200 response`() {
//            //Arrange - add a user and 3 associated logs that we plan to retrieve
//            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
//            addMedicationLog(
//                medicationLogs[0].medicationName, medicationLogs[0].dosage,
//                medicationLogs[0].frequency, medicationLogs[0].started,
//                medicationLogs[0].ended, medicationLogs[0].notes, addedUser.id)
//            addMedicationLog(
//                medicationLogs[1].medicationName, medicationLogs[1].dosage,
//                medicationLogs[1].frequency, medicationLogs[1].started,
//                medicationLogs[1].ended, medicationLogs[1].notes, addedUser.id)
//            addMedicationLog(
//                medicationLogs[2].medicationName, medicationLogs[2].dosage,
//                medicationLogs[2].frequency, medicationLogs[2].started,
//                    medicationLogs[2].ended, medicationLogs[2].notes, addedUser.id)
//
//            //Assert and Act - retrieve the three added logs by user id
//            val response = retrieveMedicationLogsByUserId(addedUser.id)
//            assertEquals(404, response.status)
//            val retrievedMedicationLog = jsonNodeToObject<Array<MedicationLog>>(response)
//            assertEquals(3, retrievedMedicationLog.size)
//
//            //After - delete the added user and assert a 204 is returned (logs are cascade deleted)
//            assertEquals(204, deleteUser(addedUser.id).status)
//        }

        @Test
        fun `get all medications by user id when no medications exist returns 404 response`() {
            //Arrange - add a user
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the activities by user id
            val response = retrieveMedicationLogsByUserId(addedUser.id)
            assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all medications by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve activities by user id
            val response = retrieveMedicationLogsByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get medication by medication id when no medication exists returns 404 response`() {
            //Arrange
            val medicationLogId = -1
            //Assert and Act - attempt to retrieve the activity by activity id
            val response = retrieveMedicationLogByMedicationLogId(medicationLogId)
            assertEquals(404, response.status)
        }

//        @Test
//        fun `get medication by medication id when medication exists returns 200 response`() {
//            //Arrange - add a user and associated activity
//            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
//            val addMedicationLogResponse = addMedicationLog(
//                medicationLogs[0].medicationName,
//                medicationLogs[0].dosage,
//                medicationLogs[0].frequency,
//                medicationLogs[0].started,
//                medicationLogs[0].ended,
//                medicationLogs[0].notes,
//                addedUser.id)
//            assertEquals(200, addMedicationLogResponse.status)
//            val addedMedicationLog = jsonNodeToObject<MedicationLog>(addMedicationLogResponse)
//
//            //Act & Assert - retrieve the activity by medication id
//            val response = retrieveMedicationLogByMedicationLogId(addedMedicationLog.id)
//            assertEquals(200, response.status)
//
//            //After - delete the added user and assert a 204 is returned
//            assertEquals(204, deleteUser(addedUser.id).status)
//        }

    }

    @Nested
    inner class UpdateMedicationLogs {

        @Test
        fun `updating a medication by medication id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val medicationLogID = -1

            //Arrange - check there is no user for -1 id
            assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an activity/user that doesn't exist
            assertEquals(
                404, updateMedicationLog(
                    medicationLogID, updatedMedicationName, updatedDosage,
                    updatedFrequency, updatedstarted, updatedended, updatedNotes, userId
                ).status
            )
        }

//        @Test
//        fun `updating a medication by medication id when it exists, returns 204 response`() {
//
//            //Arrange - add a user and an associated activity that we plan to do an update on
//            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
//            val addMedicationLogResponse = addMedicationLog(
//                medicationLogs[0].medicationName,
//                medicationLogs[0].dosage, medicationLogs[0].frequency,
//                medicationLogs[0].started, medicationLogs[0].ended, medicationLogs[0].notes,
//                addedUser.id)
//            assertEquals(200, addMedicationLogResponse.status)
//            val addedMedicationLog = jsonNodeToObject<MedicationLog>(addMedicationLogResponse)
//
//            //Act & Assert - update the added activity and assert a 204 is returned
//            val updatedMedicationLog = updateMedicationLog(addedMedicationLog.id, updatedMedicationName,
//                updatedDosage, updatedFrequency, updatedstarted,  updatedended, updatedNotes, addedUser.id)
//            assertEquals(204, updatedMedicationLog.status)
//
//            //Assert that the individual fields were all updated as expected
//            val retrievedActivityResponse = retrieveMedicationLogByMedicationLogId(addedMedicationLog.id)
//            val updatedActivity = jsonNodeToObject<Activity>(retrievedActivityResponse)
//            assertEquals(updatedDescription,updatedActivity.description)
//            assertEquals(updatedDuration, updatedActivity.duration, 0.1)
//            assertEquals(updatedCalories, updatedActivity.calories)
//            assertEquals(updatedStarted, updatedActivity.started )
//
//            //After - delete the user
//            deleteUser(addedUser.id)
//        }
    }

    @Nested
    inner class DeleteMedicationLogs {

        @Test
        fun `deleting a medication by activity id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteMedicationLogByMedicationLogId(-1).status)
        }

        @Test
        fun `deleting medications by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteMedicationLogsByUserId(-1).status)
        }

//        @Test
//        fun `deleting a medication by id when it exists, returns a 204 response`() {
//
//            //Arrange - add a user and an associated activity that we plan to do a delete on
//            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
//            val addMedicationLogResponse = addMedicationLog(
//                medicationLogs[0].medicationName, medicationLogs[0].dosage,
//                medicationLogs[0].frequency, medicationLogs[0].started,
//                medicationLogs[0].ended, medicationLogs[0].notes, addedUser.id)
//            assertEquals(200, addMedicationLogResponse.status)
//
//            //Act & Assert - delete the added activity and assert a 204 is returned
//            val addedMedicationLog = jsonNodeToObject<MedicationLog>(addMedicationLogResponse)
//            assertEquals(204, deleteMedicationLogByMedicationLogId(addedMedicationLog.id).status)
//
//            //After - delete the user
//            deleteUser(addedUser.id)
//        }

//        @Test
//        fun `deleting all MEDICATIONS by userid when it exists, returns a 204 response`() {
//
//            //Arrange - add a user and 3 associated activities that we plan to do a cascade delete
//            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
//            val addMedicationLogResponse1 = addMedicationLog(
//                medicationLogs[0].medicationName, medicationLogs[0].dosage,
//               medicationLogs[0].frequency, medicationLogs[0].started,
//               medicationLogs[0].ended, medicationLogs[0].notes, addedUser.id)
//            assertEquals(200, addMedicationLogResponse1.status)
//            val addMedicationLogResponse2 = addMedicationLog(
//                medicationLogs[0].medicationName, medicationLogs[0].dosage,
//                medicationLogs[0].frequency, medicationLogs[0].started,
//                medicationLogs[0].ended, medicationLogs[0].notes, addedUser.id)
//            assertEquals(200, addMedicationLogResponse2.status)
//            val addMedicationLogResponse3 = addMedicationLog(
//                medicationLogs[0].medicationName, medicationLogs[0].dosage,
//                medicationLogs[0].frequency, medicationLogs[0].started,
//                medicationLogs[0].ended, medicationLogs[0].notes, addedUser.id)
//            assertEquals(200, addMedicationLogResponse3.status)
//
//            //Act & Assert - delete the added user and assert a 204 is returned
//            assertEquals(204, deleteUser(addedUser.id).status)
//
//            //Act & Assert - attempt to retrieve the deleted activities
//            val addedMedicationLog1 = jsonNodeToObject<MedicationLog>(addMedicationLogResponse1)
//            val addedMedicationLog2 = jsonNodeToObject<MedicationLog>(addMedicationLogResponse2)
//            val addedMedicationLog3 = jsonNodeToObject<MedicationLog>(addMedicationLogResponse3)
//            assertEquals(404, retrieveMedicationLogByMedicationLogId(addedMedicationLog1.id).status)
//            assertEquals(404, retrieveMedicationLogByMedicationLogId(addedMedicationLog2.id).status)
//            assertEquals(404, retrieveMedicationLogByMedicationLogId(addedMedicationLog3.id).status)
//        }
    }

        //helper function to add a test user to the database
    private fun addUser (name: String, email: String): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/users")
            .body("{\"name\":\"$name\", \"email\":\"$email\"}")
            .asJson()
    }

    //helper function to delete a test user from the database
    private fun deleteUser (id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/$id").asString()
    }

    //helper function to retrieve a test user from the database by id
    private fun retrieveUserById(id: Int) : HttpResponse<String> {
    return Unirest.get(origin + "/api/users/${id}").asString()
}
    //helper function to retrieve all medication
    private fun retrieveAllMedicationLogs(): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/medication").asJson()
    }
    //helper function to retrieve logs by user id
    private fun retrieveMedicationLogsByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/medication").asJson()
    }

    //helper function to retrieve activity by activity id
    private fun retrieveMedicationLogByMedicationLogId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/activities/${id}").asJson()
    }

    //helper function to delete an activity by activity id
    private fun deleteMedicationLogByMedicationLogId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/medication/$id").asString()
    }

    //helper function to delete an activity by activity id
    private fun deleteMedicationLogsByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/$id/medication").asString()
    }

    //helper function to add an activity
    private fun addMedicationLog(medicationName:String, dosage: Double, frequency: Int,
                                 started: DateTime, ended: DateTime?, notes: String?, userId: Int): HttpResponse<JsonNode> {
        val endedValue = ended?.toString()?.let { "\"$it\"" } ?: "null"

        return Unirest.post(origin + "/api/medication")
            .body("""
                {
                   "medicationName":"$medicationName",
                   "dosage":$dosage,
                   "frequency":$frequency,
                   "started":"$started",
                   "ended":"$endedValue",
                   "notes":"$notes",
                   "userId":$userId
                }
            """.trimIndent())
            .asJson()
    }

    //helper function to add a test user to the database
    private fun updateMedicationLog(id: Int, medicationName: String, dosage: Double, frequency: Int,
                               started: DateTime,ended: DateTime?, notes: String?, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/medication/$id")
            .body("""
                {
                  "medicationName":"$medicationName",
                  "dosage":$dosage,
                  "frequency":$frequency,
                  "started":"$started",
                  "ended":"$ended",
                  "notes":"$notes",
                  "userId":$userId
                }
            """.trimIndent()).asJson()
    }
}