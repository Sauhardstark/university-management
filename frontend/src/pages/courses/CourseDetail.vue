<script setup>
import { ref, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import http from "../../api/http";

const route = useRoute();
const id = ref(route.params.id);

const course = ref(null);
const enrollments = ref([]);
const loading = ref(true);
const error = ref("");

const teachers = ref([]);
const selectedTeacherId = ref("");

function stats() {
  const graded = enrollments.value.map(e => e.grade).filter(g => g != null);
  const avg = graded.length ? Math.round((graded.reduce((a,b)=>a+b,0)/graded.length)*10)/10 : null;
  const fails = graded.filter(g => g < 50).length;
  return { avg, count: enrollments.value.length, fails };
}

function teacherName(id) {
  const t = teachers.value.find(x => x.id === id);
  return t ? `${t.firstName} ${t.lastName}` : "—";
}

async function load() {
  loading.value = true; error.value = "";
  try {
    const { data: c } = await http.get(`/api/courses/${id.value}`);
    course.value = c;
    const { data: ens } = await http.get(`/api/courses/${id.value}/enrollments`);
    enrollments.value = ens;
    const { data: ts } = await http.get(`/api/teachers`);
    teachers.value = ts;
    selectedTeacherId.value = c.teacherId || "";
  } catch (e) {
    error.value = e?.response?.data?.message || e.message;
  } finally {
    loading.value = false;
  }
}

async function assignTeacher() {
  if (!selectedTeacherId.value) return;
  const { data } = await http.put(`/api/courses/${id.value}/teacher/${selectedTeacherId.value}`);
  course.value = data;
}

async function unassignTeacher() {
  await http.delete(`/api/courses/${id.value}/teacher`);
  course.value = { ...course.value, teacherId: null };
  selectedTeacherId.value = "";
}

onMounted(load);
watch(() => route.params.id, (nid) => { id.value = nid; load(); });
</script>

<template>
  <section>
    <div v-if="loading">Loading…</div>
    <div v-else-if="error">{{ error }}</div>

    <div v-else-if="course">
      <h1><strong>{{ course.code }}</strong> — {{ course.name }}</h1>
      <div>Dates: {{ course.startDate }} - {{ course.endDate }}</div>

      <h2>Teacher</h2>
      <div>
        Current: {{ course.teacherId ? teacherName(course.teacherId) : "—" }}
      </div>
      <div>
        <select v-model="selectedTeacherId">
          <option value="">-- select teacher --</option>
          <option v-for="t in teachers" :key="t.id" :value="t.id">
            {{ t.firstName }} {{ t.lastName }}
          </option>
        </select>
        <button @click="assignTeacher">Assign</button>
        <button @click="unassignTeacher" :disabled="!course.teacherId">Unassign</button>
      </div>

      <h2>Enrollments</h2>
      <div>
        Avg: {{ stats().avg ?? "—" }},
        Count: {{ stats().count }},
        Fails (< 50): {{ stats().fails }}
      </div>
    </div>
  </section>
</template>
