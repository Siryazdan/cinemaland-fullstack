import {createRouter, createWebHistory} from "vue-router";
import CategoryList from "@/views/CategoryList.vue";
import MovieDetail from "@/views/MovieDetail.vue";
import ActorDetail from "@/views/ActorDetail.vue";
import MovieList from "@/views/MovieList.vue";


const routes = [
    {path: '/', component: MovieList},
    {path: '/movie/:id', component: MovieDetail},
    {path: '/movie/:movieId/Actor/:id', component: ActorDetail},
    {path: '/categories', component: CategoryList},
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
