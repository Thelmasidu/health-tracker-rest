package ie.setu.domain.repository

import ie.setu.domain.SleepRecord
import ie.setu.domain.db.SleepRecords
import ie.setu.domain.db.UserSleepRecords
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import utils.mapToSleepRecord

class SleepRecordDAO {

    fun getAll(): ArrayList<SleepRecord> {
        val sleepRecordsList: ArrayList<SleepRecord> = arrayListOf()
        transaction {
            SleepRecords.selectAll().map {
                sleepRecordsList.add(mapToSleepRecord(it))
            }
        }
        return sleepRecordsList
    }

    fun findBySleepRecordId(id: Int): SleepRecord? {
        return transaction {
            SleepRecords
                .selectAll().where { SleepRecords.id eq id }
                .map { mapToSleepRecord(it) }
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): List<SleepRecord> {
        return transaction {
            (UserSleepRecords innerJoin SleepRecords)
                .selectAll().where { UserSleepRecords.userId eq userId }
                .map { mapToSleepRecord(it) }
        }
    }

    fun addSleepRecordForUser(userId: Int, sleepRecord: SleepRecord): Int? {
        // Validate sleep quality before proceeding
        require(sleepRecord.sleepQuality in 1..5) { "Sleep quality should be between 1 and 5" }

        return transaction {
            // Add to SleepRecords
            val recordId = SleepRecords.insertAndGetId {
                it[date] = sleepRecord.date
                it[sleepDuration] = sleepRecord.sleepDuration
                it[sleepQuality] = sleepRecord.sleepQuality
                it[wakeUpTime] = sleepRecord.wakeUpTime
            }.value

            // Link User to SleepRecord in UserSleepRecords
            UserSleepRecords.insert {
                it[UserSleepRecords.userId] = userId
                it[UserSleepRecords.sleepRecordId] = recordId
            }

            recordId
        }
    }


    fun deleteSleepRecordForUser(userId: Int, sleepRecordId: Int): Int {
        return transaction {
            // First delete from the join table
            val userSleepRecordDeleted = UserSleepRecords.deleteWhere {
                (UserSleepRecords.userId eq userId) and (UserSleepRecords.sleepRecordId eq sleepRecordId)
            }

            // If we successfully deleted the user association, delete the actual record
            if (userSleepRecordDeleted > 0) {
                SleepRecords.deleteWhere { SleepRecords.id eq sleepRecordId }
            } else {
                0
            }
        }
    }

//    fun deleteSleepRecordForUser(userId: Int, sleepRecordId: Int): Int {
//        return transaction {
//            UserSleepRecords.deleteWhere {
//                (UserSleepRecords.userId eq userId) and
//                        (UserSleepRecords.sleepRecordId eq sleepRecordId)
//            }
//        }
//    }
}