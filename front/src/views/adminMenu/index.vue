<script>
import { notify } from "@kyvg/vue3-notification";
import { Carousel, Slide, Navigation } from 'vue3-carousel';
export default {
  name: "index",
  components: {
    Carousel,
    Slide,
    Navigation,
  },
  data(){
    return {
      gameRequests:[],
      popupActivo1:false,
      popupActivo2:false,
      popupActivo3:false,
      tag1: '',
      tagsTosend:{
        tags: []
      },
      tagsTodelete:{
        tags: []
      },
      selectedGame: {},
      img: ""

    }
  },
  methods:{
    getTags(){
      const res = new XMLHttpRequest()
      res.open("GET", "/tags/available_tags", false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.responseText)
      this.tagsget = JSON.parse(res.response).availableTags
      console.log(this.tags)
    },

    getGamesRequest(){
      const res = new XMLHttpRequest()
      res.open("GET", 'game/gamerequests', false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.responseText)
      this.gameRequests = JSON.parse(res.response)
      console.log(this.tags)
    },

    sendTags(method, send, URL){
      var res = new XMLHttpRequest()
      var json = JSON.stringify(send)
      res.open(method, URL, false)
      res.setRequestHeader("Accept", "application/json")
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      console.log(json)
      res.send(json)
    },

    addTag(){
      this.tagsTosend.tags.push(this.tag1)
      this.sendTags("PATCH", this.tagsTosend, "/tags/available_tags/add")
      this.getTags()
    },

    removeTag(){
      this.sendTags("DELETE", this.tagsTodelete, "/tags/available_tags/delete")
      this.getTags()
      this.tagsTodelete.tags = []
    },
    gameData(id){
      console.log(this.gameRequests)
      console.log(id)
      this.gameRequests.map((game)=>{
        if (game.id === id){
          this.selectedGame = game
          this.popupActivo3 = true
          console.log(this.selectedGame.title)
          this.img = 'http://localhost:8443/image/game_requests/' + id.toString() + '/main.png'
          return
        }
        return
      })
    },
    approval(result, id){
      const approval = {
        isApproved: result,
        gameRequestId: id
      }
      var xhr = new XMLHttpRequest()
      var json = JSON.stringify(approval)
      xhr.open("POST", "/game/solicitude/approval", false)
      xhr.setRequestHeader("Content-Type", "application/json")
      xhr.setRequestHeader("token", localStorage.getItem("token"))
      xhr.send(json)
      console.log(json)
      if (xhr.status === 200){
        notify({
          type: "success",
          title: JSON.parse(xhr.response()).message,
        });
      }
    }
  },
  beforeMount() {
    this.getTags()
    this.getGamesRequest()
  }
}
</script>

