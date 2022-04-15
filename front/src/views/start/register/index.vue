<script setup>
import { ref } from 'vue'


const user = ref({})
const input = ref({
  userName: "",
  nickname: "",
  password: "",
  verifypassword: "",
  token: "",
})
function register () {
  fetch(process.env.VUE_APP_HTTP_URL + "/register", {
    method:"post",
    body: JSON.stringify(input.value)
  })
    .then(res=> {
      return res.json()
    })
    .then(res => {
      user.value = res
    })
}


</script>

<template>

  <h1>Register</h1>
  <vs-row>
    <vs-col >
      <div>
        <div class="centerx">
          <vs-input label-placeholder="Username" v-model="input.userName" size="large" color="success"/>
          <vs-input label-placeholder="Nickname" v-model="input.nickname" size="large" color="success"/>
          <vs-input label-placeholder="Password" v-model="input.password" size="large" type="password" color="success"/>
          <vs-input label-placeholder="Verify password" v-model="input.verifypassword" size="large" type="password" color="success"/>
          <vs-button @click="register" color="success" type="gradient">Login</vs-button>
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