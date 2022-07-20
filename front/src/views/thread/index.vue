<script>
export default {
  name: "index",
  data(){
    return{
      thread:{},
      textarea: "",
      page: 1,
      comments: []
    }
  },
  methods:{
    getThreadData(){
      const res = new XMLHttpRequest()
      res.open("GET", "/forum/thread/" + this.$route.params.id.toString(), false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.response)
      this.thread = JSON.parse(res.response)
      console.log(this.thread)
    },
    getComments(){
      const res = new XMLHttpRequest()
      res.open("GET", "/comment/commentPage/" + this.$route.params.id.toString() + '/' + this.page.toString(), false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.response)
      this.comments = JSON.parse(res.response).comments
      console.log(this.comments)
    },
    comment() {
      const data = {
        content: this.textarea,
        path: '/forum/thread/' + this.$route.params.id.toString()
      }
      const res = new XMLHttpRequest()
      res.open("POST", "/comment/thread/" + this.$route.params.id.toString(), false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(JSON.stringify(data))
     if (res.status === 200) {
        location.reload()
      }
    },
    upvote(comment){
      const res = new XMLHttpRequest()
      res.open("POST", '/comment/upvote/' + comment.id.toString()  ,false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      if (res.status === 200){
        if (comment.userVote === -1){
          comment.votes += 2
          comment.userVote = 1
        }else{
          comment.votes += 1
          comment.userVote = 1
        }

      }
    },
    downvote(comment){
      const res = new XMLHttpRequest()
      res.open("POST", '/comment/downvote/' + comment.id.toString()  ,false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      if (res.status === 200){
        if (comment.userVote === 1){
          comment.votes -= 2
          comment.userVote = -1
        }else{
          comment.votes -= 1
          comment.userVote = -1
        }
      }
    },
    neutralVote(comment){
      const res = new XMLHttpRequest()
      res.open("POST", '/comment/neutralvote/' + comment.id.toString()  ,false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      if (res.status === 200){
        if(comment.userVote === 1){
          comment.votes -= 1
        }
        if (comment.userVote === -1){
          comment.votes += 1
        }
        comment.userVote = 0
      }
    },
    next(){
      this.page +=1
      this.getComments()
    },
    back(){
      this.page -=1
      this.getComments()
    }
  },
  beforeMount() {
    this.getThreadData()
    this.getComments()
  }
}
</script>

<template>
  <div class="scroller">
    <div class="form-box" style="position: relative">
      <div style="position: absolute; left: 10px; top: 10px; display: flex">
        <vs-avatar size="large" :src="'http://localhost:8443/image/profile_pictures/' + thread.user.id.toString() + '.png'"/>
        <div style="margin-top: 15px; display: flex">
          <h3 style="margin-right: 10px">{{thread.user.nickname}}</h3>
          <p style="margin-top: 9px">{{thread.date}}</p>
        </div>
      </div>
      <div style="margin-top: 30px">
        <h1 style="text-align: left; margin-left: 10px">{{thread.title}}</h1>
        <p style="margin-left: 10px; font-size: 20px; white-space: initial;">{{thread.description}}</p>
      </div>
      <vs-divider color="#ffffff"/>
      <label style="text-align: center; margin-left: 10px; display: flex; position: relative">Add a comment</label>
      <vs-textarea counter="300" v-model="textarea"/>
      <vs-button @click="comment" color="success" type="border">Comment</vs-button>
      <div v-for="(comment, index) in comments" :key="index" style="margin-top: 40px;display: flex; flex-wrap: wrap">
          <vs-avatar size="large" :src="'http://localhost:8443/image/profile_pictures/' + comment.userId.toString() + '.png'"/>
          <div style="display: block; width: 90%">
            <div style="display: flex; margin-bottom: -15px">
              <p style=" text-align: left;color: white; display: flex; position: relative; margin-right: 10px">{{ comment.nickname}}</p>
              <p style=" text-align: left;color: #515b7f; display: flex; position: relative">{{comment.date.toString()}}</p>
            </div>
            <p style="overflow-wrap: break-word">{{ comment.content}}</p>
            <div style="display:flex">
              <div v-if="comment.userVote === 1">
                <vs-button @click="neutralVote(comment)" color="rgb(255, 255, 255)" type="flat" icon="thumb_up" style="margin-right: 10px; position: relative"></vs-button>
              </div>
              <div v-else>
                <vs-button @click="upvote(comment)" color="rgb(86, 86, 86)" type="flat" icon="thumb_up" style="margin-right: 10px; position: relative"></vs-button>
              </div>
              <label style="margin-right: 10px; position: relative">{{comment.votes}}</label>
              <div v-if="comment.userVote === -1">
                <vs-button @click="neutralVote(comment)" color="rgb(255, 255, 255)" type="flat" icon="thumb_down" style="margin-right: 10px; position: relative"></vs-button>
              </div>
              <div v-else>
                <vs-button @click="downvote(comment)" color="rgb(86, 86, 86)" type="flat" icon="thumb_down" style="margin-right: 10px; position: relative"></vs-button>
              </div>
            </div>
          </div>
        </div>
      <div style="display: flex; position: relative">
        <div v-if="page !== 1">
          <vs-button @click="back()" color="primary" type="border">Back</vs-button>
        </div>
        <div v-else></div>
        <div v-if="comments.length === 10">
          <vs-button @click="next()" color="primary" type="border">Next</vs-button>
        </div>
        <div v-else></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.scroller {
  overflow-x: hidden;
  height: calc(100% - 100px);
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
.vs-textarea{
  color: white!important;
}
</style>