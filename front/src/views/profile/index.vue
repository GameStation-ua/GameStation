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
      debelopedGames:[],
      listdata:{
        gameId: '',
        score: 0,
        status: ''
      },
      selectedGame:{},
      popupActivo:false,
    }
  },
  methods: {
    getDevelopedGames(){
      const res = new XMLHttpRequest()
      res.open("GET", "/game/createdgames/" + this.$route.params.id.toString(), false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      this.debelopedGames = JSON.parse(res.response)
      console.log(this.debelopedGames)
    },
    getList(){
      const res = new XMLHttpRequest()
      res.open("GET", "/gamelist/" + this.$route.params.id.toString(), false)
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
      console.log(res.response)
      return 'data:image.png;base64,' + res.response.toString()
    },
    gamepage(id){
      localStorage.setItem("id", id)
      this.page('/gamePage/' + id.toString())
    },
    page(rout){
      router.push(rout)
      localStorage.setItem("inSearch", "false")
    },
    openPopup(game){
      this.selectedGame = game
      this.popupActivo = true
      this.listdata.gameId = game.gameId
      console.log(this.selectedGame)
    },
    editList(){
      const res = new XMLHttpRequest()
      res.open("PATCH", "/gamelist/edit", false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(this.listdata))
      this.popupActivo = false
      location.reload()
    },
    deleteList(game){
      const dellistdata={
        gameId: game.gameId,
        score: game.score,
        status: game.status
      }
      const res = new XMLHttpRequest()
      res.open("PATCH", "/gamelist/delete", false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(dellistdata))
      this.popupActivo = false
      location.reload()
    }
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
          <img :src="'http://localhost:8443/image/profile_pictures/' + user.id.toString() + '.png'" style="width: 100%; border-radius:150px">
        </div>
        <div style="display: block; left: 10%; position: relative; text-align: left">
          <h1>{{user.nickname}}</h1>
          <h2>tags</h2>
          <a v-for="(tag, index) in user.likedTags" :key="index" v-bind:href=" '/search/tag/' + tag " >{{tag}}</a>
        </div>
        <div v-if="user.id === verifi">
          <vs-button  color="primary" type="border" icon="edit" style="position: absolute; display: flex; right: 5%; top: 0" :to="'/profileEdit/' + verifi">Edit User</vs-button>
        </div>
        <div v-else></div>
      </div>
      <div style="margin-top: 30px">
        <vs-tabs :color="colorx" alignment="fixed">
          <vs-tab @click="colorx = 'success'" label="GameList">
            <div class="con-tab-ejemplo" style="display: block">
              <div v-for="(games,index) in gamelist" :key="index" style="position: relative">
                <div class="selection" @click="gamepage(games.gameId)" style="cursor: pointer ">
                  <img :src="'http://localhost:8443/image/games/' + games.gameId.toString() + '/main.png'" style="height: 100%" alt="logo">
                  <div style="display: block; position: relative; left: 10px">
                    <h1>{{ games.title }}</h1>
                    <label v-if="games.score.toString() !== '0'">score: {{games.score.toString()}}</label>
                  </div>
                </div>
                <div class="buttons" v-if="user.id === verifi">
                  <vs-button color="danger" type="border" icon="delete" @click="deleteList(games)"></vs-button>
                  <vs-button color="primary" type="border" icon="edit" @click="openPopup(games)"></vs-button>
                </div>
                <div v-else></div>
              </div>
            </div>
          </vs-tab>
          <vs-tab @click="colorx = 'danger'" label="Published Games">
            <div class="con-tab-ejemplo">
              <div v-for="(games,index) in debelopedGames" :key="index">
                <div class="selection" @click="gamepage(games.gameId)" style="cursor: pointer ">
                  <img :src="'http://localhost:8443/image/games/' + games.gameId.toString() + '/main.png'" style="height: 100%" alt="logo">
                  <div style="display: block; position: relative; left: 10px">
                    <h1>{{games.title}}</h1>
                  </div>
                </div>
                <div v-if="user.id === verifi">
                  <vs-button  color="primary" type="border" icon="edit" style="position: absolute; display: flex; right: 5%; top: 20px" :to="'/editGameMenu/' + games.gameId.toString()"></vs-button>
                </div>
              </div>
            </div>
          </vs-tab>
        </vs-tabs>
        <vs-popup class="holamundo"  title="List data" :active.sync="popupActivo">
          <h1 style="color: black">{{selectedGame.title}}</h1>
          <div style="display: block">
            <label style="color: black">Status</label>
            <ul class="leftx">
              <li>
                <vs-radio color="success" v-model="listdata.status" vs-value="Playing">Playing</vs-radio>
              </li>
              <li>
                <vs-radio color="danger" v-model="listdata.status" vs-value="Dropped">Dropped</vs-radio>
              </li>
              <li>
                <vs-radio color="warning" v-model="listdata.status" vs-value="On hold">On hold</vs-radio>
              </li>
              <li>
                <vs-radio color="dark" v-model="listdata.status" vs-value="Waiting">Waiting</vs-radio>
              </li>
              <li>
                <vs-radio color="rgb(87, 251, 187)" v-model="listdata.status" vs-value="Completed">Completed</vs-radio>
              </li>
            </ul>
            <label style="color: black">Score</label>
            <vs-slider v-model="listdata.score"  max="100"/>
            <div style="display: flex">
              <vs-button @click="editList()" color="success" type="border">Edit</vs-button>
              <vs-button @click="popupActivo = false" color="dark" type="border">Cancel</vs-button>
            </div>
          </div>
        </vs-popup>
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
.buttons{
  position: absolute;
  bottom: 15px;
  right: 15px;
}
</style>