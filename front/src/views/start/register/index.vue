<script setup>
import { ref } from 'vue'
import Store from "@/store";

const numeros = require("core-js/internals/array-includes");
const letras_mayusculas = require("core-js/internals/array-includes");



const user = ref({})
const input = ref({
  username: "",
  nickname: "",
  password: "",
  verifypassword: "",
})

var one = false;
var two = false;
var three = false;
var four = false
function register () {
  var xhr = new XMLHttpRequest()
  const hashed = {
    username : input.value.username,
    nickname : input.value.nickname,
    password : input.value.password
  }
//    hashed.password = sha512(hashed.password)
  var json = JSON.stringify(hashed)
  xhr.open("POST", "http://localhost:8443/register", false),
      xhr.setRequestHeader("Content-Type", "application/json")
  xhr.send(json)
  console.log(json)
  if (xhr.status === 200){
    Store.state.mesage = ""
    localStorage.setItem("token",xhr.responseText)
    console.log(localStorage.getItem("token"))
  }
}

function passwords(){
  if (tiene_numeros(input.value.password) && tiene_mayusculas(input.value.password)){
    two=true
    if (input.value.password === input.value.verifypassword){
      four=true
      register()
    }else three=true
  }else one=true
}

function tiene_numeros(texto){
  let i;
  for(i=0; i<texto.length; i++){
    if (numeros.indexOf(texto.charAt(i),0)!==-1){
      return 1;
    }
  }
  return 0;
}

function tiene_mayusculas(texto){
  let i;
  for(i=0; i<texto.length; i++){
    if (letras_mayusculas.indexOf(texto.charAt(i),0)!==-1){
      return 1;
    }
  }
  return 0;
}





</script>

<template>

  <h1>Register</h1>
  <vs-row>
    <vs-col >
      <div>
        <div class="centerx">
          <vs-input label-placeholder="Username" v-model="input.username" size="large" color="success"/>
          <vs-input label-placeholder="Nickname" v-model="input.nickname" size="large" color="success"/>
          <vs-input label-placeholder="Password" v-model="input.password" size="large" type="password" color="success" :danger="one"  :success="two"   description-text="the password needs 8 or more characters, numbers and capital letters"/>
          <vs-input label-placeholder="Verify password" v-model="input.verifypassword" size="large" type="password" color="success" :danger="three"  :success="four"  description-text="wright the same password"/>
          <vs-button @click="passwords" color="success" type="gradient">Register</vs-button>
        </div>
        {{ user.nickName }}
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