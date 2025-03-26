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
        </div>
        <div class="text-caption text-grey">
          Created at: {{ formatDate(post.createdAt) }}
        </div>
        <q-separator class="q-my-sm" />
        <div class="text-body1">{{ post.content }}</div>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { format } from "date-fns";
import { PostServices, UserServices } from "src/services/api";

const posts = ref([]); // initialize posts as an empty array
const text = ref(''); // post content
// const ph = ref('');
const dense = ref(false);

// Call API when component is mounted to fetch posts
const fetchPosts = async () => {
  try {
    const response = await PostServices.getPosts();
    if (response.data._embedded && response.data._embedded.postDTOList) {
      posts.value = response.data._embedded.postDTOList; // extract data

      // Call API to fetch user for each post
      for (let post of posts.value) {
        if (post.userId) {
          fetchUser(post);
        }
      }
    } else {
      console.error("An error occurred while fetching posts.");
    }
  } catch (error) {
    console.error("Failed to fetch posts: ", error);
  }
};

// Call API when component is mounted to fetch user
const fetchUser = async (post) => {
  try {
    const response = await UserServices.getUserById(post.userId);
    if (response.data && response.data) {
      post.user = response.data; // extract data
    } else {
      console.error("An error occurred while fetching posts.");
    }
  } catch (error) {
    console.error("Failed to fetch user: ", error);
  }
}

// convert date to dd/MM/yyyy HH:mm:ss
const formatDate = (isoString) => {
  if (!isoString) return "N/A";
  return format(new Date(isoString), "dd/MM/yyyy HH:mm:ss");
};

// send request to create post
const submitPost = async () => {
  const token = JSON.parse(localStorage.getItem("authUser"));
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
      content: response.data.content,
      createdAt: response.data.post.createdAt,
      lastUpdated: response.data.post.lastUpdated,
      user: response.data.post.user || { firstName: "Unknown", surname: "", lastName: "" }
    });

    text.value = ""; // clear text area
  } catch (error) {
    console.error("Failed to create post:", error.response?.data || error.message);
  }
}

// Call API when component is loaded
onMounted(fetchPosts);
</script>
