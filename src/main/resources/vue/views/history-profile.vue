<template id="history-profile">
  <app-layout>
    <div>
      <form v-if="history">
        <label class="col-form-label">History ID: </label>
        <input class="form-control" v-model="history.id" name="id" type="number" readonly/><br>
        <label class="col-form-label">HeartRate: </label>
        <input class="form-control" v-model="history.heartRate" name="heart-rate" type="text"/><br>
        <label class="col-form-label">CholesterolLevels: </label>
        <input class="form-control" v-model="history.cholesterolLevels" name="cholesterol-levels" /><br>
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

      </form>

    </div>
  </app-layout>

</template>

<script>
//history profile component
app.component("history-profile", {
  template: "#history-profile",
  data: () => ({
    history: null
  }),
  created: function () {
    const historyId = this.$javalin.pathParams["history-id"];
    const url = `/api/histories/${historyId}`
    axios.get(url)
        .then(res =>
            {
              console.log("API Response:", res)
              //res -response. Only the stuffs inside data objects
              this.history = res.data;
              console.log("History Data:", this.history);
            }
        )

        .catch(() => alert("Error while fetching history" + historyId));
  }
});
</script>