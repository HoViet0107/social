import axios from "axios";

const API_URL = "http://localhost:8080/api";

export const api = axios.create({
    baseURL: API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

const authConfig = (token) => ({
    headers: { Authorization: `Bearer ${token}` }
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
    },
    getUserByEmail(token) {
        return api.get(`/user/user-details`, authConfig(token));
    },
    editUser(data, token) {
        return api.put(`/user/${data.userId}`, data, authConfig(token));
    },
};

export const PostServices = {
    getPosts() {
        return api.get(`/posts`);
    },
    getPostById(postId) {
        return api.get(`/posts/${postId}`);
    },
    createPost(data, token) {
        return api.post(`/posts`, data, authConfig(token));
    },
    updatePost(data, token) {
        return api.put(`/posts`, data, authConfig(token));
    },
    deletePost(postId, token) {
        return api.put(`/posts/${postId}/status`, {}, authConfig(token));
    }
};

export const CommentServices = {
    getComments(postId) {
        return api.get(`/comments/${postId}?pageNumber=1&pageSize=10`)
    },
    getReplies(parentCommentId, postId) {
        return api.get(`/comments/reply/${parentCommentId}?postId=${postId}&pageNumber=1&pageSize=5`);
    },
    createComment(data, token) {
        return api.post(`/comments`, data, {
            headers: { Authorization: `Bearer ${token}` }
        });
    },
    editComment(data, token) {
        return api.put(`/comments/${data.commentId}`, data, {
            headers: { Authorization: `Bearer ${token}` }
        });
    },
    deleteComment(data, token) {
        return api.put(`/comments/${data.commentId}/delete`, data, {
            headers: { Authorization: `Bearer ${token}` }
        });
    },
};

export const UserReactionServices = {
    userReaction(data, token) {
        return api.put(`/user-reaction`, data, authConfig(token));
    },
}
