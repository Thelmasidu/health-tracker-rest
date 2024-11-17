package ie.setu.helpers

import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO
import org.jetbrains.exposed.sql.transactions.transaction

class TestUtilities {
    private val userDAO = UserDAO()

    fun addUser(
        name: String = "Test User",
        email: String = "test@test.com"
    ): Int {
        return transaction {
            // Handle the nullable return type with Elvis operator
            userDAO.save(User(
                id = -1,
                name = name,
                email = email
            )) ?: throw IllegalStateException("Failed to save user")
        }
    }
}