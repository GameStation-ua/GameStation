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
  <div>
    <input v-model="input.userName" type="text" />
    <input v-model="input.password" type="password" />
    <button @click="login">
      get login
    </button>
    {{ user.nickName }}
  </div>
</template>

