<template>
  <div id="app">
    <nav>
      <div class="brand">
        <svg width="93" height="30" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true" focusable="false">
          <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 14.5v-5l4.5 2.5-4.5 2.5z" fill="#DF1067"/>
        </svg>
        <span>CINEMA</span>
      </div>
      <ul>
        <li>
          <router-link to="/">Movies</router-link>
        </li>
        <li>
          <router-link to="/categories">Categories</router-link>
        </li>
        <li>
          <router-link to="/actors">Actors</router-link>
        </li>
        <li>
        </li>
      </ul>
    </nav>
    <div class="router-view">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  components: {}
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

nav .brand span {
  font-size: 2rem; /* Increased font size */
  font-weight: bold;
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