package ie.setu.domain

import org.joda.time.DateTime

data class MedicationLog(
    var id: Int,
    var medicationName:String,
    var dosage: Double,
    var frequency: Int,
    var started: DateTime,
    var ended: DateTime?,
    var notes: String?,
    var userId: Int)