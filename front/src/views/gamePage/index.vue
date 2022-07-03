<script>
import { Carousel, Slide, Navigation } from 'vue3-carousel';

export default {
  name: "index.vue",
  data(){
    return {
      img: "",
      imgSelect: "",
      infoUrl: "/game/info/" + this.$route.params.id.toString(),
      gameInfo: {},
      imgs:[],
      textarea: '',
      counterDanger: false,
      page:1,
      comments: []
    }
  },
  components: {
    Carousel,
    Slide,
    Navigation,
  },
  methods:{
    getGameInfo(){
      console.log(this.infoUrl)
      this.getImg()
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
        this.imgs.push("http://localhost:8443/image/games/" + this.$route.params.id.toString() + "/carousel=" + (i+1).toString() + ".png")
      }
      console.log(this.imgs)
    },
    mainImg(selected){
        this.imgSelect=selected
    },
    getImg(){
      this.img = 'http://localhost:8443/image/games/' + this.$route.params.id.toString() + '/main.png'
      this.imgSelect = 'http://localhost:8443/image/games/' + this.$route.params.id.toString() + '/main.png'
      this.imgs.push('http://localhost:8443/image/games/' + this.$route.params.id.toString() + '/main.png')
    },
    getcomments(){
      const res = new XMLHttpRequest()
      res.open("GET", '/comment/commentPage/' + this.$route.params.id.toString() + '/' + this.page.toString() , false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.responseText)
      this.comments = JSON.parse(res.response).comments
      console.log(this.comments)
    },
    comment(){
      const data ={
        content: this.textarea,
        path: '/gamePage/' + this.$route.params.id.toString()
      }
      const res = new XMLHttpRequest()
      res.open("POST", "/comment/game/" + this.$route.params.id.toString(), false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("path", "/games/" + this.$route.params.id.toString() + "/main.png")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(data))
      if (res.status === 200){
        location.reload()
      }

    }
  },
  beforeMount() {
    this.getGameInfo()
    this.getcomments()
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
          <h2 style="text-align: left; color: white">Description</h2>
          <p>
            {{gameInfo.description}}
          </p>
          <vs-divider style="background: white"/>
          <h2 style="text-align: left; color: white">Publisher</h2>
          <div class="publisher">
            <vs-avatar size="large" :src="'http://localhost:8443/image/profile_pictures/' + this.gameInfo.creators.id.toString() + '.png'"/>
            <div style="display: block; position: relative; left: 10px">
              <h1 style="position: relative; top: 5px">{{ gameInfo.creators.nickname }}</h1>
            </div>
            <vs-button color="danger" type="border" icon="favorite" style="position: absolute; right: 10px; top: 10px;"></vs-button>
          </div>
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
              <a v-for="(tag, index) in gameInfo.tags" :key="index" v-bind:href=" '/search/tag/' + tag ">{{tag}}</a>
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
      <h1 style="display: flex; position: relative; left: -5px">Comments</h1>
      <div style="width: 60%">
        <vs-textarea counter="300" :counter-danger.sync="counterDanger" v-model="textarea" width="100%" height="200px"/>
        <div style="display: flex; position: relative; right: 10px; margin-left: 10px">
          <vs-button @click="comment()" color="success" type="filled">Comment</vs-button>
        </div>
        <div v-for="(comment, index) in comments" :key="index" style="margin-top: 40px;display: flex; flex-wrap: wrap">
          <vs-avatar size="large" :src="'http://localhost:8443/image/profile_pictures/' + comment.userId.toString() + '.png'"/>
          <div style="display: block; width: 90%">
            <div style="display: flex; margin-bottom: -15px">
              <p style=" text-align: left;color: white; display: flex; position: relative; margin-right: 10px">{{ comment.nickname}}</p>
              <p style=" text-align: left;color: #2c3e50; display: flex; position: relative">{{comment.date.toString()}}</p>
            </div>
            <p style="overflow-wrap: break-word">{{ comment.content}}</p>
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

.publisher{
  background: rgba(0, 0, 0, 0.8);
  color: #000000;
  position: relative;
  left: 10px;
  display: flex;
  height: 60px;
  margin-right: 10px;
  margin-bottom: 10px;
}
.vs-textarea:focus{
  resize: none!important;
}
</style>