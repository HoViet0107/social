<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated class="bg-primary glossy">
      <q-toolbar>
        <q-btn flat dense round icon="menu" aria-label="Menu" @click="leftDrawerOpen = !leftDrawerOpen" />

        <!-- Logo trở thành link về trang chủ -->
        <q-toolbar-title class="text-weight-bold cursor-pointer" @click="$router.push('/')">
          <q-avatar>
            <img src="https://cdn.quasar.dev/logo-v2/svg/logo-mono-white.svg">
          </q-avatar>
          Social App
        </q-toolbar-title>

        <!-- Chỉ giữ lại icon message và profile -->
        <div class="q-gutter-sm row items-center no-wrap">
          <q-btn round flat icon="notifications" to="/notifications">
            <q-badge color="red" floating>2</q-badge>
          </q-btn>
          <q-btn round flat icon="message" to="/chats">
            <q-badge color="red" floating>4</q-badge>
          </q-btn>
          <q-btn dense flat round icon="account_circle" @click="toggleRightDrawer" />
        </div>
      </q-toolbar>
    </q-header>

    <!-- right drawer -->
    <q-drawer v-model="rightDrawerOpen" side="right" bordered content-class="bg-grey-1">
      <q-list padding>
        <q-item-label header class="text-grey-8">User Menu</q-item-label>

        <q-item clickable v-ripple to="/user/profile">
          <q-item-section avatar>
            <q-icon name="account_circle" color="primary" />
          </q-item-section>
          <q-item-section>Profile</q-item-section>
        </q-item>

        <!-- Thêm mục Groups vào sidebar -->
        <q-item clickable v-ripple to="/groups">
          <q-item-section avatar>
            <q-icon name="group" color="teal" />
          </q-item-section>
          <q-item-section>Groups</q-item-section>
        </q-item>

        <q-item clickable v-ripple v-show="isAdminUser()" to="/admin/admin-page">
          <q-item-section avatar>
            <q-icon name="admin_panel_settings" color="deep-orange" />
          </q-item-section>
          <q-item-section>Admin Panel</q-item-section>
        </q-item>

        <q-item clickable v-ripple v-if="isTokenExpired()" to="/auth/login">
          <q-item-section avatar>
            <q-icon name="login" color="green" />
          </q-item-section>
          <q-item-section>Login</q-item-section>
        </q-item>

        <q-item clickable v-ripple v-else @click="logout">
          <q-item-section avatar>
            <q-icon name="logout" color="negative" />
          </q-item-section>
          <q-item-section>Logout</q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container class="bg-grey-2">
      <router-view />
    </q-page-container>

    <q-footer bordered class="bg-white text-primary">
      <q-toolbar>
        <q-toolbar-title class="text-center text-caption">
          © 2023 Social App - All Rights Reserved
        </q-toolbar-title>
      </q-toolbar>
    </q-footer>
  </q-layout>
</template>

<script setup>
import { ref } from 'vue';
import { isAdminUser, isTokenExpired } from 'src/helpers/helperFunctions';

const rightDrawerOpen = ref(false);
const leftDrawerOpen = ref(false);

const toggleRightDrawer = () => {
  rightDrawerOpen.value = !rightDrawerOpen.value;
};

// Add logout function
const logout = () => {
  localStorage.removeItem('token');
  window.location.href = '/auth/login';
};
</script>

<style lang="scss">
.q-header {
  .q-toolbar {
    height: 64px;
  }
}

.cursor-pointer {
  cursor: pointer;
}

.q-drawer {
  .q-item {
    border-radius: 8px;
    margin: 0 8px;

    &.q-router-link--active {
      background-color: #e3f2fd;
      color: #1976d2;
    }
  }
}

/* Responsive styles */
@media (max-width: 599px) {
  .q-toolbar-title {
    font-size: 1.1rem;
  }
}
</style>