<template>
  <div>
    <h1>Admin Menu</h1>
    <div class="">
      <vs-tabs color="success">
        <vs-tab label="Tags">
          <div class="con-tab-ejemplo">
            <vs-button @click="popupActivo1=true" color="success" type="filled">Add Tag</vs-button>
            <vs-button @click="popupActivo2=true" color="danger" type="filled">Delate Tag</vs-button>
            <div class="box">
              <ul class="center" style="flex-wrap: wrap; display: flex">
                <li v-for="(tag,index) in tagsget" :key="index">
                  <vs-checkbox v-model="tagsTodelete.tags" :vs-value="tag">{{tag}}</vs-checkbox>
                </li>
              </ul>
            </div>
          </div>
        </vs-tab>
        <div class="centerx">
          <vs-popup class="tagpopup"  title="Add Tag" :active.sync="popupActivo1" button-close-hidden="true">
            <vs-input placeholder="Tag" color="success" v-model="tag1"/>
            <vs-button @click="addTag" color="success" type="filled">Add Tag</vs-button>
            <vs-button @click="popupActivo1=false" color="dark" type="flat">Cancel</vs-button>
          </vs-popup>
          <vs-popup class="tagpopup"  title="Delate Tag" :active.sync="popupActivo2" button-close-hidden="true">
            <p>
              Are you sure you want to delate the tags selected?
            </p>
            <vs-button @click="removeTag" color="danger" type="filled">Yes</vs-button>
            <vs-button @click="popupActivo2=false" color="dark" type="flat">Cancel</vs-button>
          </vs-popup>
        </div>
        <vs-tab label="Publish Game List" style="display: flex; flex-wrap: wrap" @click="getSrc">
          <div v-for="(gameRequest,index) in gameRequests" :key="index" class="selection" @click="gameData(gameRequest.id)" style="cursor: pointer ">
            <div class="Img" style="height: 100%">
              <img :src="'http://localhost:8443/image/game_requests/' + gameRequest.id.toString() + '/main.png'" style="height: 100%">
            </div>
            <div style="display: block; position: relative; left: 10px">
              <h1>{{ gameRequest.title }}</h1>
              <p>{{ gameRequest.description }}</p>
            </div>
          </div>
          <vs-popup class="Game Content"  title="Game Content" :active.sync="popupActivo3" fullscreen="true">
            <div style="display: flex">
              <div style="width: 35%">
                <h1 style="color: black">Name</h1>
                <h3 style="color: black!important;">{{selectedGame.title}}</h3>
                <h1 style="color: black">Tags</h1>
                <div>
                  <ul>
                    <li v-for="(tag, index) in selectedGame.tags" :key="index" style="color: black; margin-left: 10px; text-decoration: underline; display: flex; flex-wrap: wrap">{{tag}}</li>
                  </ul>
                </div>
                <h1 style="color: black">Wiki</h1>
                <vs-button color="warning" type="border" target :href="selectedGame.wiki">Wiki</vs-button>
                <h1 style="color: black">Description</h1>
                <p style="color: black">{{selectedGame.description}}</p>
              </div>
              <div style="width: 50%">
                <img :src="img" style="width: 100%">
                <Carousel :items-to-show="3" wrapAround="false" :mouseDrag="false" :touchDrag="false">
                  <Slide v-for="slide in selectedGame.imgsInCarousel" :key="slide">
                    <div class="carousel__admin">
                      <img :src="'http://localhost:8443/image/game_requests/' + selectedGame.id.toString() + '/carousel=' + slide.toString() + '.png'" style="width: 100%">
                    </div>
                  </Slide>
                  <template #addons>
                    <Navigation />
                  </template>
                </Carousel>
              </div>
            </div>
            <div style="position: absolute; bottom: 30px; left: 30px">
              <vs-button color="success" type="border" icon="done" style="margin-right: 10px" @click="approval(true, selectedGame.id)">accept</vs-button>
              <vs-button color="danger" type="border" icon="close" style="margin-right: 10px" @click="approval(false, selectedGame.id)">reject</vs-button>
              <vs-button color="dark" type="border" @click="popupActivo3=false">cancel</vs-button>
            </div>
          </vs-popup>
        </vs-tab>
        <vs-tab label="Edit Game List">
          <div class="con-tab-ejemplo">
            Edit
          </div>
        </vs-tab>
      </vs-tabs>
    </div>
  </div>
</template>

<style scoped>

h1{
  color: white;
  display: flex;
}

.vs-button{
  display: flex;
  margin-top: 10px;
  width: 100px;
}

.box{
  position: relative;
  width: calc(100% - 130px);
  left: 130px;
  top: -90px;
}

.tagpopup .vs-button{
  display: revert;
}

li{
  display: flex;
  position: relative;
  color: white;
}

.center{
  display: flex;
  justify-content: space-between;

}

.selection {
  width: 45%;
  background: rgba(0, 0, 0, 0.8);
  color: #000000;
  position: relative;
  left: 10px;
  display: flex;
  height: 150px;
  margin-right: 10px;
  margin-bottom: 10px;
}

.carousel__admin {
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

</style>