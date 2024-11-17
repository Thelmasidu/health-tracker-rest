package ie.setu.domain

import org.joda.time.DateTime
import org.joda.time.LocalTime


data class SleepRecord(
    val id: Int,
    val date: DateTime,
    val sleepDuration: Double,
    val sleepQuality: Int,
    val wakeUpTime: LocalTime
)