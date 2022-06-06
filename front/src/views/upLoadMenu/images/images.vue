<script>
export default {
  name: "images",
  data(){
    return {
      headersMain:{
        token: localStorage.getItem('token'),
        id: localStorage.getItem('id'),
        imgType: 'main'
      },
      headersCarousel:{
        token: localStorage.getItem('token'),
        id: localStorage.getItem('id'),
        imgType: 'carousel'
      }
    }
  }

}
</script>

<template>
  <div>
    <div class="form-box">
      <div class="centerx">
        <vs-upload limit="1" :headers="headersMain" fileName="uploaded_file" action="/upload/attachImg" />
        <vs-upload limit="10" :headers="headersCarousel" fileName="uploaded_file" action="/upload/attachImg" />
      </div>
    </div>
  </div>
</template>



<style>

.con-img-upload{
  background: rgba(0, 0, 0, 0.8) !important;
  width: 98%;
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