<template>
    <q-card-section>
        <!-- Form nhập comment -->
        <q-input v-model="newComment" label="Write a comment..." dense outlined class="q-mb-md">
            <template v-slot:append>
                <q-btn icon="send" dense flat @click="addComment" />
            </template>
        </q-input>

        <!-- Danh sách comment -->
        <div v-for="comment in comments" :key="comment.commentId" class="q-mb-md">
            <q-card flat bordered class="q-pa-sm">
                <q-card-section>
                    <div class="text-bold">User ID: {{ comment.userId }} - Cmt ID: {{ comment.commentId }}</div>
                    <div class="text-body2">{{ comment.content }}</div>
                </q-card-section>

                <q-separator />

                <!-- Like, Reply -->
                <div class="row justify-between q-gutter-sm">
                    <q-btn flat dense size="sm" icon="thumb_up" />
                    <q-btn flat dense size="sm" icon="reply" @click="toggleReply(comment.commentId)" />
                    <q-btn flat dense size="sm" icon="expand_more" v-if="!showReplies[comment.commentId]"
                        @click="fetchReplies(comment.commentId)"> Show Replies </q-btn>
                    <q-btn flat dense size="sm" icon="expand_less" v-if="showReplies[comment.commentId]"
                        @click="toggleReplyVisibility(comment.commentId)"> Hide Replies </q-btn>
                </div>

                <!-- Form reply -->
                <q-input v-if="replyingTo === comment.commentId" v-model="newReply" label="Reply..." dense outlined
                    class="q-mt-sm">
                    <template v-slot:append>
                        <q-btn icon="send" dense flat @click="addReply(comment.commentId)" />
                    </template>
                </q-input>

                <!-- Show reply -->
                <div v-if="showReplies[comment.commentId]" class="q-ml-md q-mt-sm">
                    <div v-for="reply in replies[comment.commentId]" :key="reply.commentId" class="q-mb-md">
                        <q-card flat bordered class="q-pa-sm">
                            <q-card-section>
                                <div class="text-bold">
                                    <span class="text-grey">Reply ID:</span> {{ reply.commentId }} -
                                    <span class="text-grey">Reply to:</span> {{ reply.parentCommentId }}
                                </div>
                                <div class="text-body2">{{ reply.content }}</div>
                            </q-card-section>
                        </q-card>
                    </div>
                </div>
            </q-card>
        </div>
    </q-card-section>
</template>

<script setup>
import { ref, defineProps, onMounted } from "vue";
import { CommentServices } from "src/services/api";

const props = defineProps({ postId: Number, userid: Number });

const comments = ref([]);
const replies = ref({});
const showReplies = ref({});
const replyingTo = ref(null);
const newComment = ref("");
const newReply = ref("");

const fetchComments = async () => {
    try {
        const response = await CommentServices.getComments(props.postId);
        if (response.data == []) {
            throw new Error(`No comments found for this post: ${props.postId}!`);
        }
        comments.value = response.data;
    } catch (error) {
        console.error("Failed to fetch comments:", error);
    }
};

const fetchReplies = async (parentCommentId) => {
    try {
        const response = await CommentServices.getReplies(parentCommentId, props.postId);
        if (response.data == []) {
            throw new Error(`No replies found for this comment: ${parentCommentId}!`);
        }

        // Cập nhật danh sách reply chỉ cho comment cha cụ thể
        replies.value = { ...replies.value, [parentCommentId]: response.data };

        // // Cập nhật trạng thái show/hide đúng cách
        showReplies.value = { ...showReplies.value, [parentCommentId]: true };
        console.log(showReplies.value);

    } catch (error) {
        console.error("Failed to fetch replies:", error);
    }
};


const toggleReplyVisibility = (parentCommentId) => {
    showReplies.value[parentCommentId] = !showReplies.value[parentCommentId];
};

const toggleReply = (commentId) => {
    replyingTo.value = replyingTo.value === commentId ? null : commentId;
};

const addComment = async () => {
    if (!newComment.value.trim()) return;

    const token = JSON.parse(localStorage.getItem("authUser"));
    const data = { content: newComment.value, postId: props.postId, parentCommentId: 0 };
    console.log(data);

    try {
        const response = await CommentServices.createComment(data, token);
        comments.value.unshift(response.data);
        newComment.value = "";
    } catch (error) {
        console.error("Failed to create comment:", error);
    }
};

const addReply = async (parentCommentId) => {
    if (!newReply.value.trim()) return;

    const token = JSON.parse(localStorage.getItem("authUser"));
    const data = {
        content: newReply.value,
        postId: props.postId,
        userId: props.userid,
        parentCommentId: parentCommentId
    };

    try {
        const response = await CommentServices.createComment(data, token);

        // Đảm bảo replies của comment cha đúng
        replies.value = {
            ...replies.value,
            [parentCommentId]: [...(replies.value[parentCommentId] || []), response.data]
        };

        // Reset ô input
        newReply.value = "";
        replyingTo.value = null;
    } catch (error) {
        console.error("Failed to create reply:", error);
    }
};


onMounted(fetchComments);
</script>
<!-- gốc -->