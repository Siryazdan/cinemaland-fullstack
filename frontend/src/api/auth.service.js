import apiClient from "@/api/api.client";

export default {
    me() {
        return apiClient.get('/auth/me');
    },
    login(email, password) {
        return apiClient.post('/auth/login', {email, password});
    },
    signup(displayName, email, password) {
        return apiClient.post('/auth/signup', {displayName, email, password});
    },
    logout() {
        return apiClient.post('/auth/logout');
    },
}
