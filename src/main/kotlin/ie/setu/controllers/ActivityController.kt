package ie.setu.ie.setu.controllers

import domain.Activity
import domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import org.jetbrains.exposed.sql.transactions.transaction
import utils.jsonToObject

object ActivityController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()


    fun getAllActivities(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.

        val activities = activityDAO.getAll()
        if (activities.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(activities)
    }

    fun getActivitiesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                ctx.json(activities)
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

    fun getActivitiesByActivityId(ctx: Context) {
        val activity = activityDAO.findByActivityId((ctx.pathParam("activity-id").toInt()))
        if (activity != null){
            ctx.json(activity)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addActivity(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val activity : Activity = jsonToObject(ctx.body())
        val userId = userDao.findById(activity.userId)
        if (userId != null) {
            val activityId = activityDAO.save(activity)
            activity.id = activityId
            ctx.json(activity)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }
    }

    fun deleteActivityByUser(ctx: Context) {
        if (activityDAO.deleteActivityByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteSpecificActivity(ctx: Context) {
        if (activityDAO.deleteByActivityId(ctx.pathParam("activity-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateActivity(ctx: Context) {
        transaction {
            val activity: Activity = jsonToObject(ctx.body())
            val activityId = ctx.pathParam("activity-id").toInt()

            if (activityDAO.updateSpecificActivityById(activityId, activity) != 0) {
                ctx.status(204)
            } else {
                ctx.status(404)
            }
        }
    }

}