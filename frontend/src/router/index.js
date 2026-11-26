import {createRouter, createWebHistory} from "vue-router";
import CategoryList from "@/views/CategoryList.vue";
import MovieDetail from "@/views/MovieDetail.vue";
import ActorDetail from "@/views/ActorDetail.vue";
import ActorList from "@/views/ActorList.vue";
import MovieList from "@/views/MovieList.vue";
import LoginView from "@/views/LoginView.vue";
import {auth, safeRedirect} from "@/auth";

const routes = [
    {path: '/login', component: LoginView, meta: {public: true}},
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

// Every page except the login page needs a logged-in user
router.beforeEach(async (to) => {
    if (auth.user === undefined) {
        await auth.load();
    }
    if (to.meta.public) {
        // already logged in? then the login page makes no sense
        return auth.user ? safeRedirect(to.query.redirect) : true;
    }
    if (!auth.user) {
        return {path: '/login', query: to.fullPath !== '/' ? {redirect: to.fullPath} : {}};
    }
    return true;
});

export default router;
