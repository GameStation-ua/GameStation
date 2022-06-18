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

  function search(){
    localStorage.setItem("search", Store.state.search)
    if (localStorage.getItem("inSearch") === "true"){
      location.reload()
    }else{
      localStorage.setItem("inSearch", "true")
      router.push("/search/" + Store.state.search.toString())
    }
  }

  function profile(){
    localStorage.setItem('selectedProfile', localStorage.getItem('userData'))
    const data = localStorage.getItem('userData')
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
        <vs-avatar src="data:image/jpeg;base64,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" id="three"/>
      </div>
    </vs-navbar>
    <div id="parentx">
      <vs-sidebar parent="body" default-index="2"  color="success" class="sidebarx" spacer v-model="active">
        <div class="header-sidebar" slot="header">
          <vs-avatar  size="70px" src="data:image/jpeg;base64,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"/>
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
