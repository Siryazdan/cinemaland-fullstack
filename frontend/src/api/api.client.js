import axios from "axios";

const apiClient = axios.create({
    baseURL: 'http://localhost:3000/api',
    headers: {
        'Content-type': 'application/json'
    }
});


apiClient.interceptors.request.use(config => {
    const token = localStorage.getItem('authToken');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    } else {
        console.error("Auth token not found");
    }
    return config;
}, error => {
    return Promise.reject(error);
});


export default apiClient;