<script>
import {onMounted, ref, toRaw} from "vue";
import movieService from "@/api/movie.service";
import Movie from "@/models/Movie";
import categoryService from "@/api/category.service";
import fileDataService from "@/api/file-data.service";

export default {
  setup() {
    const movies = ref([]);
    const editMode = ref(false);
    const selectedMovie = ref(new Movie());
    const categories = ref([]);
    const previewUrl = ref(null);
    const selectedFile = ref(null);

    const handleFileUpload = (event) => {
      const file = event.target.files[0];

      if (file) {
        selectedFile.value = file;
        if (file.type.startsWith("image/")) {
          previewUrl.value = URL.createObjectURL(file);
        } else {
          previewUrl.value = null;
        }
      }
    };

    const createFile = async () => {
      const formData = new FormData();
      formData.append("file", selectedFile.value);

      try {
        const response = await fileDataService.createFile(selectedMovie.value.id, formData);
        previewUrl.value = null;
        console.log("Upload successful:", response.data);
        await fetchMovies();
      } catch (error) {
        console.error("Error uploading file:", error);
        previewUrl.value = null;
      }
    };

    const updateFile = async (fileId) => {
      const formData = new FormData();
      formData.append("file", selectedFile.value);

      try {
        const response = await fileDataService.updateFile(fileId, formData);
        console.log("Upload successful:", response.data);
        await fetchMovies();
        previewUrl.value = null;
      } catch (error) {
        console.error("Error uploading file:", error);
        previewUrl.value = null;
      }
    };

    const fetchCategories = async () => {
      const response = await categoryService.getCategories();
      categories.value = response.data;
    };

    const fetchMovies = async () => {
      const response = await movieService.getMovies();
      movies.value = response.data;
      await fetchCategories();
    };

    const editMovie = (movie) => {
      toggleEditMode();
      console.log('Edit Movie', toRaw(movie));
      selectedMovie.value = movie;
      console.log('Selected Movie', toRaw(selectedMovie.value));
    }

    const saveMovie = async () => {
      if (selectedMovie.value.id) {
        await movieService.updateMovie(selectedMovie.value).then(() => {
          console.log('Movie updated');
          toggleEditMode();
        });
      } else {
        await movieService.createMovie(selectedMovie.value).then(async (data) => {
          console.log('Movie created', data);
          toggleEditMode();
        });
      }
      await fetchMovies();
    }
    const deleteMovie = async (id) => {
      await movieService.deleteMovie(id).then(() => {
        console.log('Movie deleted');
      });
      await fetchMovies();

    }
    const toggleEditMode = () => {
      editMode.value = !editMode.value;
      selectedMovie.value = new Movie();
    }

    onMounted(fetchMovies);

    return {
      movies,
      editMode,
      editMovie,
      selectedMovie,
      saveMovie,
      deleteMovie,
      categories,
      toggleEditMode,
      selectedFile,
      handleFileUpload,
      updateFile,
      createFile,
      previewUrl
    };
  }
}
</script>

