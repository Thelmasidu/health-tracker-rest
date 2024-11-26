<template id="user-history-overview">
  <app-layout>
  <div>
    <h3>Histories list </h3>

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

  </div>
  </app-layout>
</template>

<script>
app.component("user-history-overview",{
  template: "#user-history-overview",
  data: () => ({
    histories: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/histories`)
        .then(res => this.histories = res.data)
        .catch(() => alert("Error while fetching histories"));
  }
});
</script>