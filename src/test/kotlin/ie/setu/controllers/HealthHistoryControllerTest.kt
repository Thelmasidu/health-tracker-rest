package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.HealthHistory
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
class HealthHistoryControllerTest {
    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class CreateHealthHistories {
        @Test
        fun `add a history when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addHealthHistoryResponse = addHealthHistory(
                healthHistories[0].heartRate, healthHistories[0].cholesterolLevels,
                healthHistories[0].bloodSugarLevels, healthHistories[0].weight,
                healthHistories[0].height, healthHistories[0].dateOfRecord,
                healthHistories[0].bloodPressure, addedUser.id
            )
            assertEquals(201, addHealthHistoryResponse.status)

            //After - delete the user (Activity will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add a history when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, retrieveUserById(userId).status)

            val addHealthHistoryResponse = addHealthHistory(
                healthHistories.get(0).heartRate, healthHistories.get(0).cholesterolLevels,
                healthHistories.get(0).bloodSugarLevels, healthHistories.get(0).weight,
                healthHistories.get(0).height,healthHistories.get(0).dateOfRecord,
                healthHistories.get(0).bloodPressure, userId
            )
            assertEquals(404, addHealthHistoryResponse.status)
        }
    }

    @Nested
    inner class ReadActivities {

        @Test
        fun `get all histories from the database returns 200 or 404 response`() {
            val response = retrieveAllHealthHistories()
            if (response.status == 404) {
                val retrievedHealthHistories = jsonNodeToObject<Array<HealthHistory>>(response)
                assertNotEquals(0, retrievedHealthHistories.size)
            } else {
                assertEquals(200, response.status)
            }
        }

        @Test
        fun `get all histories by user id when user and histories exists returns 200 response`() {
            //Arrange - add a user and 3 associated histories that we plan to retrieve
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            addHealthHistory(
                healthHistories[0].heartRate, healthHistories[0].cholesterolLevels,
                healthHistories[0].bloodSugarLevels, healthHistories[0].weight,
                healthHistories[0].height, healthHistories[0].dateOfRecord,
                healthHistories[0].bloodPressure, addedUser.id
            )
            addHealthHistory(
                healthHistories[1].heartRate, healthHistories[1].cholesterolLevels,
                healthHistories[1].bloodSugarLevels, healthHistories[1].weight,
                healthHistories[1].height, healthHistories[1].dateOfRecord,
                healthHistories[1].bloodPressure, addedUser.id
            )
            addHealthHistory(
                healthHistories[2].heartRate, healthHistories[2].cholesterolLevels,
                healthHistories[2].bloodSugarLevels, healthHistories[2].weight,
                healthHistories[2].height, healthHistories[2].dateOfRecord,
                healthHistories[2].bloodPressure, addedUser.id
            )
            //Assert and Act - retrieve the three added histories by user id
            val response = retrieveHealthHistoriesByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedHealthHistories = jsonNodeToObject<Array<HealthHistory>>(response)
            assertEquals(3, retrievedHealthHistories.size)

            //After - delete the added user and assert a 204 is returned (histories are cascade deleted)
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all histories by user id when no histories exist returns 404 response`() {
            //Arrange - add a user
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the activities by user id
            val response = retrieveHealthHistoriesByUserId(addedUser.id)
            assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all histories by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve activities by user id
            val response = retrieveHealthHistoriesByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get history by history id when no history exists returns 404 response`() {
            //Arrange
            val historyId = -1
            //Assert and Act - attempt to retrieve the activity by activity id
            val response = retrieveHealthHistoryByHealthHistoryId(historyId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get history by history id when history exists returns 200 response`() {
            //Arrange - add a user and associated activity
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addHealthHistoryResponse = addHealthHistory(
                healthHistories[0].heartRate, healthHistories[0].cholesterolLevels,
                healthHistories[0].bloodSugarLevels, healthHistories[0].weight,
                healthHistories[0].height, healthHistories[0].dateOfRecord,
                healthHistories[0].bloodPressure, addedUser.id
            )
            assertEquals(201, addHealthHistoryResponse.status)
            val addedHealthHistory = jsonNodeToObject<HealthHistory>(addHealthHistoryResponse)

            //Act & Assert - retrieve the activity by activity id
            val response = retrieveHealthHistoryByHealthHistoryId(addedHealthHistory.id)
            assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }
    }
        @Nested
        inner class UpdateHealthHistories {

            @Test
            fun `updating an history by history id when it doesn't exist, returns a 404 response`() {
                val userId = -1
                val healthHistoryID = -1

                //Arrange - check there is no user for -1 id
                assertEquals(404, retrieveUserById(userId).status)

                //Act & Assert - attempt to update the details of an history/user that doesn't exist
                assertEquals(
                    404, updateHealthHistory(
                        healthHistoryID,  updatedHeartRate, updatedCholesterolLevels,
                        updatedBloodSugarLevels, updatedWeight, updatedHeight, updatedDateOfRecord, updatedBloodPressure, userId
                    ).status
                )
            }

//            @Test
//            fun `updating an history by history id when it exists, returns 204 response`() {
//
//                //Arrange - add a user and an associated history that we plan to do an update on
//                val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
//                val addHealthHistoryResponse = addHealthHistory(
//                    healthHistories[0].heartRate, healthHistories[0].cholesterolLevels,
//                    healthHistories[0].bloodSugarLevels, healthHistories[0].weight,
//                    healthHistories[0].height, healthHistories[0].dateOfRecord,
//                    healthHistories[0].bloodPressure, addedUser.id)
//                assertEquals(201, addHealthHistoryResponse.status)
//                val addedHealthHistory = jsonNodeToObject<HealthHistory>(addHealthHistoryResponse)
//
//                //Act & Assert - update the added history and assert a 204 is returned
//                val updatedHealthHistoryResponse = updateHealthHistory(addedHealthHistory.id, updatedHeartRate,
//                    updatedCholesterolLevels, updatedBloodSugarLevels, updatedWeight, updatedHeight,
//                    updatedDateOfRecord, updatedBloodPressure, addedUser.id)
//                assertEquals(204, updatedHealthHistoryResponse.status)
//
//                //Assert that the individual fields were all updated as expected
//                val retrievedHealthHistoryResponse = retrieveHealthHistoriesByUserId(addedHealthHistory.id)
//                val updatedHealthHistory = jsonNodeToObject<HealthHistory>(retrievedHealthHistoryResponse)
//                assertEquals(updatedHeartRate,updatedHealthHistory.heartRate)
//                assertEquals(updatedCholesterolLevels, updatedHealthHistory.cholesterolLevels, 0.1)
//                assertEquals(updatedBloodSugarLevels, updatedHealthHistory.bloodSugarLevels)
//                assertEquals(updatedWeight, updatedHealthHistory.weight )
//                assertEquals(updatedHeight, updatedHealthHistory.height )
//                assertEquals(updatedDateOfRecord, updatedHealthHistory.dateOfRecord )
//                assertEquals(updatedBloodPressure, updatedHealthHistory.bloodPressure )
//
//                //After - delete the user
//                deleteUser(addedUser.id)
//            }

            @Nested
            inner class DeleteHealthHistories {

                @Test
                fun `deleting a history by history id when it doesn't exist, returns a 404 response`() {
                    //Act & Assert - attempt to delete a user that doesn't exist
                    assertEquals(404, deleteHealthHistoryByHealthHistoryId(-1).status)
                }
            }

            @Test
            fun `deleting histories by user id when it doesn't exist, returns a 404 response`() {
                //Act & Assert - attempt to delete a user that doesn't exist
                assertEquals(404, deleteHealthHistoriesByUserId(-1).status)
            }

            @Test
            fun `deleting a history by id when it exists, returns a 204 response`() {

                //Arrange - add a user and an associated activity that we plan to do a delete on
                val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
                val addHealthHistoryResponse = addHealthHistory(
                    healthHistories[0].heartRate, healthHistories[0].cholesterolLevels,
                    healthHistories[0].bloodSugarLevels, healthHistories[0].weight,
                    healthHistories[0].height, healthHistories[0].dateOfRecord,
                    healthHistories[0].bloodPressure, addedUser.id)
                assertEquals(201, addHealthHistoryResponse.status)

                //Act & Assert - delete the added activity and assert a 204 is returned
                val addedHealthHistory = jsonNodeToObject<HealthHistory>(addHealthHistoryResponse)
                assertEquals(404, deleteHealthHistoriesByUserId(addedHealthHistory.id).status)

                //After - delete the user
                deleteUser(addedUser.id)
            }

            @Test
            fun `deleting all histories by userid when it exists, returns a 204 response`() {

                //Arrange - add a user and 3 associated activities that we plan to do a cascade delete
                val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
                val addHealthHistoryResponse1 = addHealthHistory(
                    healthHistories[0].heartRate, healthHistories[0].cholesterolLevels,
                    healthHistories[0].bloodSugarLevels, healthHistories[0].weight,
                    healthHistories[0].height, healthHistories[0].dateOfRecord,
                    healthHistories[0].bloodPressure, addedUser.id)
                assertEquals(201, addHealthHistoryResponse1.status)
                val addHealthHistoryResponse2 = addHealthHistory(
                    healthHistories[1].heartRate, healthHistories[1].cholesterolLevels,
                    healthHistories[1].bloodSugarLevels, healthHistories[1].weight,
                    healthHistories[1].height, healthHistories[1].dateOfRecord,
                    healthHistories[1].bloodPressure, addedUser.id)
                assertEquals(201, addHealthHistoryResponse2.status)
                val addHealthHistoryResponse3 = addHealthHistory(
                    healthHistories[2].heartRate, healthHistories[2].cholesterolLevels,
                    healthHistories[2].bloodSugarLevels, healthHistories[2].weight,
                    healthHistories[2].height, healthHistories[2].dateOfRecord,
                    healthHistories[2].bloodPressure, addedUser.id)
                assertEquals(201, addHealthHistoryResponse3.status)

                //Act & Assert - delete the added user and assert a 204 is returned
                assertEquals(204, deleteUser(addedUser.id).status)

                //Act & Assert - attempt to retrieve the deleted activities
                val addedHealthHistory1 = jsonNodeToObject<HealthHistory>(addHealthHistoryResponse1)
                val addedHealthHistory2 = jsonNodeToObject<HealthHistory>(addHealthHistoryResponse2)
                val addedHealthHistory3 = jsonNodeToObject<HealthHistory>(addHealthHistoryResponse3)
                assertEquals(200, retrieveHealthHistoryByHealthHistoryId(addedHealthHistory1.id).status)
                assertEquals(200, retrieveHealthHistoryByHealthHistoryId(addedHealthHistory2.id).status)
                assertEquals(200, retrieveHealthHistoryByHealthHistoryId(addedHealthHistory3.id).status)
            }
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

    //helper function to delete an activity by activity id
    private fun deleteHealthHistoryByHealthHistoryId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/histories/$id").asString()
    }

    //helper function to retrieve all activities
    private fun retrieveAllHealthHistories(): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/histories").asJson()
    }

