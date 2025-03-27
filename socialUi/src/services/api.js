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
    updatePost(data, token) {
        return api.put(`/posts`, data, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
    },
    deletePost(postId, token) {
        return api.put(`/posts/${postId}/status`, {}, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
    }
};

export const CommentServices = {
    getComments(postId) {
        return api.get(`/comments/${postId}?pageNumber=1&pageSize=10`);
    },
    getReplies(parentCommentId, postId) {
        return api.get(`/comments/reply/${parentCommentId}?postId=${postId}&pageNumber=1&pageSize=5`);
    },
    createComment(data, token) {
        return api.post(`/comments`, data, {
            headers: { Authorization: `Bearer ${token}` }
        });
    }
};


