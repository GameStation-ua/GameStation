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
      imgs:{
        img1:[],
        img2:[],
        img3:[],
        img4:[],
        img5:[],
      },
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
      localStorage.setItem('userData', JSON.stringify(this.gamesRecomended.user))
      for (let i = 0; i < this.gamesRecomended.gamesTag1.length; i++) {
        this.imgs.img1.push(this.getImg(this.gamesRecomended.gamesTag1[i].id))
      }for (let i = 0; i < this.gamesRecomended.gamesTag2.length; i++) {
        this.imgs.img2.push(this.getImg(this.gamesRecomended.gamesTag2[i].id))
      }for (let i = 0; i < this.gamesRecomended.gamesTag3.length; i++) {
        this.imgs.img3.push(this.getImg(this.gamesRecomended.gamesTag3[i].id))
      }for (let i = 0; i < this.gamesRecomended.gamesTag4.length; i++) {
        this.imgs.img4.push(this.getImg(this.gamesRecomended.gamesTag4[i].id))
      }for (let i = 0; i < this.gamesRecomended.gamesTag5.length; i++) {
        this.imgs.img5.push(this.getImg(this.gamesRecomended.gamesTag5[i].id))
      }console.log(this.imgs)
    },
    gamepage(game){
      localStorage.setItem("id", game.id)
      this.page('/gamePage/' + game.id.toString())
    },
    getImg(id){
      const res = new XMLHttpRequest()
      res.open("GET", "/image", false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("path", "/games/" + id.toString() + "/main.png")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.responseText)
      return 'data:image.png;base64,' + res.response.toString()
    },
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
            <img :src="imgs.img1[slide]" loading="lazy" style="width: 100%" alt="logo">
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
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[1].toUpperCase()}}</h1>
      <Carousel :items-to-show="3.5" wrapAround="false" :mouseDrag="false" snapAlign="left">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag2" :key="slide + 1" style="border-radius: 8px">
          <div @click="gamepage(game)" class="carousel__item1">
            <img :src="imgs.img2[slide]" loading="lazy" style="width: 100%" alt="logo">
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
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[2].toUpperCase()}}</h1>
      <Carousel :items-to-show="3.5" wrapAround="false" :mouseDrag="false" snapAlign="left">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag3" :key="slide + 1" style="border-radius: 8px">
          <div @click="gamepage(game)" class="carousel__item1" >
            <img :src="imgs.img3[slide]" loading="lazy" style="width: 100%" alt="logo">
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
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[3].toUpperCase()}}</h1>
      <Carousel :items-to-show="3.5" wrapAround="false" :mouseDrag="false" snapAlign="left">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag4" :key="slide + 1" style="border-radius: 8px">
          <div @click="gamepage(game)" class="carousel__item1" >
            <img :src="imgs.img4[slide]" loading="lazy" style="width: 100%" alt="logo">
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
      <h1 style="text-align: left; left: 20px; position: relative">{{gamesRecomended.userLikedTags[4].toUpperCase()}}</h1>
      <Carousel :items-to-show="3.5" wrapAround="false" :mouseDrag="false" snapAlign="left">
        <Slide v-for="(game, slide) in gamesRecomended.gamesTag5" :key="slide + 1" style="border-radius: 8px">
          <div @click="gamepage(game)" class="carousel__item1" alt="logo">
            <img async :src="imgs.img5[slide]" style="width: 100%" alt="logo">
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