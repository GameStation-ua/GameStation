<script setup>
  import {ref} from 'vue'
  // eslint-disable-next-line no-unused-vars
  import Login from './login'
  // eslint-disable-next-line no-unused-vars
  import Register from './register'
  const tab = ref('login')
</script>
<template>
  <div class="start">
    <div class="form-box">
      <nav>
        <button @click="tab = 'login'" :class="{ active: tab === 'login' }">LogIn</button> |
        <button @click="tab = 'register'" :class="{ active: tab === 'register' }">Register</button>
      </nav>
      <img class="logo" alt="GS logo" src="@/assets/logo_tp_medios.png">
      <Login v-if="tab==='login'">
      </Login>
      <Register v-if="tab==='register'">

      </Register>
    </div>
  </div>

</template>



<style scoped>
nav {
  padding: 30px;
}

nav button {
  font-weight: bold;
  color: #2c3e50;
  background: transparent;
  border: none;
}

nav button.active {
  color: #42b983;
}
.form-box{
  width: 500px;
  background: rgba(0,0,0,0.8);
  padding: 50px 0;
  color: #fff;
  box-shadow: 0 0 20px 2px rgba(0,0,0,0.5);
  margin: 30px auto;
  height: fit-content;
}
.logo{
  width: 500px;
}
.start{
  width: 100%;
  height: 100%;
  background: url("@/assets/4848691.jpg");
  display: flex;
  justify-content: center;
  overflow-y: auto;
  overflow-y: overlay;

}

</style>