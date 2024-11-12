package ie.setu.domain.db
import domain.db.Users
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object HealthHistories : Table("histories") {
    val id = integer("id").autoIncrement()
    val heartRate = integer("heart_rate")
    val cholesterolLevels = double("cholesterol_levels")
    val bloodSugarLevels = double("blood_sugar_levels")
    val weight =  double("weight")
    val height = double("height")
    val dateOfRecord = datetime("date_of_record")
    val bloodPressure = varchar("blood_pressure", length = 101)
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(HealthHistories.id, name = "PK_HealthHistories_ID")
}