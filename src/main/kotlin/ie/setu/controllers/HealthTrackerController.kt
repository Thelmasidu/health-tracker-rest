package ie.setu.controllers

import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.User

object HealthTrackerController {

    private val userDao = UserDAO()

    fun getAllUsers(ctx: Context) {
        ctx.json(userDao.getAll())
    }

    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
        }
    }

    fun getUserByEmail(ctx: Context){
        val email = userDao.findByEmail(ctx.pathParam("user-email"))
        if (email != null) {
            ctx.json(email)
        }
    }

    fun addUser(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<User>(ctx.body())
        userDao.save(user)
        ctx.json(user)
    }

    fun deleteUser(ctx: Context){
        val user = userDao.delete(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
        } else {
            ctx.status(404).result("User not found")
        }
    }

    fun updateUser(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val updatedUser = mapper.readValue<User>(ctx.body())

        val user = userDao.update(ctx.pathParam("user-id").toInt(), updatedUser)

        if (user != null) {
            ctx.json(user)
        } else {
            ctx.status(404).result("User not found")
        }
    }

}

