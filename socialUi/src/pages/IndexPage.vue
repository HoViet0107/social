<template>
  <q-page class="column flex-center q-pa-md">
    <q-card class="q-pa-md q-mb-md full-width" v-for="post in posts" :key="post.id">

      <q-card-section class="flex-col w-full">
        <div class="flex flex-row items-center w-full">
          <q-avatar>
            <q-icon name="account_circle" />
          </q-avatar>
          <div class="flex-1 text-caption text-grey">{{ post.user ? post.user.firstName : "Unknown" }}&nbsp;
          </div>
          <div class="flex-1 text-caption text-grey">{{ post.user ? post.user.surname : "Unknown" }}&nbsp;</div>
          <div class="flex-1 text-caption text-grey">{{ post.user ? post.user.lastName : "Unknown" }}&nbsp;
          </div>
        </div>
        <div class="text-caption text-grey">
          Created at: {{ post.createdAt }}
        </div>
        <q-separator class="q-my-sm" />
        <div class="text-body1">{{ post.content }}</div>
      </q-card-section>

    </q-card>
  </q-page>
</template>


<script setup>
import { ref, onMounted } from "vue";
import { PostServices, UserServices } from "src/services/api";

const posts = ref([]); // initialize posts as an empty array

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

// Call API when component is loaded
onMounted(fetchPosts);
</script>
