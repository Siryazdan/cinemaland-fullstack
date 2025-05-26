<script>
import movieService from "@/api/movie.service";
import { onMounted, ref, toRefs } from "vue";

export default {
  props: {
    id: {
      type: String,
      required: true
    }
  },
  setup(props) {
    const { id } = toRefs(props);

    const actors = ref([]);
    const fetchActors = async () => {
      try {
        const response = await movieService.getActors(id.value);
        actors.value = response.data;
      } catch (error) {
        console.error("Error fetching actors:", error);
      }
    };

    onMounted(fetchActors);

    return {
      actors
    };
  }
};
</script>

<template>
  <h1>Actors</h1>
  <ul>
    <li v-for="actor in actors" :key="actor.id">
      {{ actor?.name }} - {{ actor?.height }} - {{ actor?.age }}
      <router-link :to="`/movie/${actor['movie-id']}/actor/${actor.id}`">Details</router-link>
    </li>
  </ul>
</template>

<style scoped>
</style>