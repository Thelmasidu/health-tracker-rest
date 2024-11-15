package ie.setu.domain.db

import domain.db.Activities.autoIncrement
import domain.db.Activities.references
import domain.db.Users
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object MedicationLogs : Table("medication") {
    val id = integer("id").autoIncrement()
    val medicationName = varchar("medication_name", 100)
    val dosage = double("dosage")
    val frequency = integer("frequency")
    val started = datetime("started")
    val ended = datetime("ended").nullable()
    val notes = varchar("notes", length = 100).nullable()
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

        override val primaryKey = PrimaryKey(MedicationLogs.id, name = "PK_MedicationLogs_ID")

    }