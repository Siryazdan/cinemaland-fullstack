import {reactive} from "vue";
import authService from "@/api/auth.service";

/**
 * Who is logged in. `user` is:
 * - undefined: not checked yet
 * - null: not logged in
 * - {id, email, displayName}: logged in
 */
export const auth = reactive({
    user: undefined,

    async load() {
        try {
            const response = await authService.me();
            this.user = response.data;
        } catch (e) {
            this.user = null;
        }
        return this.user;
    },

    async login(email, password) {
        const response = await authService.login(email, password);
        this.user = response.data;
    },

    async signup(displayName, email, password) {
        const response = await authService.signup(displayName, email, password);
        this.user = response.data;
    },

    async logout() {
        try {
            await authService.logout();
        } finally {
            this.user = null;
        }
    },
});

/** Only allow redirects to pages inside this app (no "//other-site.com"). */
export function safeRedirect(value) {
    return typeof value === 'string' && value.startsWith('/') && !value.startsWith('//') ? value : '/';
}

/** The error message the backend sent, or a fallback. */
export function errorMessage(e, fallback) {
    return e?.response?.data?.message || fallback;
}
