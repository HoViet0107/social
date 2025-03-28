<script setup>
import { CommentServices } from 'src/services/api';
import ActionMenu from './ActionMenu.vue';
import { ref, onMounted } from 'vue';
import EditPostDialog from './EditPostDialog.vue';
import { defineProps } from 'vue';

// Define props
const props = defineProps({
    postId: {
        type: Number,
        required: true
    },
    userId: {
        type: Number,
        required: true
    }
});

// Initialize refs
const loading = ref(false);
const error = ref(null);
const comments = ref([]);
const replies = ref({});
const replyingTo = ref(null);
const newReply = ref('');
const showReplies = ref({});

const showEditDialog = ref(false);
const currentEditItem = ref(null);

// Fetch comments
const fetchComments = async () => {
    loading.value = true;
    error.value = null;
    try {
        const response = await CommentServices.getComments(props.postId);
        console.log('Raw API response:', response);

        if (Array.isArray(response)) {
            comments.value = response;
        } else if (response && Array.isArray(response.data)) {
            comments.value = response.data;
        } else {
            console.error('Unexpected response format:', response);
            comments.value = [];
            error.value = 'Received invalid data format from server';
        }

        // Initialize replies object
        comments.value.forEach(comment => {
            showReplies.value[comment.commentId] = false;
            replies.value[comment.commentId] = [];
        });
    } catch (err) {
        console.error('Error fetching comments:', err);
        error.value = 'Failed to load comments. Please try again.';
        comments.value = [];
    } finally {
        loading.value = false;
    }
};

// Toggle replies visibility
const toggleReplies = async (commentId) => {
    showReplies.value[commentId] = !showReplies.value[commentId];

    // Fetch replies if showing and not already loaded
    if (showReplies.value[commentId] && (!replies.value[commentId] || replies.value[commentId].length === 0)) {
        try {
            const response = await CommentServices.getReplies(commentId, props.postId);
            console.log('Raw API response for replies:', response);

            if (Array.isArray(response)) {
                replies.value[commentId] = response;
            } else if (response && Array.isArray(response.data)) {
                replies.value[commentId] = response.data;
            } else {
                console.error('Unexpected replies format:', response);
                replies.value[commentId] = [];
            }
        } catch (error) {
            console.error('Error fetching replies:', error);
        }
    }
};

// Toggle reply form
const toggleReplyForm = (commentId) => {
    replyingTo.value = replyingTo.value === commentId ? null : commentId;
    newReply.value = '';
};

// Add reply function
const addReply = async (commentId = null) => {
    try {
        const token = JSON.parse(localStorage.getItem('authUser'));
        if (!token || !newReply.value.trim()) {
            console.error('Token is missing or reply content is empty.');
            return;
        }
        console.log(commentId);

        const requestData = {
            postId: props.postId,
            content: newReply.value,
            parentCommentId: commentId
        };
        console.log(requestData);

        const response = await CommentServices.createComment(
            requestData,
            token
        );

        // Add the new reply to the local state
        if (!replies.value[commentId]) {
            replies.value[commentId] = [];
        }

        // Handle different response formats
        if (response && response.data) {
            replies.value[commentId].push(response.data);
        } else {
            replies.value[commentId].push(response);
        }

        // Clear the input and reset replyingTo
        newReply.value = '';
        replyingTo.value = null;

        // Make sure replies are visible
        showReplies.value[commentId] = true;
    } catch (error) {
        console.error('Error adding reply:', error);
    }
};

onMounted(async () => {
    await fetchComments();
});

// Handlers
const handleCommentEdit = (data) => {
    currentEditItem.value = data.item;
    showEditDialog.value = true;
};

// Function to save edited comment
const handleEditSave = async (updatedItem) => {
    try {
        const token = JSON.parse(localStorage.getItem('authUser'));
        if (!token) return;

        const response = await CommentServices.editComment(updatedItem, token);

        // Handle different response formats
        const updatedContent = response.data || response;

        // Update the comment in the local state
        if (updatedItem.type === 'comment') {
            const commentIndex = comments.value.findIndex(c => c.commentId === updatedItem.commentId);
            if (commentIndex !== -1) {
                // Merge the existing comment with the updated content
                comments.value[commentIndex] = {
                    ...comments.value[commentIndex],
                    ...updatedContent
                };
                // Force reactivity update
                comments.value = [...comments.value];
            }
        } else if (updatedItem.type === 'reply') {
            const parentCommentId = updatedItem.parentCommentId;
            if (replies.value[parentCommentId]) {
                const replyIndex = replies.value[parentCommentId].findIndex(
                    r => r.commentId === updatedItem.commentId
                );
                if (replyIndex !== -1) {
                    // Merge the existing reply with the updated content
                    replies.value[parentCommentId][replyIndex] = {
                        ...replies.value[parentCommentId][replyIndex],
                        ...updatedContent
                    };
                    // Force reactivity update
                    replies.value = { ...replies.value };
                }
            }
        }

        // Close the dialog
        showEditDialog.value = false;
    } catch (error) {
        console.error('Error editing comment:', error);
    }
};

