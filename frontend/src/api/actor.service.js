import apiClient from "./api.client";

export default {
    getActors() {
        return apiClient.get('/actors');
    },
    getActor(actorId) {
        return apiClient.get(`/actors/${actorId}`);
    },
}
