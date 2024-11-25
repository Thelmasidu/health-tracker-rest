<template id="activity-profile">
  <app-layout>
    <div>
      <form v-if="activity">
        <label class="col-form-label">Activity ID: </label>
        <input class="form-control" v-model="activity.id" name="id" type="number" readonly/><br>
        <label class="col-form-label">Description: </label>
        <input class="form-control" v-model="activity.description" name="description" type="text"/><br>
        <label class="col-form-label">Duration: </label>
        <input class="form-control" v-model="activity.duration" name="duration" type="number"/><br>
        <label class="col-form-label">Calories: </label>
        <input class="form-control" v-model="activity.calories" name="calories" type="number"/><br>
        <label class="col-form-label">Started: </label>
       <input class="form-control" v-model="activity.started" name="started" /><br>

      </form>

    </div>
  </app-layout>

</template>

<script>
app.component("activity-profile", {
  template: "#activity-profile",
  data: () => ({
    activity: null
  }),
  created: function () {
    const activityId = this.$javalin.pathParams["activity-id"];
    const url = `/api/activities/${activityId}`
    axios.get(url)
        .then(res =>
            {
              console.log("API Response:", res)
              //res -response. Only the stuffs inside data objects
              this.activity = res.data;
              console.log("Activity Data:", this.activity);
            }
        )

        .catch(() => alert("Error while fetching activity" + activityId));
  }
});
</script>