import apiClient from "@/api/api.client";

// For FormData, axios sets the correct multipart Content-Type (with boundary) automatically
export default {
    updateFile(relId, formData) {
        return apiClient.put(`/files/${relId}`, formData);
    },
    createFile(relId, formData) {
        return apiClient.post(`/files/${relId}`, formData);
    }
}
