package ie.setu.domain.repository

import ie.setu.domain.User


class UserDAO {

    private val users = arrayListOf<User>(
        User(name = "Alice", email = "alice@wonderland.com", id = 0),
        User(name = "Bob", email = "bob@cat.ie", id = 1),
        User(name = "Mary", email = "mary@contrary.com", id = 2),
        User(name = "Carol", email = "carol@singer.com", id = 3)
    )

    fun getAll() : ArrayList<User>{
        return users
    }

    fun findById(id: Int): User?{
        return users.find {it.id == id}
    }

    fun save(user: User){
        users.add(user)
    }

    fun findByEmail(email: String) :User?{
        return users.find {it.email == email}
    }

    fun delete(id: Int): User? {
        val userToDelete = users.find { it.id == id }
        if (userToDelete != null) {
            users.remove(userToDelete)
        }
        return userToDelete
    }

    fun update(id: Int, updatedUser: User): User? {
        val userIndex = users.indexOfFirst { it.id == id }
        if (userIndex != -1) {
            // Replace the existing user with the updated user data
            users[userIndex] = updatedUser.copy(id = id) // Retain the original id
            return users[userIndex]
        }
        return null // Return null if the user wasn't found
    }


}
