<script>
export default {
  name: "index",
  data(){
    return {
      popupActivo1:false,
      popupActivo2:false,
      tag:'',
      tags:[]
    }
  },
  methods:{
    getTags(){
      var res = new XMLHttpRequest()
      res.open("GET", "/tags/available_tags", false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      this.tags = JSON.parse(res.response).availableTags
      console.log(this.tags)
    },

    sendTags(){
      var res = new XMLHttpRequest()
      var json = JSON.stringify(this.tags)
      res.open("PATCH", "/tags/available_tags/add", false)
      res.setRequestHeader("Accept", "application/json")
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("Authorization", localStorage.getItem("token"))
      console.log(json)
      res.send(json)
    },

    addTag(){
      this.tags.push({availableTag: this.tag})
      this.sendTags()
    }
  },
  beforeMount() {
    this.getTags()
  }
}
</script>

<template>
  <div>
    {{tags}}
    <h1>Admin Menu</h1>
    <div class="">
      <vs-tabs color="success">
        <vs-tab label="Tags">
          <div class="con-tab-ejemplo">
            <vs-button @click="popupActivo1=true" color="success" type="filled">Add Tag</vs-button>
            <vs-button @click="popupActivo2=true" color="danger" type="filled">Delate Tag</vs-button>
            <div class="box">
                <vs-list v-for="(show, index) in tags" :key=index>
                  <vs-list-item  title=''>
                    <vs-checkbox color="danger"/>
                  </vs-list-item>
                </vs-list>
            </div>
          </div>
        </vs-tab>
        <div class="centerx">
          <vs-popup class="tagpopup"  title="Add Tag" :active.sync="popupActivo1" button-close-hidden="true">
            <vs-input placeholder="Tag" color="success" v-model="this.tag"/>
            <vs-button @click="addTag" color="success" type="filled">Add Tag</vs-button>
            <vs-button @click="popupActivo1=false" color="dark" type="flat">Cancel</vs-button>
          </vs-popup>
          <vs-popup class="tagpopup"  title="Delate Tag" :active.sync="popupActivo2" button-close-hidden="true">
            <p>
              Are you sure you want to delate the tags selected?
            </p>
            <vs-button  color="danger" type="filled">Yes</vs-button>
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
  position: fixed;
  left: 120px;
  top: 210px;
  right: 0;
}

.tagpopup .vs-button{
  display: revert;
}

</style>