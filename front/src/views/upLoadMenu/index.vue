<script>
export default {
  name: "index.vue",
  data(){
    return {
      popupActivo:false,
    }
  }
}
</script>

<template>

  <div>
    <H1>UpLoad Menu</H1>
    <div class="centerxmenu">
      <vs-input label="Name" placeholder="" v-model="value1"/>
      <vs-input label="Wiki" placeholder="Url" v-model="value1"/>
      <vs-input label="Name" placeholder="Placeholder" v-model="value1"/>
      <div>
        <vs-button @click="popupActivo=true" color="primary" type="border">Open Default popup</vs-button>
        <vs-popup class="holamundo"  title="Select the tags of your game" :active.sync="popupActivo">

        </vs-popup>
      </div>
      <vs-textarea label="Game Description" v-model="textarea" width="40%" height="300px"/>
    </div>
  </div>

</template>

<style>

.centerxmenu{
  position: relative;
  left: 10px;
}
.centerxmenu .vs-input{
  margin-top: 40px;
  width: 40%;
  text-align: left;

}

.vs-input--label{
  color: white;
}

.centerxmenu .vs-button{
  position: relative;
  left: 0;
}

.vs-textarea{
  color: white;
  text-align: left;
}

</style>