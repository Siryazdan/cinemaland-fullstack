import formDataApiClient from "@/api/form-data-api.client";


export default {
    updateFile(relId, formData) {
        return formDataApiClient.put(`/files/${relId}`, formData);
    },
    createFile(relId, formData) {
        return formDataApiClient.post(`/files/${relId}`, formData);
    }
}
