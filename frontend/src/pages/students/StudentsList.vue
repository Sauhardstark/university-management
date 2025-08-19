<script setup>
import { ref, onMounted } from "vue";
import http from "../../api/http";

const students = ref([]);
const loading = ref(false);
const error = ref("");

async function load() {
  loading.value = true; error.value = "";
  try {
    const { data } = await http.get("/api/students");
    students.value = data;
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
    <h1>Students</h1>
    <div v-if="loading">Loading…</div>
    <div v-else-if="error">{{ error }}</div>
    <ul v-else>
      <li v-for="s in students" :key="s.id">
        <router-link :to="`/students/${s.id}`">
          {{ s.firstName }} {{ s.lastName }}
        </router-link>
        — {{ s.email }}
      </li>
      <li v-if="!students.length">No students found.</li>
    </ul>
  </section>
</template>
