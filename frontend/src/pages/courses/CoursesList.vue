<script setup>
import { ref, onMounted } from "vue";
import http from "../../api/http";

const q = ref("");
const courses = ref([]);
const loading = ref(false);
const error = ref("");

async function fetchCourses() {
  loading.value = true; error.value = "";
  try {
    if (q.value.trim()) {
      const { data } = await http.get("/api/courses/search", { params: { name: q.value.trim() } });
      courses.value = data;
    } else {
      const { data } = await http.get("/api/courses");
      courses.value = data;
    }
  } catch (e) {
    error.value = e?.response?.data?.message || e.message;
  } finally {
    loading.value = false;
  }
}
function onSearch() { fetchCourses(); }
onMounted(fetchCourses);
</script>

<template>
  <section>
    <h1>Courses</h1>
    <div>
      <input v-model="q" @keyup.enter="onSearch" placeholder="Search by course name…" />
      <button @click="onSearch">Search</button>
      <button @click="() => { q=''; onSearch(); }">Clear</button>
    </div>

    <div v-if="loading">Loading…</div>
    <div v-else-if="error">{{ error }}</div>

    <ul v-else>
      <li v-for="c in courses" :key="c.id">
        <router-link :to="`/courses/${c.id}`">
          <strong>{{ c.code }}</strong> — {{ c.name }}
        </router-link>
        ({{ c.startDate }} - {{ c.endDate }})
      </li>
      <li v-if="!courses.length">No courses found.</li>
    </ul>
  </section>
</template>
