<template>
  <q-page class="column flex-center full-height q-pa-md">
    <!-- Input form -->
    <q-input bottom-slots v-model="text" autogrow counter maxlength="5000" :dense="dense"
      class="full-width q-mx-md q-pa-sm q-shadow-2 q-rounded-borders" style="max-width: 600px;">

      <template v-slot:before>
        <q-avatar>
          <img src="https://cdn.quasar.dev/img/avatar5.jpg">
        </q-avatar>
      </template>

      <template v-slot:append>
        <q-icon v-if="text !== ''" name="close" @click="text = ''" class="cursor-pointer" />
      </template>

      <template v-slot:after>
        <q-btn round dense flat icon="send" @click="submitPost" />
      </template>
    </q-input>

    <!-- Posts -->
    <q-card class="q-pa-md q-mb-md q-mx-auto q-mt-md  full-width" v-for="post in posts" :key="post.id"
      style="max-width: 600px !important;">
      <q-card-section class="column full-width">
        <div class="row items-center full-width">
          <q-avatar>
            <q-icon name="account_circle" />
          </q-avatar>
          <div class="text-caption text-weight-bold">
            {{ post.user ? post.user.firstName : "Unknown" }}&nbsp;
          </div>
          <div class="text-caption text-weight-bold">
            {{ post.user ? post.user.surname : "Unknown" }}&nbsp;
          </div>
          <div class="text-caption text-weight-bold">
            {{ post.user ? post.user.lastName : "Unknown" }}
          </div>
          <!-- action menu -->
          <ActionMenu class="float-right" :item="post" @edit="openEditDialog({ item: post, type: post.type })"
            @remove="handleDelete" @report="handleReport" />

        </div>
        <div class="text-caption text-grey">
          Created at: {{ formatDate(post.createdAt) }}
        </div>

        <q-separator class="q-my-sm" />
        <div class="text-body1">{{ post.content }}</div>

        <!-- post like, comment -->
        <q-separator class="q-my-sm" />
        <div class="row full-width justify-between items-center q-gutter-md">
          <q-btn flat round dense icon="favorite_border" size="md" />
          <q-btn flat round dense icon="mode_comment" size="md" @click="openPost(post)" />
          <q-btn flat round dense icon=" share" size="md" />
        </div>

      </q-card-section>
    </q-card>

    <!-- Post detail Dialog -->
    <!-- <PostDetail v-if="selectedPost" v-model="isPostOpen" :post="selectedPost" @close="isPostOpen = false" /> -->
    <PostDetail v-model="isPostOpen" :post="selectedPost" />

    <!-- Edit post Dialog -->
    <!-- <EditPostDialog v-model="isEditDialogOpen" :editData="editData" @save="updateContent" /> -->
    <EditPostDialog v-model="isEditDialogOpen" :item="editData" :type="editData?.type || 'post'"
      @save="updateContent" />
  </q-page>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { format } from "date-fns";
import { CommentServices, PostServices, UserServices } from "src/services/api";
import PostDetail from "src/components/PostDetail.vue";
import ActionMenu from "src/components/ActionMenu.vue";
import EditPostDialog from "src/components/EditPostDialog.vue";

// State variables
const posts = ref([]);
const text = ref('');
const dense = ref(false);
const selectedPost = ref(null);
const isPostOpen = ref(false);
const isEditDialogOpen = ref(false);
const editData = ref(null);

// Helper function to get auth token
const getAuthToken = () => {
  try {
    return JSON.parse(localStorage.getItem("authUser"));
  } catch (error) {
    console.error("Error parsing authUser:", error);
    return null;
  }
};

// Call API when component is mounted to fetch posts
const fetchPosts = async () => {
  try {
    const response = await PostServices.getPosts();
    if (response.data._embedded && response.data._embedded.postDTOList) {
      posts.value = response.data._embedded.postDTOList;

      // Call API to fetch user for each post
      for (let post of posts.value) {
        if (post.userId) {
          post.type = 'post'
          fetchUser(post);
        }
      }
    }
  } catch (error) {
    console.error("Failed to fetch posts: ", error);
    // Handle error appropriately
  }
};

