<script>
import router from "@/router";
export default {
  name: "editGameMenu.vue",
  data(){
    return {
      popupActivo: false,
      availableTags1: [],
      infoUrl: "/game/info/" + this.$route.params.id.toString(),
      gamedata:{
        title: "",
        description: "",
        tags: [],
        wiki: "",
        imgsInCarousel: 0
      }
    }
  },
  methods: {
    getgameinfo(){
      const res = new XMLHttpRequest()
      res.open("GET", this.infoUrl, false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.responseText)
      this.gamedata.title = JSON.parse(res.response).title
      this.gamedata.description = JSON.parse(res.response).description
      this.gamedata.tags = JSON.parse(res.response).tags
      this.gamedata.wiki = JSON.parse(res.response).wiki
      this.gamedata.imgsInCarousel = JSON.parse(res.response).imgsInCarousel
    },
    getTags() {
      const res = new XMLHttpRequest()
      res.open("GET", "/tags/available_tags", false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.responseText)
      this.availableTags1 = JSON.parse(res.response).availableTags
      console.log(this.tags)
    },
    delAll(){
      this.gamedata.tags = []
    },
    editGame(){
      var xhr = new XMLHttpRequest()
      var json = JSON.stringify(this.gamedata)
      xhr.open("POST", "/game/edit/" + this.$route.params.id.toString(), false)
      xhr.setRequestHeader("Content-Type", "application/json")
      xhr.setRequestHeader("token", localStorage.getItem("token"))
      xhr.send(json)
      console.log(json)
      if (xhr.status === 200){
        router.push('/')
      }
    }
  },
  beforeMount() {
    this.getgameinfo()
    this.getTags()
  }
}
</script>

<template>

  <div>
    <div class="scroller">
      <H1>UpLoad Menu</H1>
      <div class="form-box">
        <div class="centerxmenu">
          <vs-input label="Name" placeholder="" v-model="gamedata.title"/>
          <vs-input label="Wiki" placeholder="Url" v-model="gamedata.wiki"/>
          <div class="tags">
            <div>
              <label>Tags</label>
            </div>
            <vs-button @click="popupActivo=true" color="primary" type="border">See Tags</vs-button>
            <vs-popup class="holamundo"  title="Select the tags of your game" :active.sync="popupActivo">
              <div class="box">
                <ul class="center">
                  <li v-for="(tag,index) in availableTags1" :key="index">
                    <vs-checkbox v-model="gamedata.tags" :vs-value="tag">{{tag}}</vs-checkbox>
                  </li>
                </ul>
              </div>
              <vs-button @click="popupActivo=false" color="success" type="filled">Confirm</vs-button>
              <vs-button @click="delAll" color="danger" type="filled">Delete all</vs-button>
            </vs-popup>
          </div>
          <label id="description">Description</label>
          <vs-textarea  counter="1024" v-model="gamedata.description" width="70%" height="300px"/>
          <div class="next">
            <vs-button @click="editGame" color="success" type="filled" icon="arrow_forward_ios">Next</vs-button>
          </div>
        </div>
      </div>
    </div>
  </div>


</template>

<style>

.centerxmenu{
  position: relative;
  text-align: left;
  left: 10px;
}
.centerxmenu .vs-input{
  width: 40%;

}

.vs-input--label{
  color: white;
}

.centerxmenu .vs-button{
  position: relative;
  left: 0;
}

.vs-textarea{
  color: white !important;
  background: #242222;
  border: 1px solid rgb(255 255 255 / 8%) !important;
}

ul{
  display: flex;
  flex-wrap: wrap
}

li{
  display: flex;
}

label{
  color: white !important;
  margin-top: 2%;
}

.tags{
  position: relative;

  left: 4px;
}

.next{
  position: absolute !important;
  right: 20px;

}

.form-box {
  width: 50%;
  background: rgba(0, 0, 0, 0.8);
  padding: 50px 0;
  color: #fff;
  box-shadow: 0 0 20px 2px rgba(0, 0, 0, 0.5);
  margin: 30px auto;
  height: fit-content;
}
.scroller {
  overflow-x: hidden;
  height: calc(100% - 100px);
}



</style>