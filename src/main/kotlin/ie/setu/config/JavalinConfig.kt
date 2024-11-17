package ie.setu.config

import ie.setu.ie.setu.controllers.*
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import io.javalin.http.HttpStatus
import utils.jsonObjectMapper

class JavalinConfig {
    private fun getRemoteAssignedPort(): Int =
        System.getenv("PORT")?.toIntOrNull() ?: 7001

    fun startJavalinService(): Javalin {
        val app = Javalin.create { config ->
            config.jsonMapper(JavalinJackson(jsonObjectMapper()))
            config.router.apiBuilder {
                // Base route
                get("/") { it.redirect("api/users", HttpStatus.FOUND) }

                // User endpoints
                path("api/users") {
                    get(UserController::getAllUsers)
                    post(UserController::addUser)
                    path("{user-id}") {
                        get(UserController::getUserByUserId)
                        patch(UserController::updateUser)
                        delete(UserController::deleteUser)

                        // Nested routes for activities, histories, and medication logs for specific user
                        path("activities") {
                            get(ActivityController::getActivitiesByUserId)
                            delete(ActivityController::deleteActivityByUser)
                        }
                        path("histories") {
                            get(HealthHistoryController::getHealthHistoriesByUserId)
                            delete(HealthHistoryController::deleteHealthHistoryByUser)
                        }
                        path("medication") {
                            get(MedicationLogController::getMedicationLogsByUserId)
                            delete(MedicationLogController::deleteMedicationLogByUser)
                        }

                        // Nested route for sleep records
                        path("sleep-records") {
                            get(SleepRecordController::getRecordsByUserId)
                            post(SleepRecordController::addSleepRecordForUser)
                            delete("{record-id}", SleepRecordController::deleteSleepRecordForUser)
                        }
                    }
                    path("email/{user-email}") {
                        get(UserController::getUserByEmail)
                    }
                }

                // Activity endpoints
                path("api/activities") {
                    get(ActivityController::getAllActivities)
                    post(ActivityController::addActivity)
                    path("{activity-id}") {
                        get(ActivityController::getActivitiesByActivityId)
                        patch(ActivityController::updateActivity)
                        delete(ActivityController::deleteSpecificActivity)
                    }
                }

                // Health history endpoints
                path("api/histories") {
                    get(HealthHistoryController::getAllHealthHistories)
                    post(HealthHistoryController::addHealthHistory)
                    path("{history-id}") {
                        get(HealthHistoryController::getHealthHistoriesByHealthHistoryId)
                        patch(HealthHistoryController::updateHealthHistory)
                        delete(HealthHistoryController::deleteSpecificHealthHistory)
                    }
                }

                // Medication endpoints
                path("api/medication") {
                    get(MedicationLogController::getAllMedicationLogs)
                    post(MedicationLogController::addMedicationLog)
                    path("{medication-id}") {
                        get(MedicationLogController::getMedicationLogsByMedicationLogId)
                        patch(MedicationLogController::updateMedicationLog)
                        delete(MedicationLogController::deleteSpecificMedicationLog)
                    }
                }

                // Sleep records endpoints
                path("api/sleep-records") {
                    get(SleepRecordController::getAllSleepRecords)
                    // Nested route for sleep records
                    path("{sleep-records}") {
                        get(SleepRecordController::getRecordsByUserId)
                        post(SleepRecordController::addSleepRecordForUser)
                        delete("{record-id}", SleepRecordController::deleteSleepRecordForUser)
                    }
                }
            }
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getRemoteAssignedPort())

        return app
    }
}