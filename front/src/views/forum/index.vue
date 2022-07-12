<script>
import router from "@/router";
export default {
  name: "index",
  data(){
    return{
      page: 1,
      threads:[],
      threadCreate:{
        gameId: this.$route.params.id.toString(),
        description: "",
        title: ""
      },
      popupActivo:false,
      counterDanger: false
    }
  },
  methods:{

    getThread(){
      const res = new XMLHttpRequest()
      res.open("GET", "/forum/threadPage/" + this.$route.params.id.toString() + "/" + this.page.toString(), false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.response)
      this.threads = JSON.parse(res.response)
    },
    createTread(){
      const res = new XMLHttpRequest()
      res.open("POST", "/forum/thread/create", false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      const json = JSON.stringify(this.threadCreate)
      console.log(json)
      res.send(json)
      if(res.status === 200){
        this.popupActivo = false
        this.threadCreate.description = ""
        this.threadCreate.title = ""
      }
    },
    followThread(user){
      const data = {
        path: "/profile/" + JSON.parse(localStorage.getItem('userData')).id.toString()
      }
      const res = new XMLHttpRequest()
      res.open("PATCH", "/follow/add/" + user.threadId.toString() , false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(data))
      if (res.status === 200){
        user.isFollowing = true
      }
    },

    unfollowThread(user){
      const data = {
        path: "/profile/" + JSON.parse(localStorage.getItem('userData')).id.toString()
      }
      const res = new XMLHttpRequest()
      res.open("PATCH", "/follow/delete/" + user.threadId.toString() , false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(data))
      if (res.status === 200){
        user.isFollowing = false
      }
    },
    gotoThread(thread){
      router.push('/forum/thread/' + thread.threadId.toString())
    },
  },
  beforeMount() {
    this.getThread()
  }
}
</script>

<template>
  <div class="scroller">
    <div style="width: 60%; position: relative; margin: auto; display: flex">
      <h1 style="text-align: left; margin-top: 30px">Forum</h1>
      <vs-button @click="popupActivo=true" color="primary" type="border" style="position: absolute; right: 10px; top: 30px">Add Thread</vs-button>
    </div>
    <div v-for="(thread, index) in threads" :key="index" style="width: 60%; position: relative; margin: auto">
      <div class="selection" style="display: flex; cursor: pointer" @click="gotoThread(thread)">
        <div>
          <vs-avatar size="70px" :src="'http://localhost:8443/image/profile_pictures/' + thread.user.id.toString() + '.png'"/>
        </div>
        <div style="display: block">
          <div style="display: flex">
            <p style="margin-right: 10px">{{thread.user.nickname}}</p>
            <p style="color: #515b7f">{{thread.date}}</p>
          </div>
          <h2 style="color: white; text-align: left">{{thread.title}}</h2>
        </div>
      </div>
      <div style="position: absolute; right: 10px; top: 10px">
        <vs-button v-if="thread.isFollowing === true" color="danger" type="border" icon="heart_broken" @click="unfollowThread(thread)" ></vs-button>
        <vs-button v-else color="danger" type="border" icon="favorite" @click="followThread(thread)"></vs-button>
      </div>

    </div>
    <vs-popup class="holamundo"  title="New Thread" button-close-hidden="true"	 :active.sync="popupActivo">
      <vs-input label-placeholder="Thread Title" v-model="threadCreate.title" style="margin-bottom: 10px"/>
      <vs-textarea class="algo" counter="500" label="Description" :counter-danger.sync="counterDanger" v-model="threadCreate.description" style="color: black"/>
      <vs-button @click="createTread()" color="success" type="border">Success</vs-button>
      <vs-button @click="popupActivo=false" color="dark" type="border">Cancel</vs-button>
    </vs-popup>
  </div>
</template>

<style>
.scroller {
  overflow-x: hidden;
  height: calc(100% - 100px);
}

h4{
  color: black!important;
}
.algo .vs-textarea{
  color: black !important;
}
.selection{
  background: rgba(0, 0, 0, 0.8);
  color: #000000;
  position: relative;
  left: 10px;
  display: block;
  height: auto;
  margin-right: 10px;
  margin-bottom: 10px;
}
</style>