<script>
import router from "@/router";
export default {
  name: "index",
  data(){
    return{
      verifi: JSON.parse(localStorage.getItem('userData')).id,
      user: {},
      colorx:'success',
      gamelist:[],
      debelopedGames:[]
    }
  },
  methods: {
    getDevelopedGames(){
      const res = new XMLHttpRequest()
      res.open("GET", "/game/createdgames/" + this.user.id, false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      this.debelopedGames = JSON.parse(res.response)
      console.log(this.debelopedGames)
    },
    getList(){
      const res = new XMLHttpRequest()
      res.open("GET", "/gamelist/" + localStorage.getItem('userId'), false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      this.gamelist = JSON.parse(res.response)
      console.log(this.gamelist)
    },
    getUser(){
      this.user = JSON.parse(localStorage.getItem('selectedProfile'))
      console.log(this.user.id)
      console.log(this.verifi)
      console.log(this.$route.params)
    },
    getImg(id){
      const res = new XMLHttpRequest()
      res.open("GET", "/image", false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("path", "/games/" + id.toString() + "/main.png")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      return res.response
    },
    gamepage(id){
      localStorage.setItem("id", id)
      this.page('/gamePage/' + id.toString())
    },
    page(rout){
      router.push(rout)
      localStorage.setItem("inSearch", "false")
    },
  },
  beforeMount() {
    this.getUser()
    this.getList()
    this.getDevelopedGames()
  }
}
</script>

<template>
  <div class="scroller">
    <div class="form-box">
      <div style="display: flex; position: relative">
        <div style="width: 15%; position: relative; left: 5%">
          <img src="https://plantillasdememes.com/img/plantillas/messirve01624673157.jpg" style="width: 100%; border-radius:150px">
        </div>
        <div style="display: block; left: 10%; position: relative; text-align: left">
          <h2>Nickname</h2>
          <h3>{{user.nickname}}</h3>
          <h2>tags</h2>
          <a v-for="(tag, index) in user.likedTags" :key="index" v-bind:href=" '/search/tag/' + tag " >{{tag}}</a>
        </div>
        <div v-if="user.id === verifi">
          <vs-button  color="primary" type="border" icon="edit" style="position: absolute; display: flex; right: 5%; top: 0">Edit User</vs-button>
        </div>
        <div v-else></div>
      </div>
      <div style="margin-top: 30px">
        <vs-tabs :color="colorx" alignment="fixed">
          <vs-tab @click="colorx = 'success'" label="GameList">
            <div class="con-tab-ejemplo">
              <div v-for="(games,index) in gamelist" :key="index" class="selection" @click="gamepage(games.gameId)" style="cursor: pointer ">
                <img :src="'data:image.png;base64,' + getImg(games.gameId)" style="height: 100%" alt="logo">
                <div style="display: block; position: relative; left: 10px">
                  <h1>{{ gameSearch.title }}</h1>
                </div>
              </div>
            </div>
          </vs-tab>
          <vs-tab @click="colorx = 'danger'" label="Published Games">
            <div class="con-tab-ejemplo">
              <div v-for="(games,index) in debelopedGames" :key="index" class="selection" @click="gamepage(games.gameId)" style="cursor: pointer ">
                <img :src="'data:image.png;base64,' + getImg(games.gameId)" style="height: 100%" alt="logo">
                <div style="display: block; position: relative; left: 10px">
                  <h1>{{ games.title }}</h1>
                </div>
              </div>
            </div>
          </vs-tab>
        </vs-tabs>
      </div>
    </div>

  </div>
</template>

<style scoped>
.form-box{
  width: 50%;
  background: rgba(0, 0, 0, 0.8);
  padding: 50px 0;
  color: #fff;
  box-shadow: 0 0 20px 2px rgba(0, 0, 0, 0.5);
  margin: 30px auto;
  height: fit-content;
}
.selection{
  background: rgba(0, 0, 0, 0.8);
  color: #000000;
  position: relative;
  left: 10px;
  display: flex;
  height: 150px;
  margin-right: 10px;
  margin-bottom: 10px;
}
</style>