<script>
import 'vue3-carousel/dist/carousel.css';
import { Carousel, Slide, Pagination, Navigation } from 'vue3-carousel';
import router from "@/router";
export default {
  name: "index",
  components: {
    Carousel,
    Slide,
    Pagination,
    Navigation,
  },
  data(){
    return {
      gamesRecomended:[],
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
      console.log(this.gamesRecomended.gamesTag1)
    },
    imgsInCarousel(id){
      return  require("../../../../src/main/resources/public/games/" + id.toString() + "/main.png")
    }
  },
  beforeMount() {
    this.recomendedGames()
  }
}


</script>

<template>
  <div class="scroller">
    <carousel :items-to-show="1" wrapAround="true">
      <slide v-for="slide in 10" :key="slide">
        <div class="carousel__item">{{ slide }}</div>
      </slide>

      <template #addons>
        <navigation />
        <pagination />
      </template>
    </carousel>
    <div style="margin-bottom: 50px">
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[0]}}</h1>
      <Carousel :items-to-show="3.3" wrapAround="false" :mouseDrag="false">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag1" :key="slide + 1" style="border-radius: 8px">
          <div @click="page('/gamePage/' + game.id.toString())" class="carousel__item1">
            <img :src="imgsInCarousel(game.id)" style="width: 100%; height: fit-content">
          </div>
          <div class="buttons">
            <vs-button color="danger" type="border" icon="favorite" ></vs-button>
            <vs-button color="primary" type="border" icon="add" ></vs-button>
          </div>
        </Slide>
        <template #addons>
          <Navigation />
        </template>
      </Carousel>
    </div>
    <div style="margin-bottom: 50px">
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[1]}}</h1>
      <Carousel :items-to-show="3.3" wrapAround="false" :mouseDrag="false">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag2" :key="slide + 1" style="border-radius: 8px">
          <div @click="page('/gamePage/' + game.id.toString())" class="carousel__item1">
            <img :src="imgsInCarousel(game.id)" style="width: 100%; height: fit-content">
          </div>
          <div class="buttons">
            <vs-button color="danger" type="border" icon="favorite" ></vs-button>
            <vs-button color="primary" type="border" icon="add" ></vs-button>
          </div>
        </Slide>
        <template #addons>
          <Navigation />
        </template>
      </Carousel>
    </div>
    <div style="margin-bottom: 50px">
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[2]}}</h1>
      <Carousel :items-to-show="3.3" wrapAround="false" :mouseDrag="false">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag3" :key="slide + 1" style="border-radius: 8px">
          <div @click="page('/gamePage/' + game.id.toString())" class="carousel__item1">
            <img :src="imgsInCarousel(game.id)" style="width: 100%; height: fit-content">
          </div>
          <div class="buttons">
            <vs-button color="danger" type="border" icon="favorite" ></vs-button>
            <vs-button color="primary" type="border" icon="add" ></vs-button>
          </div>
        </Slide>
        <template #addons>
          <Navigation />
        </template>
      </Carousel>
    </div>
    <div style="margin-bottom: 50px">
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[3]}}</h1>
      <Carousel :items-to-show="3.3" wrapAround="false" :mouseDrag="false">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag4" :key="slide + 1" style="border-radius: 8px">
          <div @click="page('/gamePage/' + game.id.toString())" class="carousel__item1">
            <img :src="imgsInCarousel(game.id)" style="width: 100%; height: fit-content">
          </div>
          <div class="buttons">
            <vs-button color="danger" type="border" icon="favorite" ></vs-button>
            <vs-button color="primary" type="border" icon="add" ></vs-button>
          </div>
        </Slide>
        <template #addons>
          <Navigation />
        </template>
      </Carousel>
    </div>
    <div style="margin-bottom: 50px">
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[4]}}</h1>
      <Carousel :items-to-show="3.3" wrapAround="false" :mouseDrag="false">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag5" :key="slide + 1" style="border-radius: 8px">
          <div @click="page('/gamePage/' + game.id.toString())" class="carousel__item1">
            <img :src="imgsInCarousel(game.id)" style="width: 100%; height: fit-content">
          </div>
          <div class="buttons">
            <vs-button color="danger" type="border" icon="favorite" ></vs-button>
            <vs-button color="primary" type="border" icon="add" ></vs-button>
          </div>
        </Slide>
        <template #addons>
          <Navigation />
        </template>
      </Carousel>
    </div>
    <h2></h2>
  </div>
  <router-view/>
</template>



<style>
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

</style>