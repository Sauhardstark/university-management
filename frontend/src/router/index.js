import { createRouter, createWebHistory } from "vue-router";
import CoursesList from "../pages/courses/CoursesList.vue";
import CourseDetail from "../pages/courses/CourseDetail.vue";
import TeachersList from "../pages/teachers/TeachersList.vue";
import TeacherDetail from "../pages/teachers/TeacherDetail.vue";
import StudentsList from "../pages/students/StudentsList.vue";
import StudentDetail from "../pages/students/StudentDetail.vue";

export default createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/", redirect: "/courses" },
    { path: "/courses", component: CoursesList },
    { path: "/courses/:id", component: CourseDetail, props: true },
    { path: "/students", component: StudentsList },
    { path: "/students/:id", component: StudentDetail, props: true },
    { path: "/teachers", component: TeachersList },
    { path: "/teachers/:id", component: TeacherDetail, props: true }
  ],
});
