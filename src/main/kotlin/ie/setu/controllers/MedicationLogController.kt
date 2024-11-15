package ie.setu.ie.setu.controllers

import domain.Activity
import ie.setu.domain.MedicationLog
import ie.setu.domain.db.MedicationLogs
import ie.setu.domain.repository.MedicationLogDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import org.jetbrains.exposed.sql.transactions.transaction
import utils.jsonToObject

object MedicationLogController {

    private val userDao = UserDAO()
    private val medicationLogDAO = MedicationLogDAO()

    fun getAllMedicationLogs(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.

        val medicationLogs = medicationLogDAO.getAll()
        if (medicationLogs.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(medicationLogs)
    }

    fun getMedicationLogsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val medicationLogs = medicationLogDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (medicationLogs.isNotEmpty()) {
                ctx.json(medicationLogs)
                ctx.status(200)
            }
            else{
                ctx.status(404)
            }
        }
        else{
            ctx.status(404)
        }
    }

    fun getMedicationLogsByMedicationLogId(ctx: Context) {
        val medicationLog = medicationLogDAO.findByMedicationLogId((ctx.pathParam("medication-id").toInt()))
        if (medicationLog != null){
            ctx.json(medicationLog)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addMedicationLog(ctx: Context) {
        // Mapper handles the serialization of Joda date into a String.
        val medicationLog: MedicationLog = jsonToObject(ctx.body())

        // Check if the user exists
        val user = userDao.findById(medicationLog.userId)
        if (user != null) {
            // Save the medication log and get the generated ID
            val savedMedicationLogId = medicationLogDAO.save(medicationLog)

            // Set the ID of the medication log
            medicationLog.id = savedMedicationLogId

            // Respond with the saved medication log and status 201
            ctx.json(medicationLog)
            ctx.status(201)
        } else {
            ctx.status(404) // User not found
        }
    }

    fun deleteMedicationLogByUser(ctx: Context) {
        if (medicationLogDAO.deleteMedicationLogsByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteSpecificMedicationLog(ctx: Context) {
        if (medicationLogDAO.deleteByMedicationLogId(ctx.pathParam("medication-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateMedicationLog(ctx: Context) {
        transaction {

            val medicationLog: MedicationLog = jsonToObject(ctx.body())
            val medicationLogId = ctx.pathParam("medication-id").toInt()

            if (medicationLogDAO.updateSpecificMedicationLogById(medicationLogId, medicationLog) != 0) {
                ctx.status(204)
            } else {
                ctx.status(404)
            }
        }
    }


}