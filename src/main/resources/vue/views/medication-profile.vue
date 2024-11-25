<template id="medication-profile">
  <app-layout>
    <div>
      <form v-if="medication">
        <label class="col-form-label">Medication ID: </label>
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

      </form>

    </div>
  </app-layout>

</template>

<script>
app.component("medication-profile", {
  template: "#medication-profile",
  data: () => ({
    medication: null
  }),
  created: function () {
    const medicationId = this.$javalin.pathParams["medication-id"];
    const url = `/api/medication/${medicationId}`
    axios.get(url)
        .then(res =>
            {
              console.log("API Response:", res)
              //res -response. Only the stuffs inside data objects
              this.medication = res.data;
              console.log("Medication Data:", this.medication);
            }
        )

        .catch(() => alert("Error while fetching medication" + medicationId));
  }
});
</script>