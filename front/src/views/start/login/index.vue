<script setup>
  import router from "@/router";
  import { ref } from 'vue'
  import Store from '../../../store'
  import {sha512} from "js-sha512";
  import { notify } from "@kyvg/vue3-notification";



  const user = ref({})
  const input = ref({
    username: "",
    password: ""
  })

  function login () {
    var xhr = new XMLHttpRequest()
    const hashed = {
      username : input.value.username,
      password : input.value.password
    }
    hashed.password = sha512(hashed.password)
    var json = JSON.stringify(hashed)
    xhr.open("POST", "/login", false)
    xhr.setRequestHeader("Content-Type", "application/json")
    xhr.send(json)
    console.log(json)
    if (xhr.status === 200){
      const token = JSON.parse(xhr.response)
      console.log(token.message)
      Store.state.mesage = "send to home"
      localStorage.setItem("token",token.message)
      const res = new XMLHttpRequest()
      res.open("GET", '/isAdmin', false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      const is = JSON.parse(res.response).message
      console.log(is)
      localStorage.setItem("isAdmin", is)
      const ros = new XMLHttpRequest()
      ros.open("GET", "/home", false)
      ros.setRequestHeader("Content-Type", "application/json")
      ros.setRequestHeader("token", localStorage.getItem("token"))
      ros.send(null)
      console.log(ros.responseText)
      localStorage.setItem('userData', JSON.stringify(JSON.parse(ros.response).user))
      router.push('/')
      location.reload()
    }else{
      notify({
        type: "error",
        title: "Username or password are wrong",
      });
    }
  }

</script>
<template>
    <h1>Login</h1>
    <vs-row>
      <vs-col>
        <div>
          <div class="centerx">
            <vs-input label-placeholder="Username" v-model="input.username" size="large" color="success"/>
            <vs-input label-placeholder="Password" v-model="input.password" size="large" type="password" color="success"/>
            <vs-button @click="login" color="success" type="gradient">Login</vs-button>
          </div>
          {{ user.username }}
        </div>
      </vs-col>
    </vs-row>

</template>

<style scoped>
  .centerx  .vs-button{
    margin-top: 30px;
    width: 80%;
  }
  .centerx  .vs-input{
    margin-top: 30px;
    width: 80%;
    margin-left: 50px;
  }

</style>

