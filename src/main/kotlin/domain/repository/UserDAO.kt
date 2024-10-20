package ie.setu.domain.repository

import domain.db.Users
import ie.setu.domain.User
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import utils.mapToUser


class UserDAO {

    fun getAll(): ArrayList<User> {
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it)) }
        }
        return userList
    }

    fun findById(id: Int): User?{
        return transaction {
            Users.selectAll().where { Users.id eq id }
                .map{mapToUser(it)}
                .firstOrNull()
        }
    }

    fun save(user: User){
    }

    fun findByEmail(email: String) :User?{
        return null
    }

    fun delete(id: Int) {
    }

    fun update(id: Int, user: User){
    }
}