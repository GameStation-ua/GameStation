<script>
export default {
  name: "editImages",
  data(){
    return {
      headersMain:{
        token: localStorage.getItem('token'),
        id: this.$route.params.id.toString(),
        imgType: 'main'
      },
      headersCarousel:{
        token: localStorage.getItem('token'),
        id: this.$route.params.id.toString(),
        imgType: 'carousel'
      },
    }
  }
}
</script>

<template>
  <div class="scroller">
    <div class="form-box">
      <div class="centerx">
        <h1>Change Main Image</h1>
        <vs-upload limit="1" :headers="headersMain" fileName="uploaded_file" action="/upload/attachImg"/>
        <h1>Add New Images</h1>
        <vs-upload :limit="10" :headers="headersCarousel" fileName="uploaded_file" action="/upload/attachImg"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.con-img-upload{
  background: rgba(0, 0, 0, 0.8) !important;
  width: 98% !important;
}

.form-box {
  width: 50%;
  background: rgba(0, 0, 0, 0.8);
  padding: 50px 0;
  color: #000000;
  box-shadow: 0 0 20px 2px rgba(0, 0, 0, 0.5);
  margin: 30px auto;
  height: fit-content;
}
</style>