package domain.repository

import domain.Activity
import domain.db.Activities
import utils.mapToActivity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


class ActivityDAO {

    //Get all the activities in the database regardless of user id
    fun getAll(): ArrayList<Activity> {
        val activitiesList: ArrayList<Activity> = arrayListOf()
        transaction {
            Activities.selectAll().map {
                activitiesList.add(mapToActivity(it)) }
        }
        return activitiesList
    }

    //Find a specific activity by activity id
    fun findByActivityId(id: Int): Activity?{
        return transaction {
            Activities
                .selectAll().where { Activities.id eq id }
                .map{mapToActivity(it)}
                .firstOrNull()
        }
    }

    //Find all activities for a specific user id
    fun findByUserId(userId: Int): List<Activity>{
        return transaction {
            Activities
                .selectAll().where {Activities.userId eq userId}
                .map {mapToActivity(it)}
        }
    }

    //Save an activity to the database
    fun save(activity: Activity): Int {
        return transaction {
            val activityId = Activities.insert {
                it[description] = activity.description
                it[duration] = activity.duration
                it[calories] = activity.calories
                it[started] = activity.started
                it[userId] = activity.userId
            } get Activities.id
            //returns the activity id as generated by the table
            activityId
        }
    }

    // Deletes a specific activity by its activity ID
    fun deleteByActivityId(activityId: Int): Int {
        return transaction {
            Activities.deleteWhere { Activities.id eq activityId }
        }
    }
    // Deletes all activities associated with a specific user ID
    fun deleteActivityByUserId(userId: Int): Int{
        return transaction {
            Activities.deleteWhere { Activities.userId eq userId }
        }
    }

    // Updates an existing activity by its activity ID
    fun updateSpecificActivityById(activityId: Int, activityToUpdate: Activity): Int{
        return transaction {
            Activities.update ({
                Activities.id eq activityId}) {
                it[description] = activityToUpdate.description
                it[duration] = activityToUpdate.duration
                it[calories] = activityToUpdate.calories
                it[started] = activityToUpdate.started
                it[userId] = activityToUpdate.userId
            }
        }
    }
}