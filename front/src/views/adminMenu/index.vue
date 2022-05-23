<script>

export default {
  name: "index",
  data(){
    return {
      popupActivo1:false,
      popupActivo2:false,
      tag1: '',
      tagsTosend:{
        tags: []
      },
      tagsTodelete:{
        tags: []
      },
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
    }
  },
  beforeMount() {
    this.getTags()
  }
}
</script>

<template>
  <div>
    {{tagsTodelete.tags}}
    <h1>Admin Menu</h1>
    <div class="">
      <vs-tabs color="success">
        <vs-tab label="Tags">
          <div class="con-tab-ejemplo">
            <vs-button @click="popupActivo1=true" color="success" type="filled">Add Tag</vs-button>
            <vs-button @click="popupActivo2=true" color="danger" type="filled">Delate Tag</vs-button>
            <div class="box">
              <ul class="center">
                <li v-for="(tag,index) in tagsget" :key="index">
                  <vs-checkbox v-model="tagsTodelete.tags" :vs-value="tag">{{ tag }}</vs-checkbox>
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
        <vs-tab label="Publish Game List">
          <div class="con-tab-ejemplo">
            Service
          </div>
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
  left: 130px;
  top: -90px;
  display: flex;
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

</style>