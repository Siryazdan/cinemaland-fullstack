<script>
import actorService from "@/api/actor.service";
import {fileUrl} from "@/api/api.client";
import {onMounted, ref, watch} from "vue";

export default {
  name: 'ActorDetail',
  props: {
    id: {
      type: String,
      required: true
    }
  },
  setup(props) {
    const actor = ref(null);
    const error = ref('');

    const fetchActor = async () => {
      try {
        const response = await actorService.getActor(props.id);
        actor.value = response.data;
      } catch (e) {
        error.value = 'Actor not found.';
      }
    };

    onMounted(fetchActor);
    watch(() => props.id, fetchActor);

    return {actor, error, fileUrl};
  }
};
</script>

<template>
  <div class="actor-detail">
    <p v-if="error" class="error">{{ error }}</p>
    <template v-else-if="actor">
      <img v-if="fileUrl(actor.image)" :src="fileUrl(actor.image)" :alt="actor.name">
      <h1>{{ actor.name }}</h1>
      <p>Age: {{ actor.age }}</p>
      <p>Height: {{ actor.height }} cm</p>
    </template>
    <router-link to="/actors">← All actors</router-link>
  </div>
</template>

<style scoped>
.actor-detail {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.actor-detail img {
  max-width: 300px;
  border-radius: 10px;
  margin-bottom: 20px;
}

.error {
  color: #dc3545;
}
</style>
