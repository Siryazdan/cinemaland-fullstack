<script>
import {computed, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {auth, errorMessage, safeRedirect} from "@/auth";

const MIN_PASSWORD_LENGTH = 8;

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();

    const mode = ref('login'); // 'login' | 'signup'
    const displayName = ref('');
    const email = ref('');
    const password = ref('');
    const confirmPassword = ref('');
    const error = ref('');
    const busy = ref(false);

    const isSignup = computed(() => mode.value === 'signup');

    const switchMode = () => {
      mode.value = isSignup.value ? 'login' : 'signup';
      error.value = '';
      password.value = '';
      confirmPassword.value = '';
    };

    const submit = async () => {
      error.value = '';
      if (isSignup.value) {
        if (password.value.length < MIN_PASSWORD_LENGTH) {
          error.value = `Your password must be at least ${MIN_PASSWORD_LENGTH} characters long.`;
          return;
        }
        if (password.value !== confirmPassword.value) {
          error.value = "The two passwords don't match.";
          return;
        }
      }

      busy.value = true;
      try {
        if (isSignup.value) {
          await auth.signup(displayName.value, email.value, password.value);
        } else {
          await auth.login(email.value, password.value);
        }
        await router.replace(safeRedirect(route.query.redirect));
      } catch (e) {
        error.value = errorMessage(e, isSignup.value
            ? 'Sign up failed. Please try again.'
            : 'Login failed. Is the backend running?');
      } finally {
        busy.value = false;
      }
    };

    return {
      mode, isSignup, displayName, email, password, confirmPassword, error, busy,
      switchMode, submit, MIN_PASSWORD_LENGTH,
    };
  }
}
</script>

<template>
  <div class="auth-wrapper">
    <div class="auth-card">
      <h1>{{ isSignup ? 'Create your account' : 'Welcome back' }}</h1>
      <p class="lead">
        {{ isSignup
          ? 'Start your own private movie collection. Only you can see and edit it.'
          : 'Log in to manage your own movie collection.' }}
      </p>

      <div v-if="error" class="error" role="alert">{{ error }}</div>

      <form @submit.prevent="submit" novalidate>
        <label v-if="isSignup">
          <span>Name</span>
          <input v-model.trim="displayName" type="text" autocomplete="name" maxlength="100"
                 placeholder="Your name" required>
        </label>

        <label>
          <span>Email</span>
          <input v-model.trim="email" type="email" autocomplete="email" placeholder="you@example.com" required>
        </label>

        <label>
          <span>Password</span>
          <input v-model="password" type="password" maxlength="72"
                 :autocomplete="isSignup ? 'new-password' : 'current-password'"
                 :placeholder="isSignup ? `At least ${MIN_PASSWORD_LENGTH} characters` : 'Your password'" required>
        </label>

        <label v-if="isSignup">
          <span>Confirm password</span>
          <input v-model="confirmPassword" type="password" maxlength="72" autocomplete="new-password"
                 placeholder="Repeat your password" required>
        </label>

        <button type="submit" :disabled="busy">
          {{ busy ? 'Please wait…' : (isSignup ? 'Sign up' : 'Log in') }}
        </button>
      </form>

      <p class="switch">
        {{ isSignup ? 'Already have an account?' : 'New to Cinemaland?' }}
        <a href="#" @click.prevent="switchMode">{{ isSignup ? 'Log in' : 'Create an account' }}</a>
      </p>
    </div>
  </div>
</template>

<style scoped>
.auth-wrapper {
  display: flex;
  justify-content: center;
  padding: 2rem 0;
}

.auth-card {
  width: 100%;
  max-width: 420px;
  padding: 2rem;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
  border-top: 4px solid #df1067;
}

h1 {
  margin: 0 0 0.4rem;
  font-size: 1.6rem;
}

.lead {
  margin: 0 0 1.5rem;
  color: #666;
  line-height: 1.45;
}

form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

label {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
  font-size: 0.85rem;
  font-weight: 600;
  color: #444;
}

input {
  padding: 0.75rem 0.9rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

input:focus {
  outline: none;
  border-color: darkturquoise;
  box-shadow: 0 0 0 3px rgba(0, 206, 209, 0.2);
}

button {
  margin-top: 0.5rem;
  padding: 0.8rem;
  border: none;
  border-radius: 8px;
  background: #df1067;
  color: #fff;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: filter 0.2s ease;
}

button:hover:not(:disabled) {
  filter: brightness(1.1);
}

button:disabled {
  opacity: 0.7;
  cursor: wait;
}

.error {
  margin-bottom: 1rem;
  padding: 0.75rem 0.9rem;
  border-radius: 8px;
  background: #fdecf2;
  border: 1px solid #f5b3cb;
  color: #a10a49;
}

.switch {
  margin: 1.5rem 0 0;
  text-align: center;
  color: #666;
}

.switch a {
  color: #df1067;
  font-weight: 600;
  text-decoration: none;
}

.switch a:hover {
  text-decoration: underline;
}
</style>
