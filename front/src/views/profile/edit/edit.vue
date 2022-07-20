<script>
import router from "@/router";
export default {
  name: "edit",
  data() {
    return {
      popupActivo: false,
      availableTags1: [],
      user: JSON.parse(localStorage.getItem('userData')),
      addtags: {
        tags:[],
      },
      delltags:{
        tags:[],
      },
      headersProfile:{
        token: localStorage.getItem('token'),
        id: this.$route.params.id.toString(),
        imgType: 'profile'
      }
    }
  },
  methods:{
    page(rout){
      router.push(rout)
      localStorage.setItem("inSearch", "false")
    },
    save(){
      router.push("/profile/" + this.$route.params.id.toString())
      setTimeout(()=>{
        location.reload()
      },10);
    },
    getTags() {
      const res = new XMLHttpRequest()
      res.open("GET", "/tags/available_tags", false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      console.log(res.responseText)
      this.availableTags1 = JSON.parse(res.response).availableTags
      this.dellInCommon(this.availableTags1, this.user.likedTags)
      console.log(this.headersProfile)
    },
    dellTags(){
      var res = new XMLHttpRequest()
      var json = JSON.stringify(this.delltags)
      res.open("DELETE", '/tags/users/delete', false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      console.log(json)
      res.send(json)
      this.dellInCommon(this.user.likedTags, this.delltags.tags)
      localStorage.setItem('userData', JSON.stringify(this.user))
      location.reload()
    },
    addTags(){
      var res = new XMLHttpRequest()
      var json = JSON.stringify(this.addtags)
      res.open("PATCH", '/tags/users/add', false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      console.log(json)
      res.send(json)
      this.addtags.tags.map((tag)=>{
        this.user.likedTags.push(tag)
      })
      localStorage.setItem('userData', JSON.stringify(this.user))
      location.reload()
    },
    dellInCommon(array1, array2){
      array2.forEach((item) => {
        for (let index in array1) {
          if (array1[index]=== item) {
            array1.splice(index, 1);
          }
        }
      })
    }
  },
  beforeMount() {
    this.getTags()
  }
}

</script>

<template>
  <div class="scroller">
    <div class="form-box">
      <h1>Edit Profile</h1>
      <div class="centerxmenu">
        <label>Image</label>
        <vs-upload automatic limit="1" :headers="headersProfile" fileName="uploaded_file" action="/upload/attachImg" />
        <div class="tags">
          <div>
            <label>Tags</label>
          </div>
          <ul class="center">
            <li v-for="(tag,index) in user.likedTags" :key="index">
              <vs-checkbox v-model="delltags.tags" :vs-value="tag">{{ tag }}</vs-checkbox>
            </li>
          </ul>
          <vs-button @click="popupActivo=true" color="success" type="border">Add Tags</vs-button>
          <vs-button @click="dellTags" color="danger" type="border">Delete Selected Tags</vs-button>
          <vs-popup class="holamundo"  title="Select the tags of your game" :active.sync="popupActivo">
            <div class="box">
              <ul class="center">
                <li v-for="(tag,index) in availableTags1" :key="index">
                  <vs-checkbox v-model="addtags.tags" :vs-value="tag">{{ tag }}</vs-checkbox>
                </li>
              </ul>
            </div>
            <vs-button @click="addTags" color="success" type="filled">Add</vs-button>
            <vs-button  color="danger" type="filled">Delete all</vs-button>
            <vs-button  color="dark" type="filled">Cancel</vs-button>
          </vs-popup>
        </div>
        <div class="next">
          <vs-button @click="save" color="success" type="filled" icon="arrow_forward_ios">Save</vs-button>
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
.con-img-upload{
  width: 230px !important;
  background: rgba(0, 0, 0, 0.8) !important;
  color: black;
}
ul{
  display: flex;
  flex-wrap: wrap
}

li{
  display: flex;
}
</style>