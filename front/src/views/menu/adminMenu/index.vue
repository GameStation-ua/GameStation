<script>
export default {
  name: "index",

  data(){
    return {
      popupActivo1:false,
      popupActivo2:false,
      tags:["hola", "vos", "hola"]
    }
  },
  methods:{
    getTags(){
      var res = new XMLHttpRequest()
      res.open("GET", "http://localhost:8443/tags/available_tags", false)
      res.setRequestHeader("Content-Type", "application/json")
      res.setRequestHeader("token", localStorage.getItem("token"))
      res.send(null)
      var data = JSON.parse(res.response)
      if (res.status === 200){
        console.log(data)
      }
    }
  }
}
</script>

<template>
  <div>
    {{getTags()}}
    <h1>Admin Menu</h1>
    <div class="">
      <vs-tabs color="success">
        <vs-tab label="Tags">
          <div class="con-tab-ejemplo">
            <vs-button @click="popupActivo1=true" color="success" type="filled">Add Tag</vs-button>
            <vs-button @click="popupActivo2=true" color="danger" type="filled">Delate Tag</vs-button>
            <div class="box">

            </div>
          </div>
        </vs-tab>
        <div class="centerx">
          <vs-popup class="tagpopup"  title="Add Tag" :active.sync="popupActivo1" button-close-hidden="true">
            <p>
              Add tag here
            </p>
            <vs-input placeholder="Tag" color="success" v-model="$store.state.tag"/>
            <vs-button  color="success" type="filled">Add Tag</vs-button>
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
  top: 20%;
  right: 0;
}

.tagpopup .vs-button{
  display: revert;
}

</style>