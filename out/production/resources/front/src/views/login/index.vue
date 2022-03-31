<script setup>
  import { ref } from 'vue'

  const user = ref({})
  const input = ref({
    userName: "",
    password: ""
  })

  function login () {
    fetch(process.env.VUE_APP_HTTP_URL + "/auth/login", {
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
    <img class="logo" alt="GS logo" src="@/assets/logo_tp_medios.png">
    <h1>Iniciar sesión</h1>
    <vs-row>
      <vs-col>
        <div>
          <div class="centerx">
            <vs-input label-placeholder="Username" v-model="input.userName" size="large"/>
            <vs-input label-placeholder="Password" v-model="input.password" size="large" type="password"/>
            <vs-button @click="login" color="success" type="gradient">Login</vs-button>
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
    width: 100%;
  }
</style>

