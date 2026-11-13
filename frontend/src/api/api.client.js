import axios from "axios";

// Backend URL. Can be changed with VUE_APP_API_URL in a .env file.
export const API_BASE_URL = process.env.VUE_APP_API_URL || 'http://localhost:3000/api';

const apiClient = axios.create({
    baseURL: API_BASE_URL,
});

/** URL of an uploaded image, or null if there is none. */
export function fileUrl(file) {
    return file?.id ? `${API_BASE_URL}/files/${file.id}/_stream` : null;
}

export default apiClient;
