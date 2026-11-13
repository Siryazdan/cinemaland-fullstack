<script>
import actorService from "@/api/actor.service";
import {fileUrl} from "@/api/api.client";
import {onMounted, ref} from "vue";

export default {
  name: 'ActorList',
  setup() {
    const actors = ref([]);
    const error = ref('');

    const fetchActors = async () => {
      try {
        const response = await actorService.getActors();
        actors.value = response.data;
      } catch (e) {
        error.value = 'Could not load actors. Is the backend running?';
      }
    };

    onMounted(fetchActors);

    return {actors, error, fileUrl};
  }
};
</script>

<template>
  <h1>Actors</h1>
  <p v-if="error" class="error">{{ error }}</p>
  <p v-else-if="actors.length === 0">No actors yet.</p>
  <ul>
    <li v-for="actor in actors" :key="actor.id">
      <img v-if="fileUrl(actor.image)" :src="fileUrl(actor.image)" :alt="actor.name" width="48" height="48">
      <span>{{ actor.name }} – {{ actor.age }} years – {{ actor.height }} cm</span>
      <router-link :to="`/actors/${actor.id}`">Details</router-link>
    </li>
  </ul>
</template>

<style scoped>
h1 {
  text-align: center;
}

ul {
  list-style: none;
  padding: 0;
  max-width: 800px;
  margin: 0 auto;
}

li {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-bottom: 10px;
  background: #f9f9f9;
}

li span {
  flex: 1;
}

li img {
  border-radius: 50%;
  object-fit: cover;
}

.error {
  color: #dc3545;
  text-align: center;
}
</style>
