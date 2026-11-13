import {createRouter, createWebHistory} from "vue-router";
import CategoryList from "@/views/CategoryList.vue";
import MovieDetail from "@/views/MovieDetail.vue";
import ActorDetail from "@/views/ActorDetail.vue";
import ActorList from "@/views/ActorList.vue";
import MovieList from "@/views/MovieList.vue";

const routes = [
    {path: '/', component: MovieList},
    {path: '/movie/:id', component: MovieDetail, props: true},
    {path: '/actors', component: ActorList},
    {path: '/actors/:id', component: ActorDetail, props: true},
    {path: '/categories', component: CategoryList},
    {path: '/:pathMatch(.*)*', redirect: '/'},
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
