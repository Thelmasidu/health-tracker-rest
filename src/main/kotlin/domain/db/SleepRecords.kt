package ie.setu.domain.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.exposed.sql.jodatime.time

object SleepRecords: IntIdTable("records") {
    val date = datetime("date")
    val sleepDuration = double("sleep_duration")
    val sleepQuality = integer("sleep_quality")
    val wakeUpTime = time("wake_up_time")

}