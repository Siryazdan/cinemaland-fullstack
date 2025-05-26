import apiClient from './api.client';

export default {
    getCategories() {
        return apiClient.get('/categories/');
    },
    createCategory(category) {
        return apiClient.post('/categories/', category);
    },
    deleteCategory(categoryId) {
        return apiClient.delete(`/categories/${categoryId}`);
    },
    updateCategory(category) {
        return apiClient.put(`/categories/${category.id}`, category);
    },
}