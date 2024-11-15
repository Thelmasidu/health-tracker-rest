package utils

import domain.Activity
import domain.db.Activities
import domain.db.Users
import ie.setu.domain.HealthHistory
import ie.setu.domain.MedicationLog
import ie.setu.domain.User
import ie.setu.domain.db.HealthHistories
import ie.setu.domain.db.MedicationLogs
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)
fun mapToHealthHistory(it: ResultRow) = HealthHistory(
    id = it[HealthHistories.id],
    heartRate = it[HealthHistories.heartRate],
    cholesterolLevels = it[HealthHistories.cholesterolLevels],
    bloodSugarLevels = it[HealthHistories.bloodSugarLevels],
    weight = it[HealthHistories.weight],
    height = it[HealthHistories.height],
    dateOfRecord = it[HealthHistories.dateOfRecord],
    bloodPressure = it[HealthHistories.bloodPressure],
    userId = it[HealthHistories.userId]
)
fun mapToMedicationLog(it: ResultRow) = MedicationLog(
    id = it[MedicationLogs.id],
    medicationName = it[MedicationLogs.medicationName],
    dosage = it[MedicationLogs.dosage],
    frequency =it[MedicationLogs.frequency],
    started = it[MedicationLogs.started],
    ended = it[MedicationLogs.ended],
    notes =it[MedicationLogs.notes],
    userId = it[MedicationLogs.userId]
)