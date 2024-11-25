<template id="medication-overview">
  <app-layout>
    <div>
      <div>
        <ul class="medication-overview-list">
          <li v-for="medication in medication">
            <a :href="`/medication/${medication.id}`">{{medication.medicationName}} </a>
          </li>
        </ul>
      </div>
    </div>
  </app-layout>

</template>
<script>
app.component("medication-overview", {
  template: "#medication-overview",
  data: () => ({
    medication: [],
  }),
  created() {
    this.fetchMedications();
  },
  methods: {
    fetchMedications: function () {
      axios.get("/api/medication")
          .then(res => this.medication = res.data)
          .catch(() => alert("Error while fetching medication"));
    }
  }
});
</script>