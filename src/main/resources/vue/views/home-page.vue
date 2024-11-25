<!-- the "home-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="home-page">
  <app-layout>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Registered Users</h5>
          <div class="card-body">
            <h5 class="card-title">{{users.length}} users</h5>
            <a href="/users" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Activities</h5>
          <div class="card-body">
            <h5 class="card-title">{{activities.length}} activities</h5>
            <a href="/activities" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">User Health History</h5>
          <div class="card-body">
            <h5 class="card-title">{{histories.length}} histories</h5>
            <a href="/histories" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>
      <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header"> View Medication Logs</h5>
          <div class="card-body">
            <h5 class="card-title">{{medication.length}} medication</h5>
            <a href="/medication" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component('home-page',
    {
      template: "#home-page",
      data: () => ({
        users: [],
        activities: [],
        histories:[],
        medication:[]
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("Error while fetching users"));
        axios.get("/api/activities")
            .then(res => this.activities = res.data)
            .catch(() => alert("Error while fetching activities"));
        axios.get("/api/histories")
            .then(res => this.histories = res.data)
            .catch(() => alert("Error while fetching histories"));
        axios.get("/api/medication")
            .then(res => this.medication = res.data)
            .catch(() => alert("Error while fetching medication"));
      }
    });
</script>