<script>

import {onMounted, ref} from "vue";
import categoryService from "@/api/category.service";

export default {
  setup() {
    const categories = ref([]);
    const category = ref({name: '', id :null});

    const fetchCategories = async () => {
      const response = await categoryService.getCategories();
      console.log('Response'. response)
      categories.value = response.data;
    };

    const saveCategory = async () => {
      if(category.value.id == null) {
        await categoryService.createCategory(category.value);
      } else {
        await categoryService.updateCategory(category.value);
      }
      category.value = {name: '', id :null};
      await fetchCategories();

    };
    const deleteCategory = async (id) => {
      await categoryService.deleteCategory(id);
      await fetchCategories()
    };

    const editCategory = (selectedCategory) => {
      category.value = selectedCategory;
    };

    onMounted(fetchCategories);

    return {
      categories,
      category,
      saveCategory,
      deleteCategory,
      editCategory,
    };

  }
}


</script>

<template>
  <h1>Categories</h1>

  <hr>
  <form @submit.prevent="saveCategory">
    <input v-model="category.name" type="text" placeholder="Category Name" required>
    <button type="submit">
      {{ category.id ? 'Update' : 'Create' }}
    </button>
    <button type="reset">Cancel</button>
  </form>
  <!--
  {{category.name}}
  -->
  <hr>

  <ul>
    <li v-for="category in categories" :key="category.id">
      <div>
        <img v-bind:src="'http://localhost:3000/api/files/' + category?.file?.id + '/_stream'"
             alt="Bild"
             width="16">
        {{ category.name }}
      </div>
      <div class="actions">
        <button class="default" v-on:click="editCategory(category)">Edit</button>
        <button class="danger" v-on:click="deleteCategory(category.id)">Delete</button>
      </div>
    </li>
  </ul>
</template>


<style scoped>
h1 {
  text-align: center;
  color: #333;
}

form {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

form input {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 200px;
}

form button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

form button[type="submit"] {
  background: #007bff;
  color: #fff;
}

form button[type="submit"]:hover {
  background: #0056b3;
}

form button[type="reset"] {
  background: #6c757d;
  color: #fff;
}

form button[type="reset"]:hover {
  background: #5a6268;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-bottom: 10px;
  background: #f9f9f9;
}

li div {
  display: flex;
  align-items: center;
  gap: 10px;
}

li img {
  border-radius: 50%;
}

.actions {
  display: flex;
  gap: 10px;
}

.actions button {
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.actions .default {
  background: #007bff;
  color: #fff;
}

.actions .default:hover {
  background: #0056b3;
}

.actions .danger {
  background: #dc3545;
  color: #fff;
}

.actions .danger:hover {
  background: #c82333;
}

</style>

