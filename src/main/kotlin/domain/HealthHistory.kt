package ie.setu.domain

import org.joda.time.DateTime

data class HealthHistory(
    var id: Int,
    val heartRate: Int,
    val cholesterolLevels: Double,
    val bloodSugarLevels: Double,
    val weight: Double,
    val height: Double,
    val dateOfRecord: DateTime,
    val bloodPressure: String,
    val userId: Int
)








