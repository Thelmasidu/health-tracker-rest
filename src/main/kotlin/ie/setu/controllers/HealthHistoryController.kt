package ie.setu.ie.setu.controllers
import domain.Activity
import domain.repository.ActivityDAO
import ie.setu.domain.HealthHistory
import ie.setu.domain.repository.HealthHistoryDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import org.jetbrains.exposed.sql.transactions.transaction
import utils.jsonToObject

object HealthHistoryController {

    private val userDao = UserDAO()
    private val healthHistoryDAO = HealthHistoryDAO()

    fun getAllHealthHistories(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.

        val healthHistories = healthHistoryDAO.getAll()
        if (healthHistories.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(healthHistories)
    }

    fun getHealthHistoriesByUserId(ctx: Context) {
        if (HealthHistoryController.userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val healthHistories = HealthHistoryController.healthHistoryDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (healthHistories.isNotEmpty()) {
                ctx.json(healthHistories)
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

    fun getHealthHistoriesByHealthHistoryId(ctx: Context) {
        val healthHistory = HealthHistoryController.healthHistoryDAO.findByHealthHistoryId((ctx.pathParam("history-id").toInt()))
        if (healthHistory != null){
            ctx.json(healthHistory)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addHealthHistory(ctx: Context) {
        val healthHistory: HealthHistory = jsonToObject(ctx.body()) // Corrected type name
        val userId = userDao.findById(healthHistory.userId)
            if (userId != null) {
                val healthHistoryId = healthHistoryDAO.save(healthHistory)
            healthHistory.id = healthHistoryId
            ctx.json(healthHistory)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    fun deleteHealthHistoryByUser(ctx: Context) {
        if (healthHistoryDAO.deleteHealthHistoryByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteSpecificHealthHistory(ctx: Context) {
        if (healthHistoryDAO.deleteByHealthHistoryId(ctx.pathParam("history-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateHealthHistory(ctx: Context) {
        transaction {
            val healthHistory: HealthHistory = jsonToObject(ctx.body())
            val healthHistoryId = ctx.pathParam("history-id").toInt()

            if (healthHistoryDAO.updateSpecificHealthHistoryById(healthHistoryId, healthHistory) != 0) {
                ctx.status(204)
            } else {
                ctx.status(404)
            }
        }
    }
}
