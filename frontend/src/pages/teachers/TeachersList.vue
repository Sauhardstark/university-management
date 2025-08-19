<script setup>
import { ref, onMounted } from "vue";
import http from "../../api/http";

const teachers = ref([]);
const loading = ref(false);
const error = ref("");

async function load() {
  loading.value = true; error.value = "";
  try {
    const { data } = await http.get("/api/teachers");
    teachers.value = data;
  } catch (e) {
    error.value = e?.response?.data?.message || e.message;
  } finally {
    loading.value = false;
  }
}
onMounted(load);
</script>

<template>
  <section>
    <h1>Teachers</h1>
    <div v-if="loading">Loading…</div>
    <div v-else-if="error">{{ error }}</div>
    <ul v-else>
      <li v-for="t in teachers" :key="t.id">
        <router-link :to="`/teachers/${t.id}`">
          {{ t.firstName }} {{ t.lastName }}
        </router-link>
        — {{ t.email }}
      </li>
      <li v-if="!teachers.length">No teachers found.</li>
    </ul>
  </section>
</template>
