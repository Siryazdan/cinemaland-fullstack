<template>
  <div id="app">
    <nav>
      <div class="brand">
        <svg width="93" height="30" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true" focusable="false">
          <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 14.5v-5l4.5 2.5-4.5 2.5z" fill="#DF1067"/>
        </svg>
        <div class="brand-text">
          <span class="brand-name">CINEMA</span>
          <span class="aj-signature" aria-label="A film by AJ">
            <span class="aj-credit">a film by</span>
            <span class="aj-mark">
              AJ
              <svg class="aj-swash" viewBox="0 0 60 12" aria-hidden="true" focusable="false">
                <path d="M2 8 C 15 2, 30 12, 42 6 S 56 3, 58 7"/>
              </svg>
            </span>
          </span>
        </div>
      </div>
      <ul v-if="auth.user">
        <li>
          <router-link to="/">Movies</router-link>
        </li>
        <li>
          <router-link to="/categories">Categories</router-link>
        </li>
        <li>
          <router-link to="/actors">Actors</router-link>
        </li>
        <li class="user">
          <span class="user-name">Hi, {{ auth.user.displayName }}</span>
          <button type="button" class="logout" @click="logout">Logout</button>
        </li>
      </ul>
    </nav>
    <div class="router-view">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import {useRouter} from "vue-router";
import {auth} from "@/auth";

export default {
  name: 'App',
  setup() {
    const router = useRouter();

    const logout = async () => {
      await auth.logout();
      await router.push('/login');
    };

    return {auth, logout};
  }
}
</script>

<style scoped>
/* General styles */
body {
  font-family: 'Roboto', sans-serif;
  margin: 0;
  padding: 0;
  background-color: #e0f7fa; /* Light cyan background color */
  color: #333;
  transition: background-color 0.5s ease; /* Smooth transition for background color */
}

/* Navigation bar styles */
nav {
  background-color: darkturquoise;
  color: #fff;
  padding: 1.5rem 3rem; /* Increased padding for a bigger nav */
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Increased shadow for better styling */
  border-radius: 10px; /* Slightly more rounded corners */
  margin: 1.5rem; /* Increased margin around the nav */
}

nav .brand {
  display: flex;
  align-items: center;
}

nav .brand svg {
  margin-right: 1rem; /* Increased margin for better spacing */
}

nav .brand-text {
  display: flex;
  flex-direction: column;
}

nav .brand-name {
  font-size: 2rem; /* Increased font size */
  font-weight: bold;
  line-height: 1;
}

/* AJ signature – "a film by AJ" credit under the logo */
.aj-signature {
  display: flex;
  align-items: baseline;
  gap: 6px;
  margin-top: 4px;
  line-height: 1;
}

.aj-credit {
  font-family: 'Oswald', sans-serif;
  font-size: 0.6rem;
  letter-spacing: 3px;
  text-transform: uppercase;
  color: rgba(255, 255, 255, 0.8);
}

.aj-mark {
  position: relative;
  display: inline-block;
  padding-right: 4px;
  font-family: 'Yellowtail', 'Brush Script MT', cursive;
  font-size: 1.4rem;
  color: #fff;
  transform: rotate(-6deg);
  text-shadow: 0 1px 6px rgba(223, 16, 103, 0.6);
}

.aj-swash {
  position: absolute;
  left: -4px;
  bottom: -5px;
  width: 38px;
  height: 9px;
  overflow: visible;
}

.aj-swash path {
  fill: none;
  stroke: #df1067;
  stroke-width: 1.8;
  stroke-linecap: round;
  stroke-dasharray: 70;
  stroke-dashoffset: 70;
  animation: aj-draw 1.2s 0.4s ease-out forwards;
}

nav .brand:hover .aj-swash path {
  animation: aj-redraw 0.9s ease-out forwards;
}

@keyframes aj-draw {
  to { stroke-dashoffset: 0; }
}

@keyframes aj-redraw {
  from { stroke-dashoffset: 70; }
  to { stroke-dashoffset: 0; }
}

@media (prefers-reduced-motion: reduce) {
  .aj-swash path,
  nav .brand:hover .aj-swash path {
    animation: none;
    stroke-dashoffset: 0;
  }
}

nav ul {
  list-style: none;
  display: flex;
  margin: 0;
  padding: 0;
}

nav ul li {
  margin-left: 2rem; /* Increased margin between list items */
}

nav ul li a {
  color: #fff;
  text-decoration: none;
  font-size: 1.2rem; /* Increased font size */
  transition: color 0.3s ease;
  padding: 0.5rem 1rem; /* Padding for links */
  border-radius: 5px; /* Rounded corners for links */
}

nav ul li a:hover {
  color: #df1067;
  background-color: #444; /* Background color on hover */
}

nav ul {
  align-items: center;
}

nav .user {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-left: 3rem;
  padding-left: 1.5rem;
  border-left: 1px solid rgba(255, 255, 255, 0.4);
}

nav .user-name {
  font-size: 1rem;
  white-space: nowrap;
}

nav .logout {
  padding: 0.45rem 1rem;
  border: 1px solid #fff;
  border-radius: 5px;
  background: transparent;
  color: #fff;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
}

nav .logout:hover {
  background: #fff;
  color: #df1067;
}

/* Router view styles */
.router-view {
  padding: 2rem;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  animation: fadeIn 1s ease-in-out; /* Fade-in animation */
}

/* Keyframes for fade-in animation */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>