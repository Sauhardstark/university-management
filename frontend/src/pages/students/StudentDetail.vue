<script setup>
import { ref, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import http from "../../api/http";

const route = useRoute();
const id = ref(route.params.id);

const student = ref(null);
const enrollments = ref([]);
const loading = ref(true);
const error = ref("");

const courseCache = new Map();
async function getCourse(courseId) {
  if (courseCache.has(courseId)) return courseCache.get(courseId);
  const { data } = await http.get(`/api/courses/${courseId}`);
  courseCache.set(courseId, data);
  return data;
}

async function load() {
  loading.value = true; error.value = "";
  student.value = null; enrollments.value = [];
  try {
    const { data: s } = await http.get(`/api/students/${id.value}`);
    student.value = s;
    const { data: ens } = await http.get(`/api/students/${id.value}/enrollments`);
    const withCourse = await Promise.all(ens.map(async e => {
      const course = await getCourse(e.courseId);
      return { ...e, course };
    }));
    enrollments.value = withCourse;
  } catch (e) {
    error.value = e?.response?.data?.message || e.message;
  } finally {
    loading.value = false;
  }
}

onMounted(load);
watch(() => route.params.id, (nid) => { id.value = nid; load(); });
</script>

<template>
  <section>
    <div v-if="loading">Loading…</div>
    <div v-else-if="error">{{ error }}</div>

    <div v-else-if="student">
      <h1>{{ student.firstName }} {{ student.lastName }}</h1>
      <div>{{ student.email }}</div>

      <h2>Enrollments</h2>
      <ul>
        <li v-for="e in enrollments" :key="e.id">
          <router-link :to="`/courses/${e.course.id}`">
            <strong>{{ e.course.code }}</strong> — {{ e.course.name }}
          </router-link>
          ({{ e.course.startDate }} - {{ e.course.endDate }}) —
          Grade: {{ e.grade ?? "—" }}
        </li>
        <li v-if="!enrollments.length">No enrollments yet.</li>
      </ul>
    </div>
  </section>
</template>
