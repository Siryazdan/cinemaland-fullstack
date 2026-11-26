import { createApp } from 'vue';
import App from './App.vue';
import router from "@/router";
import apiClient from "@/api/api.client";
import {auth} from "@/auth";

// When the session has expired (or someone isn't logged in), go to the login page
apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        const url = error.config?.url || '';
        if (error.response?.status === 401 && !url.startsWith('/auth/')) {
            auth.user = null;
            const current = router.currentRoute.value;
            if (!current.meta.public) {
                router.push({path: '/login', query: {redirect: current.fullPath}});
            }
        }
        return Promise.reject(error);
    }
);

const app = createApp(App)
app.use(router)
app.mount('#app')
