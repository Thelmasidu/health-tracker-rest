package ie.setu.config


import ie.setu.ie.setu.controllers.UserController
import ie.setu.ie.setu.controllers.ActivityController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import utils.jsonObjectMapper

class JavalinConfig {
    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7001
    }

    fun startJavalinService(): Javalin {
        val app = Javalin.create {
            //add this jsonMapper to serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
        }
            .apply{
                exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
                error(404) { ctx -> ctx.json("404 - Not Found") }
            }
            .start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun registerRoutes(app: Javalin) {
        app.get("/api/users", UserController::getAllUsers)
        app.get("/api/users/{user-id}", UserController::getUserByUserId)
        app.post("/api/users", UserController::addUser)
        app.get("/api/users/email/{user-email}", UserController::getUserByEmail)
        app.delete("/api/users/{user-id}", UserController::deleteUser)
        app.patch("/api/users/{user-id}", UserController::updateUser)
        app.get("/api/activities", ActivityController::getAllActivities)
        app.post("/api/activities", ActivityController::addActivity)
        app.get("/api/users/{user-id}/activities", ActivityController::getActivitiesByUserId)
        app.delete("/api/users/{user-id}/activities", ActivityController::deleteActivityByUser)
        app.delete("/api/activities/{activity-id}", ActivityController::deleteSpecificActivity)
        app.patch("/api/activities/{activity-id}", ActivityController::updateActivity)
        app.get("/api/activities/{activity-id}", ActivityController::getActivitiesByActivityId)
    }

//    private fun registerRoutes(app: Javalin) {
//        app.routes {
//            path("/api") {
//                // Users endpoints
//                path("/users") {
//                    get(UserController::getAllUsers)
//                    post(UserController::addUser)
//                    path("/{user-id}") {
//                        get(UserController::getUserByUserId)
//                        patch(UserController::updateUser)
//                        delete(UserController::deleteUser)
//                        // Nested activities for specific user
//                        path("/activities") {
//                            get(ActivityController::getActivitiesByUserId)
//                            delete(ActivityController::deleteActivityByUser)
//                        }
//                    }
//                    path("/email/{user-email}") {
//                        get(UserController::getUserByEmail)
//                    }
//                }
//
//                // Activities endpoints
//                path("/activities") {
//                    get(ActivityController::getAllActivities)
//                    post(ActivityController::addActivity)
//                    path("/{activity-id}") {
//                        get(ActivityController::getActivitiesByActivityId)
//                        patch(ActivityController::updateActivity)
//                        delete(ActivityController::deleteSpecificActivity)
//                    }
//                }
//            }
//        }
//    }
//

}

