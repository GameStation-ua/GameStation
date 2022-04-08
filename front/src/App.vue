<script setup>
  import Start from "@/views/start";
</script>
<template lang="html">
  <Start v-if="!$store.state.user">

  </Start>
  <template v-else>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"
          rel="stylesheet">
    <vs-navbar v-model="activeItem" class="nabarx">
      <div id="parentx">

        <vs-button @click="active=!active" color="primary" type="filled">Open Sidebar</vs-button>
        <vs-sidebar parent="body" default-index="1"  color="primary" class="sidebarx" spacer v-model="active">

          <div class="header-sidebar" slot="header">
            <vs-avatar  size="70px" src="https://randomuser.me/api/portraits/men/85.jpg"/>

            <h4>
              My Name
              <vs-button color="primary" icon="more_horiz" type="flat"></vs-button>
            </h4>

          </div>

          <vs-sidebar-item index="1" icon="question_answer">
            Dashboard
          </vs-sidebar-item>

          <vs-sidebar-item index="2" icon="gavel">
            History
          </vs-sidebar-item>

          <vs-divider icon="person" position="left">
            User
          </vs-divider>

          <vs-sidebar-item index="3" icon="verified_user">
            Configurations
          </vs-sidebar-item>
          <vs-sidebar-item index="4" icon="account_box">
            Profile
          </vs-sidebar-item>
          <vs-sidebar-item index="5" >
            Card
          </vs-sidebar-item>

          <div class="footer-sidebar" slot="footer">
            <vs-button icon="reply" color="danger" type="flat">log out</vs-button>
            <vs-button icon="settings" color="primary" type="border"></vs-button>
          </div>

        </vs-sidebar>
      </div>
      <div>
        <img class="logo" alt="GS logo" src="@/assets/logo_tp_medios.png">
      </div>
      <div slot="title">
        <vs-navbar-title>
          Game Station
        </vs-navbar-title>
      </div>
      <vs-navbar-item index="0">
        <a href="#">Games</a>
      </vs-navbar-item>
      <vs-navbar-item index="1">
        <a href="#">News</a>
      </vs-navbar-item>
      <vs-navbar-item index="2">
        <a href="#">News</a>
      </vs-navbar-item>
      <vs-navbar-item index="3">
        <a href="#">Update</a>
      </vs-navbar-item>
      <vs-input icon="search" placeholder="Search" v-model="$store.state.search"/>
      <vs-button radius color="dark" type="flat" icon="notifications"></vs-button>
      <div>

      </div>
    </vs-navbar>
    <router-view/>
  </template>
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
  background: #1b9a59 !important;
}

img{
  width: 100px;
  height: auto;
  left: 100px;

}
.centerx  .vs-button{
  left: 0px;
}
</style>