<template>
  <div class="wrapper">
    <div v-bind:class="{ 'edit-mode-blur': editMode }">
      <div class="flex-between-center">
        <h1>Movies</h1>
        <button @click="toggleEditMode" class="btn-default">New Movie</button>
      </div>
      <ul>
        <li v-for="movie in movies" :key="movie.id">
          <div class="card">
            <img v-if="movie?.image?.id"
                 v-bind:src="'http://localhost:3000/api/files/' + movie?.image?.id + '/_stream'" alt="Image">
            <img v-else src="https://placehold.co/227x127" alt="No Image" @click="editMovie(movie)"
                 title="Add Image" class="no-image"/>
            <div class="card-body">
              <div>
                <h5 class="card-title">{{ movie.title }}</h5>
                <p class="card-text">{{ movie.genre }}</p>
                <p class="card-text">{{ movie.director }}</p>
                <p class="card-text">{{ movie.cast }}</p>
              </div>
              <div class="actions">
                <button @click="deleteMovie(movie.id)" class="danger">Delete</button>
                <button @click="editMovie(movie)">
                  Edit
                </button>
                <router-link :to="`/movie/${movie.id}`" class="detail-button">Details</router-link>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>
    <div class="drawer" v-bind:class="{ 'edit-mode': editMode }">
      {{ !selectedMovie ? 'No Movie selected' : selectedMovie?.title }}

      <form @submit.prevent="saveMovie">
        <div class="form-group">
          <label for="title">Title<sup>*</sup></label>
          <input type="text" id="title" v-model="selectedMovie.title" required>
        </div>
        <div class="form-group">
          <label for="genre">Genre<sup>*</sup></label>
          <input type="text" id="genre" v-model="selectedMovie.genre" required>
        </div>
        <div class="form-group">
          <label for="director">Director<sup>*</sup></label>
          <input type="text" id="director" v-model="selectedMovie.director" required>
        </div>
        <div class="form-group">
          <label for="cast">Cast<sup>*</sup></label>
          <input type="text" id="cast" v-model="selectedMovie.cast" required>
        </div>
        <div class="form-group">
          <label for="synopsis">Synopsis<sup>*</sup></label>
          <input type="text" id="synopsis" v-model="selectedMovie.synopsis" required>
        </div>
        <div class="form-group">
          <label for="rating">Rating<sup>*</sup></label>
          <input type="number" min="1" max="10" id="rating" v-model="selectedMovie.rating" required>
        </div>
        <div class="form-group">
          <label for="category">Category<sup>*</sup></label>
          <select name="category" id="category" v-model="selectedMovie.category" required>
            <option v-for="category in categories" v-bind:value="category" :key="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>
        <button type="submit">{{ selectedMovie?.id ? 'Update' : 'Create' }}</button>
      </form>

      <div class="file-upload">
        <template v-if="selectedMovie?.id != null">
          <template v-if="selectedMovie?.image?.relId">
            <div>
              <label for="file" class="file-upload-wrapper" v-if="!previewUrl">
                <svg width="100%" height="100%" viewBox="0 0 120 120" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <rect width="120" height="120"/>
                  <path fill-rule="evenodd" clip-rule="evenodd"
                        d="M33.2503 38.4816C33.2603 37.0472 34.4199 35.8864 35.8543 35.875H83.1463C84.5848 35.875 85.7503 37.0431 85.7503 38.4816V80.5184C85.7403 81.9528 84.5807 83.1136 83.1463 83.125H35.8543C34.4158 83.1236 33.2503 81.957 33.2503 80.5184V38.4816ZM80.5006 41.1251H38.5006V77.8751L62.8921 53.4783C63.9172 52.4536 65.5788 52.4536 66.6039 53.4783L80.5006 67.4013V41.1251ZM43.75 51.6249C43.75 54.5244 46.1005 56.8749 49 56.8749C51.8995 56.8749 54.25 54.5244 54.25 51.6249C54.25 48.7254 51.8995 46.3749 49 46.3749C46.1005 46.3749 43.75 48.7254 43.75 51.6249Z"
                        fill="#687787"/>
                </svg>
                <p>Click here to select an image</p>
                <input id="file" type="file" @change="handleFileUpload"/>
              </label>
              <div v-if="previewUrl">
                <img :src="previewUrl" alt="Preview" class="preview-image"/>
              </div>
              <div class="actions">
                <button @click="previewUrl = null" class="cancel-upload" v-if="previewUrl">Cancel</button>
                <button @click="updateFile(selectedMovie?.image?.relId)" :disabled="!selectedFile || !previewUrl"
                        class="upload">
                  Upload
                </button>
              </div>
            </div>
          </template>
          <template v-else>
            <div>
              <label for="file" class="file-upload-wrapper" v-if="!previewUrl">
                <svg width="100%" height="100%" viewBox="0 0 120 120" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <rect width="120" height="120"/>
                  <path fill-rule="evenodd" clip-rule="evenodd"
                        d="M33.2503 38.4816C33.2603 37.0472 34.4199 35.8864 35.8543 35.875H83.1463C84.5848 35.875 85.7503 37.0431 85.7503 38.4816V80.5184C85.7403 81.9528 84.5807 83.1136 83.1463 83.125H35.8543C34.4158 83.1236 33.2503 81.957 33.2503 80.5184V38.4816ZM80.5006 41.1251H38.5006V77.8751L62.8921 53.4783C63.9172 52.4536 65.5788 52.4536 66.6039 53.4783L80.5006 67.4013V41.1251ZM43.75 51.6249C43.75 54.5244 46.1005 56.8749 49 56.8749C51.8995 56.8749 54.25 54.5244 54.25 51.6249C54.25 48.7254 51.8995 46.3749 49 46.3749C46.1005 46.3749 43.75 48.7254 43.75 51.6249Z"
                        fill="#687787"/>
                </svg>
                <p>Click here to select an image</p>
                <input id="file" type="file" @change="handleFileUpload"/>
              </label>
              <div v-if="previewUrl">
                <img :src="previewUrl" alt="Preview" class="preview-image"/>
              </div>
              <div class="actions">
                <button @click="previewUrl = null" class="cancel-upload" v-if="previewUrl">Cancel</button>
                <button @click="createFile" :disabled="!selectedFile || !previewUrl" class="upload">Upload</button>
              </div>
            </div>
          </template>
        </template>
      </div>
    </div>
    <div class="backdrop" v-bind:class="{ 'visible': editMode }" @click="editMode = !editMode"></div>
  </div>
