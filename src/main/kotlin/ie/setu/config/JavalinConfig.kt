package ie.setu.config

import ie.setu.ie.setu.controllers.*
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import io.javalin.http.HttpStatus
import io.javalin.vue.VueComponent
import utils.jsonObjectMapper

class JavalinConfig {
    private fun getRemoteAssignedPort(): Int =
        System.getenv("PORT")?.toIntOrNull() ?: 7001

    fun startJavalinService(): Javalin {

        val app = Javalin.create { config ->
            config.jsonMapper(JavalinJackson(jsonObjectMapper()))
            // Enable Webjars for static files
            config.staticFiles.enableWebjars()
            // Vue configuration
            config.vue.vueInstanceNameInJs = "app"


            config.jsonMapper(JavalinJackson(jsonObjectMapper()))
            config.router.apiBuilder {

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

// The @routeComponent that we added in layout.html earlier will be replaced
// by the String inside the VueComponent. This means a call to / will load
// the layout and display our <home-page> component.
                get("/", VueComponent("<home-page></home-page>"))

                get("/users", VueComponent("<user-overview></user-overview>"))
                get("/activities", VueComponent("<activity-overview></activity-overview>"))
                get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
                get("activities/{activity-id}", VueComponent("<activity-profile></activity-profile>"))
                get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
                get("/activities/{activity-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
                get("/histories", VueComponent("<history-overview></history-overview>"))
                get("/histories/{history-id}", VueComponent("<history-profile></history-profile>"))
                get("/medication", VueComponent("<medication-overview></medication-overview>"))
                get("/medication/{medication-id}", VueComponent("<medication-profile></medication-profile>"))
                get("/users/{user-id}/histories", VueComponent("<user-history-overview></user-history-overview>"))
                get("/users/{user-id}/medication", VueComponent("<user-medication-overview></user-medication-overview>"))
            }
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getRemoteAssignedPort())

        return app
    }
}