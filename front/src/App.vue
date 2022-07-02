<script setup>
  import Start from "@/views/start";
  import {ref} from 'vue'

  let active = ref(false)
  let isAdmin = ref(localStorage.getItem("isAdmin").toString())



  import Store from './store'
  import router from "@/router";

  function logout(){
    setTimeout(()=>{
      Store.state.mesage = "";
      localStorage.setItem("token", "")
    },1000);
  }
  function getImg(){
    const res = new XMLHttpRequest()
    res.open("GET", "/image", false)
    res.setRequestHeader("Content-Type", "application/json")
    res.setRequestHeader("path", "/profile_pictures/" + JSON.parse(localStorage.getItem('userData')).id.toString() + '.png')
    res.setRequestHeader("token", localStorage.getItem("token"))
    res.send(null)
    console.log(res.responseText)
    if (res.status === 200){
      return 'data:image.png;base64,' + res.response.toString()
    }else{
      return 'https://www.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png'
    }

  }
  function search(){
    if (localStorage.getItem("inSearch") === "true"){
      router.push("/search/" + Store.state.search.toString())
      setTimeout(()=>{
        location.reload()
      },10);
    }else{
      localStorage.setItem("inSearch", "true")
      router.push("/search/" + Store.state.search.toString())
    }
  }

  function profile(){
    localStorage.setItem('selectedProfile', localStorage.getItem('userData'))
    const data = JSON.parse(localStorage.getItem('userData'))
    page('/profile/' + data.id)
  }

  function page(rout){
    localStorage.setItem("inSearch", "false")
    router.push(rout)
  }


</script>
<template lang="html">
  <Start v-if="!$store.state.mesage"></Start>
  <template v-else>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"
          rel="stylesheet">
    <vs-navbar v-model="activeItem" class="nabarx">
        <vs-button radius @click="active=!active" color="dark" type="flat" icon="menu" id="one"></vs-button>
        <img class="logo" alt="GS logo" src="@/assets/navIcon.png" @click="page('/')">
      <div class="rightx">
        <vs-input icon="search"  placeholder="Search" v-model="$store.state.search" v-on:keyup.enter="search" color="success"/>
        <div class="dropdown">
          <vs-dropdown vs-trigger-click="true">
            <vs-button radius color="dark" type="flat" icon="notifications" id="two"></vs-button>
            <vs-dropdown-menu>
              <vs-dropdown-item>
                option 1
              </vs-dropdown-item>
              <vs-dropdown-item>
                option 2
              </vs-dropdown-item>
              <vs-dropdown-group >
                <vs-dropdown-item>
                  option 1
                </vs-dropdown-item>
                <vs-dropdown-item>
                  option 2
                </vs-dropdown-item>

              </vs-dropdown-group>
              <vs-dropdown-item divider>
                option 3
              </vs-dropdown-item>
            </vs-dropdown-menu>
          </vs-dropdown>
        </div>
        <vs-button radius color="dark" type="flat" icon="file_upload" id="upload" @click="page('/upLoadMenu')"></vs-button>
        <vs-avatar :src="getImg()" id="three"/>
      </div>
    </vs-navbar>
    <div id="parentx">
      <vs-sidebar parent="body" default-index="2"  color="success" class="sidebarx" spacer v-model="active">
        <div class="header-sidebar" slot="header">
          <vs-avatar  size="70px" :src="getImg()"/>
          <h4>
            My Name
            <vs-button color="success" icon="more_horiz" type="flat"></vs-button>
          </h4>
        </div>
        <vs-sidebar-item index="1" icon="home" color="success" @click="page('/')">
          Home
        </vs-sidebar-item>
        <vs-divider icon="person" position="left">
          User
        </vs-divider>
        <vs-sidebar-item index="2" icon="account_box" @click="profile()">
          Profile
        </vs-sidebar-item>
        <vs-sidebar-item index="3" icon="list">
          My List
        </vs-sidebar-item>
        <vs-sidebar-item index="4" icon="games">
          My Games
        </vs-sidebar-item>
        <div v-if="isAdmin === 'true'">
          <vs-divider  icon="code" position="left">
            User
          </vs-divider>
          <div>
          </div>
          <vs-sidebar-item  @click="page('/adminMenu')" index="6" icon="developer_mode">
            Admin Menu
          </vs-sidebar-item>
        </div>
        <div v-else></div>
        <div class="footer-sidebar" slot="footer">
          <vs-button icon="logout" color="danger" type="flat" @click="active = false; logout()">log out</vs-button>
          <vs-button icon="settings" color="success" type="border" ></vs-button>
        </div>
      </vs-sidebar>
    </div>
    <router-view/>
  </template>
  <notifications />
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  width: 100%;
  height: 100%;
}
.vs-navbar{
  background: #309c61 !important;
  display: grid !important;
  width: 100% !important;
}
.logo{
  width: 180px;
  height: auto;
  cursor: pointer;
}
.vs-con-items{
margin-left: 20px;
}
.rightx{
  display: flex;
  position: fixed;
  right: 20px;
}
#two{
  right: -10px;
}
#three{
  right: -10px;
}
.vs-sidebar{
  background: #181818 !important;
}
.header-sidebar{
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  width: 100%
}
h4{
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}
.vs-button-primary.vs-button-flat{
  margin-left: 10px
}
.footer-sidebar{
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  bottom: 0;
  position: absolute;
}
h4{
  color: white;
}

.vs-sidebar--item{
  color: white;
}

.vs-divider{
  color: white;
}

.vs-dropdown--menu--after{
  right: 0px !important;
}

.vs-input{
  position: absolute;
  width: 50%;
}

#upload{
  right: -10px;
}

</style>
