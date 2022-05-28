<script>
import { Carousel, Slide, Navigation } from 'vue3-carousel';
export default {
  name: "index.vue",
  components: {
    Carousel,
    Slide,
    Navigation,
  },methods:{

  }
}
</script>

<template>
  <div class="scrollerGamePage">
    <div class="content">
      <div class="gameInfo">
        <div class="images">
          <div class="image">
            <img id="main" src="https://areajugones.sport.es/wp-content/uploads/2016/10/persona-5-portada.jpg">
          </div>
          <Carousel :items-to-show="4" wrapAround="true">
            <Slide v-for="slide in 10" :key="slide">
              <div class="carousel__images">
              </div>
            </Slide>
            <template #addons>
              <Navigation />
            </template>
          </Carousel>
          <h2>Description</h2>
          <p>
            Persona 5 is a fantasy based on reality which follows a group of troubled high school students: the protagonist and a collection of compatriots he meets along the way. These disturbed and troubled teenagers gradually realize that they are living in a toxic and dangerous world resembling a prison full of slavery, oppression and injustice, ruled by corrupted and twisted adults. They can't live with the system and can't live without it, and simply existing means they are at risk of being doomed and condemned to a life of slavery.

            In order to seek freedom, liberation and justice, they live dual lives as rebellious Phantom Thieves of Hearts. Using a mysterious smartphone app, they undertake fantastical adventures by using otherworldly powers to enter the hearts of people (specifically, corrupt adults in positions of power) in order to re-shape and transform them. The Phantom Thieves realize that society forces people to wear masks to protect their inner vulnerabilities, and by confronting their inner selves and by literally ripping off their protective mask do the heroes awaken their inner power, using it to help those in need. Ultimately, the group of Phantom Thieves seeks to change their day-to-day world to match their perception, end slavery and see through the masks modern society wears.

            An enhanced version, titled Persona 5 Royal, was released on PlayStation 4 on October 31st, 2019 in Japan and worldwide on March 31, 2020. It includes many new features, including new characters, battle mechanics and a third semester added to the story. This might not necessarily entirely replace the original game (particularly for PlayStation 3 players), however, as Persona enthusiasts may still enjoy this game's slightly higher difficulty, as well as prefer the original enemy and boss behaviors.
          </p>
        </div>
        <div class="right">
          <div class="rightContent" style="position: sticky !important;">
            <img id="rightimg" src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/cd/Persona_5_logo.svg/640px-Persona_5_logo.svg.png">
            <vs-button color="success" type="border">Add to list</vs-button>
            <vs-button color="danger" type="border">Follow game</vs-button>
            <div class="otherButtons">
              <vs-button color="primary" type="border">Forum</vs-button>
              <vs-button color="warning" type="border">Wiki</vs-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<style>

.images{
  width: 60%;
}

.otherButtons .vs-button{
  width: 40% !important;
}

.gameInfo{
  display: flex;
}

.right{
  width: 40%;
}

.rightContent .vs-button{
  width: 80%;
  margin: auto;
  margin-top: 10px;
}

label{
  text-align: left !important;
}

#main{
  width: 100%;
  height: 100%;
  object-fit: contain;
}

#rightimg{
  width: 80%;
}

.content{
  position: relative;
  top: 10px;
  width: 70%;
  margin: auto;
}

.scrollerGamePage{
  overflow-x: hidden;
  height: calc(100% - 100px);
}

.image{
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

.carousel__images{
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

p{
  color: white;
  text-align: left;
}

</style>