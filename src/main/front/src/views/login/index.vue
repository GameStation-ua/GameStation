<script setup>
  import { ref } from 'vue'

  const user = ref({})

  function login () {
    fetch(process.env.VUE_APP_HTTP_URL + "auth/login", {
      method:"get",
      })
      .then(res=> {
        return res.json()
      })
      .then(res => {
        user.value = res
        console.log(res)
      })
  }
</script>
<template>
  <div>
    <button @click="login">
      get login
    </button>
    {{ user.nickName }}
  </div>
</template>

