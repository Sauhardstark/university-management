<script setup>
import { ref, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import http from "../../api/http";

const route = useRoute();
const id = ref(route.params.id);

const teacher = ref(null);
const courses = ref([]);
const stats = ref(new Map());
const loading = ref(true);
const error = ref("");

const FAIL = 50;

function computeStats(ens) {
  const graded = ens.map(e => e.grade).filter(g => g != null);
  const count = ens.length;
  const fails = graded.filter(g => g < FAIL).length;
  const avg = graded.length ? Math.round((graded.reduce((a, b) => a + b, 0) / graded.length) * 10) / 10 : null;
  return { avg, count, fails };
}

async function load() {
  loading.value = true; error.value = "";
  teacher.value = null; courses.value = []; stats.value = new Map();
  try {
    const { data: t } = await http.get(`/api/teachers/${id.value}`);
    teacher.value = t;
    const { data: cs } = await http.get(`/api/teachers/${id.value}/courses`);
    courses.value = cs;
    const pairs = await Promise.all(cs.map(async c => {
      const { data: ens } = await http.get(`/api/courses/${c.id}/enrollments`);
      return [c.id, computeStats(ens)];
    }));
    stats.value = new Map(pairs);
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

    <div v-else-if="teacher">
      <h1>{{ teacher.firstName }} {{ teacher.lastName }}</h1>
      <div>{{ teacher.email }}</div>

      <h2>Courses</h2>
      <ul>
        <li v-for="c in courses" :key="c.id">
          <router-link :to="`/courses/${c.id}`">
            <strong>{{ c.code }}</strong> — {{ c.name }}
          </router-link>
          ({{ c.startDate }} - {{ c.endDate }}) |
          Avg: {{ stats.get(c.id)?.avg ?? "—" }},
          Enrollments: {{ stats.get(c.id)?.count ?? 0 }},
          Fails (< {{ FAIL }}): {{ stats.get(c.id)?.fails ?? 0 }}
        </li>
        <li v-if="!courses.length">No courses for this teacher.</li>
      </ul>
    </div>
  </section>
</template>
