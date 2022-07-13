<script>
import router from "@/router";
export default {
  name: "index",
  data(){
    return {
      searchresultGames:[],
      searchresultusers:[],
      searchinput: this.$route.params.id.toString(),
      listdata:{
        gameId: '',
        score: 0,
        status: ''
      },
      selectedGame:{},
      popupActivo:false,
    }
  },
  methods:{
    page(rout){
      router.push(rout)
    },
    getSearch(){
      const res = new XMLHttpRequest()
      res.open("GET", "/search/" + this.searchinput, false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.responseText)
      this.searchresultGames = JSON.parse(res.response).foundGames
      this.searchresultusers = JSON.parse(res.response).foundUsers
      console.log(this.searchresult)
    },
    gamepage(id){
      this.page('/gamePage/' + id.toString())
    },
    userpage(user){
      localStorage.setItem('selectedProfile', JSON.stringify(user))
      this.page('/profile/' + user.id.toString())
    },
    followGame(game){
      const data = {
        path: "/profile/" + JSON.parse(localStorage.getItem('userData')).id.toString()
      }
      const res = new XMLHttpRequest()
      res.open("PATCH", "/follow/add/" + game.gameId.toString() , false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(data))
      if (res.status === 200){
        game.isFollowing = true
      }
    },

    unfollowGame(game){
      const data = {
        path: "/profile/" + JSON.parse(localStorage.getItem('userData')).id.toString()
      }
      const res = new XMLHttpRequest()
      res.open("PATCH", "/follow/delete/" + game.gameId.toString() , false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(data))
      if (res.status === 200){
        game.isFollowing = false
      }
    },
    openPopup(game){
      this.selectedGame = game
      this.popupActivo = true
      this.listdata.gameId = game.gameId
      console.log(this.selectedGame)
    },
    followUser(user){
      const data = {
        path: "/profile/" + JSON.parse(localStorage.getItem('userData')).id.toString()
      }
      const res = new XMLHttpRequest()
      res.open("PATCH", "/follow/add/" + user.id.toString() , false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(data))
      if (res.status === 200){
        user.isFollowing = true
      }
    },

    unfollowUser(user){
      const data = {
        path: "/profile/" + JSON.parse(localStorage.getItem('userData')).id.toString()
      }
      const res = new XMLHttpRequest()
      res.open("PATCH", "/follow/delete/" + user.id.toString() , false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(data))
      if (res.status === 200){
        user.isFollowing = false
      }
    },
    addToList(){
      const res = new XMLHttpRequest()
      res.open("PATCH", "/gamelist/add", false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(this.listdata))
      this.popupActivo = false

    },
  },
  beforeMount() {
    this.getSearch()
  }
}
</script>

<template>
  <div class="scroller">
    <div style="width: 70%; margin: auto" >
      <h1>Search</h1>
      <h3>Related games</h3>
      <div v-for="(gameSearch,index) in searchresultGames" :key="index" style="position: relative">
        <div @click="gamepage(gameSearch.gameId)" style="cursor: pointer" class="selection">
          <img :src="'http://localhost:8443/image/games/' + gameSearch.gameId.toString() + '/main.png'" style="height: 100%" alt="logo">
          <div style="display: block; position: relative; left: 10px">
            <h1>{{ gameSearch.title }}</h1>
          </div>
        </div>
        <div class="buttons">
          <vs-button v-if="gameSearch.isFollowing === true" color="danger" type="border" icon="heart_broken" @click="unfollowGame(gameSearch)"></vs-button>
          <vs-button v-else color="danger" type="border" icon="favorite" @click="followGame(gameSearch)"></vs-button>
          <vs-button color="primary" type="border" icon="add" @click="openPopup(gameSearch)"></vs-button>
        </div>
      </div>
      <h3>Related users and Developers</h3>
      <div v-for="(userSearch,index) in searchresultusers" :key="index" style="position: relative">
        <div class="selection" @click="userpage(userSearch)" style="cursor: pointer ">
          <vs-avatar size="140px" :src="'http://localhost:8443/image/profile_pictures/' + userSearch.id.toString() + '.png'"/>
          <div style="display: block; position: relative; left: 10px">
            <h1>{{ userSearch.nickname }}</h1>
          </div>
        </div>
        <div class="buttons">
          <vs-button v-if="userSearch.isFollowing === true" color="danger" type="border" icon="heart_broken" @click="unfollowUser(userSearch)"></vs-button>
          <vs-button v-else color="danger" type="border" icon="favorite" @click="followUser(userSearch)"></vs-button>
        </div>
      </div>
    </div>
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
          <vs-button @click="addToList()" color="success" type="border">Add</vs-button>
          <vs-button @click="popupActivo = false" color="dark" type="border">Cancel</vs-button>
        </div>
      </div>
    </vs-popup>
  </div>
</template>



<style scoped>
.scroller{
  overflow-x: hidden;
  height: calc(100% - 100px);
}
h3{
  color: white;
  position: relative;
  text-align: left;
  margin-bottom: 30px;
  left: 10px;
  margin-top: 5%;
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