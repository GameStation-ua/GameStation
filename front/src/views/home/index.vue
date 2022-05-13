<script>
import 'vue3-carousel/dist/carousel.css';
import { Carousel, Slide, Pagination, Navigation } from 'vue3-carousel';
export default {
  name: "index",
  components: {
    Carousel,
    Slide,
    Pagination,
    Navigation,
  },

}


</script>

<template>
  <div>
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
    <Carousel>
      <Slide v-for="slide in 10" :key="slide">
        <div class="carousel__item">{{ slide }}</div>
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



.carousel__slide {
  padding: 10px;
}

.carousel__prev,
.carousel__next {
  box-sizing: content-box;
  color: #ffffff;
  background-color: black;
}
.carousel__next--in-active {
  display: none;
}
</style>