<script>
import 'vue3-carousel/dist/carousel.css';
import { Carousel, Slide, Navigation } from 'vue3-carousel';
import router from "@/router";
export default {
  name: "index",
  components: {
    Carousel,
    Slide,
    Navigation,
  },
  data(){
    return {
      gamesRecomended:[],
      listdata:{
        gameId: '',
        score: 0,
        status: ''
      },
      selectedGame:{},
      popupActivo:false,
      options1:['Playing', 'Dropped', 'On hold', 'Waiting', 'Completed'],
    }
  }
  ,methods:{
     page(rout){
      router.push(rout)
    },
    recomendedGames(){
      const res = new XMLHttpRequest()
      res.open("GET", "/home", false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.responseText)
      this.gamesRecomended = JSON.parse(res.response)
    },
    gamepage(game){
      localStorage.setItem("id", game.id)
      this.page('/gamePage/' + game.gameId.toString())
    },

    addToList(){
      const res = new XMLHttpRequest()
      res.open("PATCH", "/gamelist/add", false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(this.listdata))
      this.popupActivo = false

    },
    openPopup(game){
      this.selectedGame = game
      this.popupActivo = true
      this.listdata.gameId = game.gameId
      console.log(this.selectedGame)
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
    }
  },
  beforeMount() {
    this.recomendedGames()
    localStorage.setItem("inSearch", "false")
  }
}
</script>

<template>
  <div class="scroller">
    <div style="margin-bottom: 50px">
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[0].toUpperCase()}}</h1>
      <Carousel :items-to-show="3.5" wrapAround="false" :mouseDrag="false" snapAlign="left">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag1" :key="slide" style="border-radius: 8px">
          <div @click="gamepage(game)" class="carousel__item1">
            <img :src="'http://localhost:8443/image/games/' + game.gameId.toString() + '/main.png'" loading="lazy" style="width: 100%" alt="logo">
          </div>
          <div class="buttons">
            <vs-button v-if="game.isFollowing === true" color="danger" type="border" icon="heart_broken" @click="unfollowGame(game)"></vs-button>
            <vs-button v-else color="danger" type="border" icon="favorite" @click="followGame(game)"></vs-button>
            <vs-button color="primary" type="border" icon="add" @click="openPopup(game)"></vs-button>
          </div>
        </Slide>
        <template #addons>
          <Navigation />
        </template>
      </Carousel>
    </div>
    <div style="margin-bottom: 50px">
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[1].toUpperCase()}}</h1>
      <Carousel :items-to-show="3.5" wrapAround="false" :mouseDrag="false" snapAlign="left">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag2" :key="slide + 1" style="border-radius: 8px">
          <div @click="gamepage(game)" class="carousel__item1">
            <img :src="'http://localhost:8443/image/games/' + game.gameId.toString() + '/main.png'" loading="lazy" style="width: 100%" alt="logo">
          </div>
          <div class="buttons">
            <vs-button v-if="game.isFollowing === true" color="danger" type="border" icon="heart_broken" @click="unfollowGame(game)"></vs-button>
            <vs-button v-else color="danger" type="border" icon="favorite" @click="followGame(game)"></vs-button>
            <vs-button color="primary" type="border" icon="add" @click="openPopup(game)"></vs-button>
          </div>
        </Slide>
        <template #addons>
          <Navigation />
        </template>
      </Carousel>
    </div>
    <div style="margin-bottom: 50px">
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[2].toUpperCase()}}</h1>
      <Carousel :items-to-show="3.5" wrapAround="false" :mouseDrag="false" snapAlign="left">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag3" :key="slide + 1" style="border-radius: 8px">
          <div @click="gamepage(game)" class="carousel__item1" >
            <img :src="'http://localhost:8443/image/games/' + game.gameId.toString() + '/main.png'" loading="lazy" style="width: 100%" alt="logo">
          </div>
          <div class="buttons">
            <vs-button v-if="game.isFollowing === true" color="danger" type="border" icon="heart_broken" @click="unfollowGame(game)"></vs-button>
            <vs-button v-else color="danger" type="border" icon="favorite" @click="followGame(game)"></vs-button>
            <vs-button color="primary" type="border" icon="add" @click="openPopup(game)"></vs-button>
          </div>
        </Slide>
        <template #addons>
          <Navigation />
        </template>
      </Carousel>
    </div>
    <div style="margin-bottom: 50px">
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[3].toUpperCase()}}</h1>
      <Carousel :items-to-show="3.5" wrapAround="false" :mouseDrag="false" snapAlign="left">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag4" :key="slide + 1" style="border-radius: 8px">
          <div @click="gamepage(game)" class="carousel__item1" >
            <img :src="'http://localhost:8443/image/games/' + game.gameId.toString() + '/main.png'" loading="lazy" style="width: 100%" alt="logo">
          </div>
          <div class="buttons">
            <vs-button v-if="game.isFollowing === true" color="danger" type="border" icon="heart_broken" @click="unfollowGame(game)"></vs-button>
            <vs-button v-else color="danger" type="border" icon="favorite" @click="followGame(game)"></vs-button>
            <vs-button color="primary" type="border" icon="add" @click="openPopup(game)"></vs-button>
          </div>
        </Slide>
        <template #addons>
          <Navigation />
        </template>
      </Carousel>
    </div>
    <div style="margin-bottom: 50px">
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[4].toUpperCase()}}</h1>
      <Carousel :items-to-show="3.5" wrapAround="false" :mouseDrag="false" snapAlign="left">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag5" :key="slide + 1" style="border-radius: 8px">
          <div @click="gamepage(game)" class="carousel__item1" alt="logo">
            <img :src="'http://localhost:8443/image/games/' + game.gameId.toString() + '/main.png'" style="width: 100%" alt="logo">
          </div>
          <div class="buttons">
            <vs-button v-if="game.isFollowing === true" color="danger" type="border" icon="heart_broken" @click="unfollowGame(game)"></vs-button>
            <vs-button v-else color="danger" type="border" icon="favorite" @click="followGame(game)"></vs-button>
            <vs-button color="primary" type="border" icon="add" @click="openPopup(game)"></vs-button>
          </div>
        </Slide>
        <template #addons>
          <Navigation />
        </template>
      </Carousel>
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
  <router-view/>
</template>



<style lang="css">
.carousel__item {
  min-height: 500px;
  width: 100%;
  background-color: #1c6f42 !important;
  color:  var(--vc-clr-white);
  font-size: 20px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  left: 0 !important;
}
.carousel__item1 {
  font-size: 20px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  position: relative;
}
.scroller {
  overflow-x: hidden;
  height: calc(100% - 100px);
}
.carousel__slide {
  padding: 10px;
}
.carousel__prev{
  box-sizing: content-box;
  color: #ffffff;
  background-color: black;
  left: 1.3%;
}
.carousel__next {
  box-sizing: content-box;
  color: #ffffff;
  background-color: black;
  right: 1.3%;
}
.carousel__next--in-active {
  display: none;
}
.buttons{
  position: absolute;
  bottom: 15px;
  right: 15px;
}
.buttons .vs-button{
  margin-right: 3px;
}
input.input-select.vs-select--input{
  background: white;
}
li{
  margin-right: 10px;
}
.vs-radio--label{
  color: black !important;
}
</style>