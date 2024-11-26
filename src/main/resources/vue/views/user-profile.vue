<template id="user-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this user.</p>
      <p> View <a :href="'/users'">all users</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> User Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateUser()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteUser()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="user.id" name="id" readonly placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="user.name" name="name" placeholder="Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="user.email" name="email" placeholder="Email"/>
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
        <p  v-if="activities.length === 0"> No activities yet...</p>
        <p  v-if="activities.length > 0"> Activities so far...</p>
        <p  v-if="histories.length === 0"> No Histories yet...</p>
        <p  v-if="histories.length > 0"> Histories so far...</p>
        <p  v-if="medication.length === 0"> No Medication Logs yet...</p>
        <p  v-if="medication.length > 0"> Medication Logs so far...</p>
        <ul>
          <li v-for="activity in activities">
            {{ activity.description }} for {{ activity.duration }} minutes
          </li>
        </ul>
        <div v-for="history in histories">
          <label class="col-form-label">History ID: </label>
          <input class="form-control" v-model="history.id" name="id" type="number" readonly/><br>
          <label class="col-form-label">HeartRate: </label>
          <input class="form-control" v-model="history.heartRate" name="heart-rate" type="text"/><br>
          <label class="col-form-label">CholesterolLevels: </label>
          <input class="form-control" v-model="history.heartRate" name="cholesterol-levels" /><br>
          <label class="col-form-label">BloodSugarLevels: </label>
          <input class="form-control" v-model="history.bloodSugarLevels" name="blood-sugar-levels" /><br>
          <label class="col-form-label">Weight: </label>
          <input class="form-control" v-model="history.weight" name="weight" /><br>
          <label class="col-form-label">Height: </label>
          <input class="form-control" v-model="history.height" name="height" /><br>
          <label class="col-form-label">DateOfRecord: </label>
          <input class="form-control" v-model="history.dateOfRecord" name="date-of-record" /><br>
          <label class="col-form-label">BloodPressure: </label>
          <input class="form-control" v-model="history.bloodPressure" name="blood-pressure" /><br>
        </div>
        <div v-for="medication in medication">
          <label class="col-form-label"> Medication ID: </label>
          <input class="form-control" v-model="medication.id" name="id" type="number" readonly/><br>
          <label class="col-form-label">MedicationName: </label>
          <input class="form-control" v-model="medication.medicationName" name="medication-name" type="text"/><br>
          <label class="col-form-label">Dosage: </label>
          <input class="form-control" v-model="medication.dosage" name="dosage" type="number"/><br>
          <label class="col-form-label">Frequency: </label>
          <input class="form-control" v-model="medication.frequency" name="frequency" type="text"/><br>
          <label class="col-form-label">Started: </label>
          <input class="form-control" v-model="medication.started" name="started" /><br>
          <label class="col-form-label">Ended: </label>
          <input class="form-control" v-model="medication.ended" name="ended" /><br>
          <label class="col-form-label">Notes: </label>
          <input class="form-control" v-model="medication.notes" name="notes" type="text"/><br>
        </div>
      </div>
    </div>
  </app-layout>
</template>
<script>

app.component("user-profile", {
  template: "#user-profile",
  data: () => ({
    user: null,
    noUserFound: false,
    activities: [],
    histories: [],
    medication: [],
  }),
  created: function () {
    const userId = this.$javalin.pathParams["user-id"];
    const url = `/api/users/${userId}`
    axios.get(url)
        .then(res => this.user = res.data)
        .catch(error => {
          console.log("No user found for id passed in the path parameter: " + error)
          this.noUserFound = true
        })
    axios.get(url + `/activities`)
        .then(res => this.activities = res.data)
        .catch(error => {
          console.log("No activities added yet (this is ok): " + error)
        })
    axios.get(url + `/histories`)
        .then(res => this.histories = res.data)
        .catch(error => {
          console.log("No histories added yet (this is ok): " + error)
        })
    axios.get(url + `/medication`)
        .then(res => this.medication = res.data)
        .catch(error => {
          console.log("No medication added yet (this is ok): " + error)
        })
  },

  methods: {
    updateUser: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}`;
      axios.patch(url, {
        name: this.user.name,
        email: this.user.email,
      })
          .then(response => {
            this.user = response.data; // Correct handling of response
            alert("User updated!");
          })
          .catch(error => {
            console.log(error);
          });
    },
    deleteUser: function () {
      if (confirm("Do you really want to delete?")) {
        const userId = this.$javalin.pathParams["user-id"];
        const url = `/api/users/${userId}`;
        axios.delete(url)
            .then(() => {
              alert("User deleted");
              // display the /users endpoint
              window.location.href = '/users';
            })
            .catch(error => {
              console.log(error);
            });
      }
    },
  },
  deleteUser: function (user, index) {
    if (confirm('Are you sure you want to delete this user? This action cannot be undone.', 'Warning')) {
      //user confirmed delete
      const userId = user.id;
      const url = `/api/users/${userId}`;
      axios.delete(url)
          .then(response =>
              //delete from the local state so Vue will reload list automatically
              this.users.splice(index, 1).push(response.data))
          .catch(function (error) {
            console.log(error)
          });
    }
  }
});

</script>