    //helper function to retrieve activities by user id
    private fun retrieveHealthHistoriesByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/histories").asJson()
    }

    //helper function to retrieve activity by activity id
    private fun retrieveHealthHistoryByHealthHistoryId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/histories/${id}").asJson()
    }

    //helper function to delete an activity by activity id
    private fun deleteHealthHistoriesByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/$id/histories").asString()
    }

    //helper function to add an activity
    private fun addHealthHistory(heartRate: Int, cholesterolLevels: Double, bloodSugarLevels: Double, weight: Double,
                                 height: Double, dateOfRecord: DateTime, bloodPressure: String, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/histories")
            .body("""
                {
                   "heartRate":"$heartRate",
                   "cholesterolLevels":$cholesterolLevels,
                   "bloodSugarLevels":$bloodSugarLevels,
                   "weight":"$weight",
                   "height":"$height",
                   "dateOfRecord":"$dateOfRecord",
                   "bloodPressure":"$bloodPressure",
                   "userId":$userId
                }
            """.trimIndent())
            .asJson()
    }

    //helper function to add a test user to the database
    private fun updateHealthHistory(id: Int, heartRate: Int, cholesterolLevels: Double, bloodSugarLevels: Double,
                                    weight: Double, height: Double, dateOfRecord: DateTime, bloodPressure: String, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/histories/$id")
            .body("""
                {
                  "heartRate":"$heartRate",
                  "cholesterolLevels":$cholesterolLevels,
                  "bloodSugarLevels":$bloodSugarLevels,
                  "weight":"$weight",
                  "height":"$height",
                  "dateOfRecord":"$dateOfRecord",
                  "bloodPressure":"$bloodPressure",
                  "userId":$userId
                }
            """.trimIndent()).asJson()
    }
}
