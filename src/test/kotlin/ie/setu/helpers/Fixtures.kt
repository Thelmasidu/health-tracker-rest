package ie.setu.helpers

import domain.Activity
import ie.setu.domain.User
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf(
    Activity(id = 1, description = "Sleeping", duration = 12.5, calories = 50, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Dancing", duration = 10.2, calories = 98, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Walking", duration = 8.5, calories = 112, started = DateTime.now(), userId = 2)

)