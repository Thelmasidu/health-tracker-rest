<template id="user-medication-overview">
  <app-layout>
    <div>
      <h3>Medication list </h3>

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
  </app-layout>
</template>

<script>
app.component("user-medication-overview",{
  template: "#user-medication-overview",
  data: () => ({
     medication: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/medication`)
        .then(res => this.medication = res.data)
        .catch(() => alert("Error while fetching medication"));
  }
});
</script>