const handleCommentReport = (comment) => {
    console.log('Report comment:', comment);
};

const handleReplyReport = (reply) => {
    console.log('Report reply:', reply);
};

const handleDelete = async (item) => {
    try {
        const token = JSON.parse(localStorage.getItem('authUser'));
        if (!token) return;

        // Create update object with INACTIVE status
        const updateData = {
            commentId: item.commentId,
            type: item.type // 'comment' or 'reply'
        };
        console.log('Delete item:', updateData);

        const response = await CommentServices.deleteComment(updateData, token);
        if (response && response.data) {
            console.log(response.data);
        }
        // Remove from UI based on type
        if (item.type === 'comment') {
            // Remove comment from comments array
            const index = comments.value.findIndex(c => c.commentId === item.commentId);
            if (index !== -1) {
                comments.value.splice(index, 1);
            }
        } else if (item.type === 'reply') {
            // Remove reply from replies object
            const parentCommentId = item.parentCommentId;
            if (replies.value[parentCommentId]) {
                const replyIndex = replies.value[parentCommentId].findIndex(
                    r => r.commentId === item.commentId
                );
                if (replyIndex !== -1) {
                    replies.value[parentCommentId].splice(replyIndex, 1);
                    // Force reactivity update
                    replies.value = { ...replies.value };
                }
            }
        }
    } catch (error) {
        console.error('Error deleting item:', error);
    }
};

</script>

<template>
    <div>
        <div v-if="loading" class="text-center q-pa-md">
            <q-spinner color="primary" size="2em" />
            <div>Loading comments...</div>
        </div>

        <!-- Add new comment -->
        <div v-if="!loading" class="q-pa-md">
            <q-input v-model="newReply" label="Add a comment..." dense outlined class="q-mb-md">
                <template v-slot:append>
                    <q-btn icon="send" dense flat @click="addReply(null)" />
                </template>
            </q-input>
        </div>

        <div v-if="!loading && comments.length === 0" class="text-center q-pa-md">
            No comments yet. Be the first to comment!
        </div>

        <!-- Add EditPostDialog component (only once) -->
        <EditPostDialog v-model="showEditDialog" :item="currentEditItem" :type="currentEditItem?.type || 'comment'"
            @save="handleEditSave" />

        <!-- Comments section -->
        <div v-for="comment in comments" :key="comment.commentId">
            <q-card flat bordered class="q-pa-sm">
                <q-card-section class="relative">
                    <ActionMenu :item="{ ...comment, type: 'comment' }" @edit="handleCommentEdit" @remove="handleDelete"
                        @report="handleCommentReport" />
                    <div class="text-bold">User ID: {{ comment.userId }} - Cmt ID: {{ comment.commentId }}</div>
                    <div class="text-body2">{{ comment.content || '' }}</div>

                </q-card-section>

                <!-- Reply actions -->
                <q-card-actions>
                    <q-btn flat dense size="sm" @click="toggleReplyForm(comment.commentId)">
                        {{ replyingTo === comment.commentId ? 'Cancel' : 'Reply' }}
                    </q-btn>
                    <q-btn flat dense size="sm" @click="toggleReplies(comment.commentId)">
                        {{ showReplies[comment.commentId] ? 'Hide Replies' : 'Show Replies' }}
                    </q-btn>
                </q-card-actions>

                <!-- Reply form -->
                <q-input v-if="replyingTo === comment.commentId" v-model="newReply" label="Reply..." dense outlined
                    class="q-mt-sm q-px-md">
                    <template v-slot:append>
                        <q-btn icon="send" dense flat @click="addReply(comment.commentId)" />
                    </template>
                </q-input>

                <!-- Replies section -->
                <div v-if="showReplies[comment.commentId]" class="q-ml-md q-mt-sm">
                    <div v-for="reply in replies[comment.commentId]" :key="reply.commentId" class="q-mb-md">
                        <q-card flat bordered class="q-pa-sm">
                            <q-card-section class="relative">
                                <ActionMenu :item="{ ...reply, type: 'reply', parentCommentId: comment.commentId }"
                                    @edit="handleCommentEdit" @remove="handleDelete" @report="handleReplyReport" />
                                <div class="text-bold">User ID: {{ reply.userId }} - Reply ID: {{ reply.commentId }}
                                </div>

                                <div class="text-body2">{{ reply.content || '' }}</div>
                            </q-card-section>
                        </q-card>
                    </div>
                </div>
            </q-card>
        </div>
    </div>
</template>


<style scoped>
.relative {
    position: relative;
}
</style>
