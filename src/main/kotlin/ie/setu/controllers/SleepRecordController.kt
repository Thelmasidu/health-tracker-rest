package ie.setu.ie.setu.controllers

import ie.setu.domain.SleepRecord
import ie.setu.domain.repository.SleepRecordDAO
import io.javalin.http.Context
import utils.jsonToObject

object SleepRecordController {

    private val sleepRecordDAO = SleepRecordDAO()

    fun getAllSleepRecords(ctx: Context) {
        val records = sleepRecordDAO.getAll()
        if (records.isNotEmpty()) {
            ctx.status(200).json(records)
        } else {
            ctx.status(404).result("No records found")
        }
    }

    fun getRecordsByUserId(ctx: Context) {
        val userId = ctx.pathParam("user-id").toInt()
        val records = sleepRecordDAO.findByUserId(userId)
        if (records.isNotEmpty()) {
            ctx.status(200).json(records)
        } else {
            ctx.status(404).result("No records found for user ID $userId")
        }
    }

    fun addSleepRecordForUser(ctx: Context) {
        val userId = ctx.pathParam("user-id").toInt()
        val record: SleepRecord = jsonToObject(ctx.body())
        val recordId = sleepRecordDAO.addSleepRecordForUser(userId, record)
        if (recordId != null) {
            ctx.status(201).result("Sleep record added with ID $recordId")
        } else {
            ctx.status(400).result("Failed to add sleep record")
        }
    }

    fun deleteSleepRecordForUser(ctx: Context) {
        val userId = ctx.pathParam("user-id").toInt()
        val recordId = ctx.pathParam("record-id").toInt()
        val rowsDeleted = sleepRecordDAO.deleteSleepRecordForUser(userId, recordId)
        if (rowsDeleted > 0) {
            ctx.status(204)
        } else {
            ctx.status(404).result("No record found for given user and record IDs")
        }
    }
}