</template>

<style scoped>
.wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background: #f0f0f0;
  min-height: 100vh;
}

.flex-between-center {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 800px;
  margin-bottom: 20px;
}

.btn-default {
  background: #007bff;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}
.actions .danger {
  background: #dc3545 !important;
  color: #fff;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.actions .danger:hover {
  background: #c82333 !important;
}

.btn-default:hover {
  background: #0056b3 !important;
}

ul {
  list-style: none;
  padding: 0;
  width: 100%;
  max-width: 800px;
}

li {
  margin-bottom: 20px;
}

.card {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  padding: 20px;
  align-items: center;
}

.card img {
  width: 227px;
  height: 127px;
  object-fit: cover;
  border-radius: 10px;
  margin-right: 20px;
}

.card-body {
  flex: 1;
}

.card-title {
  margin: 0 0 10px;
  font-size: 1.5em;
  color: #333;
}

.card-text {
  margin: 5px 0;
  color: #555;
}

.actions {
  display: flex;
  gap: 10px;
  align-items: flex-start;
  cursor: pointer;
}

.actions button, .actions .details-link {
  background: #007bff;
  cursor: pointer;
  color: #fff;
  padding: 10px 20px;
  border-radius: 5px;
  text-decoration: none;
  transition: background 0.3s;
  margin-top: 10px;
}

.actions .details-link {
  background: #28a745;
  color: #fff;
  padding: 10px 20px;
  border-radius: 5px;
  text-decoration: none;
  transition: background 0.3s;
  margin-top: 10px;
  cursor: pointer;
}

.detail-button {
  background: #28a745;
  color: #fff;
  padding: 10px 20px;
  border-radius: 5px;
  text-decoration: none;
  transition: background 0.3s;
  margin-top: 10px;
}

.actions .details-link:hover {
  background: #218838;
}

.no-image {
  cursor: pointer;
}

.edit-mode-blur {
  filter: blur(5px);
}

.drawer {
  position: fixed;
  top: 0;
  right: 0;
  width: 400px;
  height: 100%;
  background: #fff;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.1);
  padding: 20px;
  transform: translateX(100%);
  transition: transform 0.3s ease-in-out;
  z-index: 1000;
  overflow-y: auto;
}

.drawer.edit-mode {
  transform: translateX(0);
}

.backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  opacity: 0;
  transition: opacity 0.3s ease-in-out;
  pointer-events: none;
  z-index: 999;
}

.backdrop.visible {
  opacity: 1;
  pointer-events: all;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 0.5rem;
  color: #333;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1rem;
  color: #333;
  transition: border-color 0.3s ease;
}

.form-group input:focus,
.form-group select:focus {
  border-color: #007bff;
  outline: none;
}

.form-group sup {
  color: #df1067;
  font-size: 0.8rem;
}
</style>