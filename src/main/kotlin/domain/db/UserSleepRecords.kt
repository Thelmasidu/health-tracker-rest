package ie.setu.domain.db

import domain.db.Users
import org.jetbrains.exposed.sql.Table

object UserSleepRecords : Table("user_sleep_records") {
    val userId = integer("user_id").references(Users.id)
    val sleepRecordId = integer("sleep_record_id").references(SleepRecords.id)

    override val primaryKey = PrimaryKey(userId, sleepRecordId, name = "PK_UserSleepRecords")
}