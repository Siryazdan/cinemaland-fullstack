import apiClient from "./api.client.js";

export default {
    getMovies() {
        return apiClient.get('/movies/');
    },
    getMovie(id) {
        return apiClient.get(`/movies/${id}`);
    },
    getActors(movieId) {
        return apiClient.get(`/movies/${movieId}/actors/`);
    },

    getActor(actorId) {
        return apiClient.get(`/actors/${actorId}`);
    },

    createMovie(movie) {
        return apiClient.post('/movies/', movie);
    },

    updateMovie(movie) {
        return apiClient.put(`/movies/${movie.id}`, movie);
    },

    deleteMovie(movieId) {
        return apiClient.delete(`/movies/${movieId}`);
    }

}