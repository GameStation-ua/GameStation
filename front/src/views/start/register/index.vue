<script setup>

import { ref , defineEmits} from 'vue'
import { notify } from "@kyvg/vue3-notification";



const emit = defineEmits(['changeTab'])

const user = ref({})
const input = ref({
  username: "",
  nickname: "",
  password: "",
  verifypassword: "",
})

function register () {
  var xhr = new XMLHttpRequest()
  const hashed = {
    username : input.value.username,
    nickname : input.value.nickname,
    password : input.value.password
  }
//  hashed.password = sha512(hashed.password)
  var json = JSON.stringify(hashed)
  xhr.open("POST", "/register", false)
      xhr.setRequestHeader("Content-Type", "application/json")
  xhr.send(json)
  console.log(json)
  var mesage = JSON.parse(xhr.response).message
  console.log(mesage)
  if ( mesage === 'User created!'){
    notify({
      type: "success",
      title: "Register success! please Log In",
    });
    emit('changeTab', 'login')
  }
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
          <vs-button @click="register" color="success" type="gradient">Register</vs-button>
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

.vue-notification{
  color: #42b983;
}
</style>