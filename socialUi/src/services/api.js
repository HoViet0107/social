import axios from "axios";

const API_URL = "http://localhost:8080/api";

export const api = axios.create({
    baseURL: API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

export const UserServices = {
    login(data) {
        return api.post(`/auth/login`, data);
    },
    register(data) {
        return api.post(`/auth/register`, data);
    },
    getUserById(userId) {
        return api.get(`/user/${userId}`);
    }
};

export const PostServices = {
    getPosts() {
        return api.get(`/posts`);
    },
    getPostById(postId) {
        return api.get(`/posts/${postId}`);
    },
    createPost(data, token) {
        return api.post(`/posts`, data, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
    },
    updatePost(data) {
        return api.put(`/posts`, data);
    },
    deletePost(postId) {
        return api.put(`/posts/${postId}/status`);
    }
};


