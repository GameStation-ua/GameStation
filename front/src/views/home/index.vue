<script>
import 'vue3-carousel/dist/carousel.css';
import { Carousel, Slide, Pagination, Navigation } from 'vue3-carousel';
import router from "@/router";
export default {
  name: "index",
  components: {
    Carousel,
    Slide,
    Pagination,
    Navigation,
  },methods:{
     page(rout){
      router.push(rout)
    }
  }
}


</script>

<template>
  <div class="scroller">
    <carousel :items-to-show="1" wrapAround="true">
      <slide v-for="slide in 10" :key="slide">
        <div class="carousel__item">{{ slide }}</div>
      </slide>

      <template #addons>
        <navigation />
        <pagination />
      </template>
    </carousel>
    <h2></h2>
    <Carousel :items-to-show="4" wrapAround="true">
      <Slide v-for="slide in 10" :key="slide">
          <div @click="page('/gamePage')" class="carousel__item1">
          </div>
        <div class="buttons">
          <vs-button color="danger" type="border" icon="favorite" ></vs-button>
          <vs-button color="primary" type="border" icon="add" ></vs-button>
        </div>
      </Slide>
      <template #addons>
        <Navigation />
      </template>
    </Carousel>
  </div>
  <router-view/>
</template>



<style>
.carousel__item {
  min-height: 500px;
  width: 100%;
  background-color: #1c6f42 !important;
  color:  var(--vc-clr-white);
  font-size: 20px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  left: 0 !important;
}

.carousel__item1 {
  padding-top: 56.25%;
  width: 100%;
  background-color: #1c6f42 !important;
  color:  var(--vc-clr-white);
  font-size: 20px;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  position: relative;
}

.scroller {
  overflow-x: hidden;
  height: calc(100% - 100px);
}

.carousel__slide {
  padding: 10px;
}

.carousel__prev{
  box-sizing: content-box;
  color: #ffffff;
  background-color: black;
  left: 1.3%;
}
.carousel__next {
  box-sizing: content-box;
  color: #ffffff;
  background-color: black;
  right: 1.3%;
}
.carousel__next--in-active {
  display: none;
}

.buttons{
  position: absolute;
  bottom: 15px;
  right: 15px;
}

.buttons .vs-button{
  margin-right: 3px;
}

</style>