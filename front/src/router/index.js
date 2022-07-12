import { createRouter, createWebHistory } from 'vue-router'
import Home from "@/views/home/index";
import Admin from "@/views/adminMenu/index";
import GamePage from "@/views/gamePage/index";
import UpLoadMenu from "@/views/upLoadMenu/index";
import UpLoadMenuImages from "@/views/upLoadMenu/images/images"
import Search from "@/views/search/index"
import Profile from "@/views/profile/index"
import EditProfile from "@/views/profile/edit/edit"
import SearchTag from  "@/views/search/searchTags"
import EditGameMenu from "@/views/upLoadMenu/editGameMenu"
import UpLoadMenuEditImages from"@/views/upLoadMenu/images/editImages"
import Forum from "@/views/forum/index"
import ForumThread from "@/views/thread"

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
  },
  {
    path:'/profile/:id',
    name: 'profile',
    component: Profile
  },
  {
    path: '/profileEdit/:id',
    name: 'profileEdit',
    component: EditProfile
  },
  {
    path: '/search/tag/:tag',
    name: 'editProfile',
    component: SearchTag
  },
  {
    path: '/editGameMenu/:id',
    name: 'editGameMenu',
    component: EditGameMenu
  },
  {
    path:'/upLoadMenu/Editimages/:id',
    name: 'upLoadMenuEditImages',
    component: UpLoadMenuEditImages
  },
  {
    path:'/forum/:id',
    name: 'forum',
    component: Forum
  },
  {
    path:'/forum/thread/:id',
    name: 'forumThread',
    component: ForumThread
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
