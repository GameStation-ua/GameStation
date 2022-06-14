<script>
import { Carousel, Slide, Navigation } from 'vue3-carousel';

export default {
  name: "index.vue",
  data(){
    return {
      img: require("../../../../src/main/resources/public/games/" + localStorage.getItem("id").toString() + "/main.png"),
      imgSelect: require("../../../../src/main/resources/public/games/" + localStorage.getItem("id").toString() + "/main.png"),
      infoUrl: "/game/info/" + localStorage.getItem("id"),
      gameInfo: {},
      imgs:[require("../../../../src/main/resources/public/games/" + localStorage.getItem("id").toString() + "/main.png")]
    }
  },
  components: {
    Carousel,
    Slide,
    Navigation,
  },
  methods:{
    getGameInfo(){
      const res = new XMLHttpRequest()
      res.open("GET", this.infoUrl, false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.responseText)
      this.gameInfo = JSON.parse(res.response)
      console.log(this.gamesInfo)
      console.log(this.gameInfo.gameId)
      for (let i = 0; i < this.gameInfo.imgsInCarousel; i++) {
        this.imgs.push(require("../../../../src/main/resources/public/games/" + localStorage.getItem("id").toString() + "/carousel=" + (i+1).toString() + ".png"))
      }
      console.log(this.imgs)
    },
    mainImg(selected){
        this.imgSelect=selected
    },
  },
  beforeMount() {
    this.getGameInfo()
  }
}
</script>

<template>
  <div class="scrollerGamePage">
    <div class="content">
      <div class="gameInfo">
        <div class="images">
          <div class="image">
            <img id="main" :src="imgSelect">
          </div>
          <Carousel :items-to-show="3" wrapAround="true" :mouseDrag="false" snapAlign="left">
            <Slide v-for="slide in imgs" :key="slide">
              <img :src="slide" style="width: 100%; cursor: pointer" @click="mainImg(slide)">
            </Slide>
            <template #addons>
              <Navigation />
            </template>
          </Carousel>
          <h2>Description</h2>
          <p>
            {{gameInfo.description}}
          </p>
        </div>
        <div class="right">
          <div class="rightContent" style="position: sticky !important;">
            <img id="rightimg" :src="img">
            <vs-button color="success" type="border">Add to list</vs-button>
            <vs-button color="danger" type="border">Follow game</vs-button>
            <div class="otherButtons">
              <vs-button color="primary" type="border">Forum</vs-button>
              <vs-button color="warning" type="border" target :href="gameInfo.wiki">Wiki</vs-button>
            </div>
            <div style="display: flex; margin-top: 10px">
              <label style="margin-left: 10%">Tags</label>
            </div>
            <div style="display: flex; margin-left: 10%">
              <a v-for="(tag, index) in gameInfo.tags" :key="index" href="">{{tag}}</a>
            </div>
            <vs-divider style="background: white; width: 80%; left: 10%"/>
            <div style="display: flex">
              <label style="margin-left: 10%; margin-top: -5px">Average</label>
            </div>
            <div style="width: 80%; margin-left: 10%; display: flex">
              <vs-progress :percent="gameInfo.meanScore" color="success" style="right: 0; width: 92%; margin-right: 5px">success</vs-progress>
              <label style="margin-top: -10px; margin-bottom: -40px">{{ gameInfo.meanScore }}%</label>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<style>

.images{
  width: 60%;
}

.rightContent{
  position: sticky;
  top: 10px;
}

.otherButtons .vs-button{
  width: 40% !important;
}

.gameInfo{
  display: flex;
}

.right{
  width: 40%;
}

.rightContent .vs-button{
  width: 80%;
  margin: auto;
  margin-top: 10px;
}

#main{
  width: 100%;
  height: 100%;
  object-fit: contain;
}

#rightimg{
  width: 80%;
}

.content{
  position: relative;
  top: 10px;
  width: 70%;
  margin: auto;
}

.scrollerGamePage{
  overflow-x: hidden;
  height: calc(100% - 100px);
}

.image{
  width: 100%;
  background-color: #1c6f42 !important;
  color:  var(--vc-clr-white);
  font-size: 20px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  position: relative;
}

.carousel__images{
  padding-top: 56.25%;
  width: 100%;
  background-color: #1c6f42 !important;
  color:  var(--vc-clr-white);
  font-size: 20px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  position: relative;
}

h1{
  color: white;
  left: 30%;
}

p{
  color: white;
  text-align: left;
}

a{
  margin-right: 10px;
}

</style>