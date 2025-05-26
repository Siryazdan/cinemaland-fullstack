<script>

import apiClient from "@/api/api.client";
import {useRoute} from "vue-router";
import movieService from "@/api/movie.service";
import {onMounted, ref} from "vue";

export default {
  name: 'MovieDetail',
  props: {
    id: String
  },
  setup() {
    const movie = ref({name: '', id: null});
    const route = useRoute();

    const apiBaseUrl = apiClient.defaults.baseURL;

    const fetchMovie = async () => {
      const response = await movieService.getMovie(route.params.id);
      movie.value = response.data;
    };
    onMounted(fetchMovie);
    return {
      movie,
      apiBaseUrl
    };
  }
}

</script>

<template>
  <div class="movie-detail">
    <img v-if="movie?.image?.id" :src="`${apiBaseUrl}/files/${movie.image.id}/_stream`" alt="Movie Image">
    <h1>{{ movie?.title }}</h1>
    <p>{{ movie?.genre }}, {{ movie?.director }}, {{ movie?.cast }}</p>
  </div>
</template>


<style scoped>
.movie-detail {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.movie-detail img {
  width: 100%;
  max-width: 400px;
  border-radius: 10px;
  margin-bottom: 20px;
}

.movie-detail h1 {
  font-size: 2em;
  margin: 0 0 10px;
  color: #333;
}

.movie-detail p {
  font-size: 1.2em;
  color: #555;
  margin: 5px 0;
  text-align: center;
}

.actor-list {
  margin-top: 20px;
  width: 100%;
}
</style>