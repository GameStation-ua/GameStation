<script>
import router from "@/router";
export default {
  name: "searchTags",
  data(){
    return {
      searchresultGames:[],
      searchinput: this.$route.params.tag.toString()
    }
  },
  methods:{
    page(rout){
      router.push(rout)
    },
    getSearch(){
      const res = new XMLHttpRequest()
      res.open("GET", "/tags/search/" + this.searchinput, false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.response)
      this.searchresultGames = JSON.parse(res.response).gameList
      console.log(this.searchresult)
    },
    gamepage(id){
      this.page('/gamePage/' + id.toString())
    },
    followGame(game){
      const data = {
        path: "/profile/" + JSON.parse(localStorage.getItem('userData')).id.toString()
      }
      const res = new XMLHttpRequest()
      res.open("PATCH", "/follow/add/" + game.id.toString() , false)
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
      res.open("PATCH", "/follow/delete/" + game.id.toString() , false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(data))
      if (res.status === 200){
        game.isFollowing = false
      }
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
        <div @click="gamepage(gameSearch.id)" style="cursor: pointer" class="selection">
          <img :src="'http://localhost:8443/image/games/' + gameSearch.id.toString() + '/main.png'" style="height: 100%" alt="logo">
          <div style="display: block; position: relative; left: 10px">
            <h1>{{ gameSearch.title }}</h1>
          </div>
        </div>
        <div class="buttons">
          <vs-button v-if="gameSearch.isFollowing === true" color="danger" type="border" icon="heart_broken" @click="unfollowGame(gameSearch)"></vs-button>
          <vs-button v-else color="danger" type="border" icon="favorite" @click="followGame(gameSearch)"></vs-button>
          <vs-button color="primary" type="border" icon="add"></vs-button>
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