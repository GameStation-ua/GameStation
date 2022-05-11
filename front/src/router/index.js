import { createRouter, createWebHistory } from 'vue-router'
import Menu from "@/views/home";

const routes = [
  {
    path: '/',
    name: 'menu',
    component: Menu
  },

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