// Call API to fetch user
const fetchUser = async (post) => {
  try {
    const response = await UserServices.getUserById(post.userId);
    if (response.data) {
      post.user = response.data;
    }
  } catch (error) {
    console.error("Failed to fetch user: ", error);
  }
}

// Format date
const formatDate = (isoString) => {
  if (!isoString) return "N/A";
  return format(new Date(isoString), "dd/MM/yyyy HH:mm:ss");
};

// Send request to create post
const submitPost = async () => {
  const token = getAuthToken();
  if (!token) {
    console.error("Token not found!");
    return;
  }

  if (!text.value.trim()) {
    console.error("Post content must not be empty!");
    return;
  }

  const postData = {
    content: text.value,
    post: {
      status: "ACTIVE"
    }
  };

  // send request
  try {
    const response = await PostServices.createPost(postData, token);

    posts.value.unshift({
      postId: response.data.post.postId,
      content: response.data.content,
      createdAt: response.data.post.createdAt,
      lastUpdated: response.data.post.lastUpdated,
      userId: response.data.post.userId,
      type: 'post',
      user: response.data.post.user || { firstName: "Unknown", surname: "", lastName: "" }
    });

    text.value = ""; // clear text area
  } catch (error) {
    console.error("Failed to create post:", error.response?.data || error.message);
  }
}

// Open post detail dialog
const openPost = (post) => {
  selectedPost.value = post;
  isPostOpen.value = true;
};

// Open edit dialog
const openEditDialog = ({ item, type }) => {
  editData.value = { ...item, type }; // Create a copy to avoid reference issues
  isEditDialogOpen.value = true;
};

// Update content (post or comment)
const updateContent = async (updatedData) => {
  try {
    const token = getAuthToken();
    if (!token) {
      console.error('No authentication token found');
      return;
    }

    if (updatedData.type === "post") {
      // Get postId directly from updatedData which now contains the full item
      const postId = updatedData.postId
      const requestBody = {
        content: updatedData.content,
        post: {
          postId: postId
        }
      };

      const response = await PostServices.updatePost(requestBody, token);

      // Update local state
      const postIndex = posts.value.findIndex(post => post.postId === postId);
      if (postIndex !== -1) {
        // Update the content in the local posts array
        posts.value[postIndex].content = updatedData.content;
        if (response && response.data && response.data.lastUpdated) {
          posts.value[postIndex].lastUpdated = response.data.lastUpdated;
        }
      }

      // Update selected post if needed
      if (selectedPost.value && selectedPost.value.postId === postId) {
        selectedPost.value.content = updatedData.content;
        if (response && response.data && response.data.lastUpdated) {
          selectedPost.value.lastUpdated = response.data.lastUpdated;
        }
      }
    }
    else if (updatedData.type === "comment") {
      const commentId = updatedData.commentId;
      if (!commentId) {
        console.error('No comment ID found in the data:', updatedData);
        return;
      }

      // This method doesn't exist in your API service
      // You need to implement CommentServices.editComment instead
      await CommentServices.editComment({
        commentId: commentId,
        content: updatedData.content
      }, token);
      
      // You should update the UI for comments here
    }
  } catch (error) {
    console.error('Error updating content:', error);
  } finally {
    isEditDialogOpen.value = false;
  }
};


// delete post or comment
const handleDelete = async (post) => {
  // Determine the type from the post object
  const type = post.type || 'post';

  if (confirm(`Are you sure you want to delete this ${type}?`)) {
    try {
      const token = getAuthToken();
      if (!token) {
        console.error('No authentication token found');
        return;
      }

      if (type === "post") {
        await PostServices.deletePost(post.postId, token);
        // Remove the post from the local array
        posts.value = posts.value.filter(p => p.postId !== post.postId);
      } else if (type === "comment") {
        // Implement comment deletion with proper UI update
        await CommentServices.deleteComment({
          commentId: post.commentId
        }, token);
        // Update UI for comments
      }
    } catch (error) {
      console.error("Failed to delete:", error);
    }
  }
};


// report post or comment
const handleReport = ({ item, type }) => {
  console.log(`${type === "post" ? "Post" : "Comment"} Reported:`, item);
};

// Call API when component is loaded
onMounted(fetchPosts);
</script>
