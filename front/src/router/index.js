import { createRouter, createWebHistory } from 'vue-router'
import Home from "@/views/home/index";
import Admin from "@/views/adminMenu/index";
import GamePage from "@/views/gamePage/index";
import UpLoadMenu from "@/views/upLoadMenu/index";
import UpLoadMenuImages from "@/views/upLoadMenu/images/images"
import Search from "@/views/search/index"

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/adminMenu',
    name: 'adminMenu',
    component: Admin
  },
  {
    path: '/gamePage/:id',
    name: 'gamePage',
    component: GamePage
  },
  {
    path: '/upLoadMenu',
    name: 'upLoadMenu',
    component: UpLoadMenu
  },
  {
    path:'/upLoadMenuImages/:id',
    name: 'upLoadMenuImages',
    component: UpLoadMenuImages
  },
  {
    path:'/search/:id',
    name: 'search',
    component: Search
  }





]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
