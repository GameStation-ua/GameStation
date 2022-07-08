<script>
import router from "@/router";
export default {
  name: "index",
  data(){
    return {
      searchresultGames:[],
      searchresultusers:[],
      searchinput: this.$route.params.id.toString()
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
    followUser(user){
      const data = {
        path: "/profile/" + JSON.parse(localStorage.getItem('userData')).id.toString()
      }
      const res = new XMLHttpRequest()
      res.open("PATCH", "/follow/add/" + user.id.toString() , false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(data))
    }
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
          <vs-button color="danger" type="border" icon="favorite" ></vs-button>
          <vs-button color="primary" type="border" icon="add" ></vs-button>
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
          <vs-button color="danger" type="border" icon="favorite" @click="followUser(userSearch)"></vs-button>
        </div>
      </div>
    </div>
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