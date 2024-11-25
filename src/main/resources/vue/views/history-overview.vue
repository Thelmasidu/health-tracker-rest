<template id="history-overview">
  <app-layout>
    <div>
      <div>
        <ul class="histories-overview-list">
          <li v-for="history in histories">
            <a :href="`/histories/${history.id}`">heart-rate - {{history.heartRate}}</a>
          </li>
        </ul>
      </div>
    </div>
  </app-layout>

</template>
<script>
app.component("history-overview", {
  template: "#history-overview",
  data: () => ({
    histories: [],
  }),
  created() {
    this.fetchHistories();
  },
  methods: {
    fetchHistories: function () {
      axios.get("/api/histories")
          .then(res => this.histories = res.data)
          .catch(() => alert("Error while fetching histories"));
    }
  }
});
</script>