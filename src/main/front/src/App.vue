<template>
  <div class="form-box">
    <nav>
      <router-link to="/">Home</router-link> |
      <router-link to="/login">LogIn</router-link> |
      <router-link to="/register">Register</router-link>
    </nav>
    <router-view/>
  </div>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
.form-box{
  width: 500px;
  background: rgba(0,0,0,0.8);
  margin: -2% auto;
  padding: 50px 0;
  color: #fff;
  box-shadow: 0 0 20px 2px rgba(0,0,0,0.5);
}
</style>
