<script>
import movieService from "@/api/movie.service";
import {fileUrl} from "@/api/api.client";
import {onMounted, ref, watch} from "vue";

export default {
  name: 'MovieDetail',
  props: {
    id: {
      type: String,
      required: true
    }
  },
  setup(props) {
    const movie = ref(null);
    const error = ref('');

    const fetchMovie = async () => {
      try {
        const response = await movieService.getMovie(props.id);
        movie.value = response.data;
      } catch (e) {
        error.value = 'Movie not found.';
      }
    };

    onMounted(fetchMovie);
    watch(() => props.id, fetchMovie);

    return {movie, error, fileUrl};
  }
}
</script>

<template>
  <div class="movie-detail">
    <p v-if="error">{{ error }}</p>
    <template v-else-if="movie">
      <img v-if="fileUrl(movie.image)" :src="fileUrl(movie.image)" :alt="movie.title">
      <h1>{{ movie.title }}</h1>
      <p>{{ movie.genre }} · {{ movie.director }} · ⭐ {{ movie.rating }}</p>
      <p v-if="movie.category">Category: {{ movie.category.name }}</p>
      <p>{{ movie.synopsis }}</p>

      <div class="actor-list" v-if="movie.actors && movie.actors.length">
        <h2>Actors</h2>
        <ul>
          <li v-for="actor in movie.actors" :key="actor.id">
            <router-link :to="`/actors/${actor.id}`">{{ actor.name }}</router-link>
          </li>
        </ul>
      </div>
      <p v-else-if="movie.cast">Cast: {{ movie.cast }}</p>
    </template>
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