package ie.setu.helpers

import domain.Activity
import domain.db.Activities
import domain.db.Users
import domain.repository.ActivityDAO
import ie.setu.domain.HealthHistory
import ie.setu.domain.User
import ie.setu.domain.db.HealthHistories
import ie.setu.domain.repository.HealthHistoryDAO
import ie.setu.domain.repository.UserDAO
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"

val updatedDescription = "Updated Description"
val updatedDuration = 30.0
val updatedCalories = 945
val updatedStarted = DateTime.parse("2020-06-11T05:59:27.258Z")
val updatedName = "Updated Name"
val updatedEmail = "Updated Email"

val updatedHeartRate = 38
val updatedCholesterolLevels = 236.0
val updatedBloodSugarLevels = 96.0
val updatedWeight = 12.8
val updatedHeight = 6
val updatedDateOfRecord = DateTime.parse("2020-06-11T05:59:27.258Z")
val updatedBloodPressure = "Updated Blood Pressure"


val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf(
    Activity(id = 1, description = "Sleeping", duration = 12.5, calories = 50, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Dancing", duration = 10.2, calories = 98, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Walking", duration = 8.5, calories = 112, started = DateTime.now(), userId = 2)

)

val healthHistories = arrayListOf(
    HealthHistory(id = 1, heartRate = 70, cholesterolLevels = 180.5, bloodSugarLevels = 95.5, weight = 75.5, height = 5.9, dateOfRecord = DateTime.now(), bloodPressure = "120/80", userId = 1),
    HealthHistory(id = 2, heartRate = 72, cholesterolLevels = 190.5, bloodSugarLevels = 100.5, weight = 78.5, height = 6.0, dateOfRecord = DateTime.now(), bloodPressure = "125/85", userId = 2),
    HealthHistory(id = 3, heartRate = 68, cholesterolLevels = 200.5, bloodSugarLevels = 105.5, weight = 80.5, height = 5.8, dateOfRecord = DateTime.now(), bloodPressure = "130/90", userId = 3)
)

fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users[0])
    userDAO.save(users[1])
    userDAO.save(users[2])
    return userDAO
}
fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities[0])
    activityDAO.save(activities[1])
    activityDAO.save(activities[2])
    return activityDAO
}

fun populateHealthHistoryTable(): HealthHistoryDAO {
    SchemaUtils.create(HealthHistories)
    val healthHistoryDAO = HealthHistoryDAO()
    healthHistoryDAO.save(healthHistories[0])
    healthHistoryDAO.save(healthHistories[1])
    healthHistoryDAO.save(healthHistories[2])
    return healthHistoryDAO
}