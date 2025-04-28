<template>
  <q-page padding>
    <div class="row justify-center">
      <div class="col-12 col-md-8 col-lg-6">
        <q-card flat bordered>
          <q-card-section>
            <div class="text-h5">User Profile</div>
          </q-card-section>

          <q-card-section v-if="loading">
            <div class="row justify-center">
              <q-spinner color="primary" size="3em" />
            </div>
          </q-card-section>

          <template v-else-if="user">
            <q-card-section>
              <div class="row q-col-gutter-md">
                <div class="col-12">
                  <div class="text-h6">{{ fullName }}</div>
                </div>
                
                <div class="col-12 col-md-6">
                  <q-item>
                    <q-item-section avatar>
                      <q-icon name="email" color="primary" />
                    </q-item-section>
                    <q-item-section>
                      <q-item-label caption>Email</q-item-label>
                      <q-item-label>{{ user.email || 'Not provided' }}</q-item-label>
                    </q-item-section>
                  </q-item>
                </div>

                <div class="col-12 col-md-6">
                  <q-item>
                    <q-item-section avatar>
                      <q-icon name="phone" color="primary" />
                    </q-item-section>
                    <q-item-section>
                      <q-item-label caption>Phone</q-item-label>
                      <q-item-label>{{ user.phone || 'Not provided' }}</q-item-label>
                    </q-item-section>
                  </q-item>
                </div>
                
                <div class="col-12 col-md-6">
                  <q-item>
                    <q-item-section avatar>
                      <q-icon name="cake" color="primary" />
                    </q-item-section>
                    <q-item-section>
                      <q-item-label caption>Date of Birth</q-item-label>
                      <q-item-label>{{ formatDate(user.dob) || 'Not provided' }}</q-item-label>
                    </q-item-section>
                  </q-item>
                </div>
                
                <div class="col-12 col-md-6">
                  <q-item>
                    <q-item-section avatar>
                      <q-icon name="event" color="primary" />
                    </q-item-section>
                    <q-item-section>
                      <q-item-label caption>Member Since</q-item-label>
                      <q-item-label>{{ formatDate(user.createdAt) }}</q-item-label>
                    </q-item-section>
                  </q-item>
                </div>
              </div>
            </q-card-section>

            <q-card-actions align="right">
              <q-btn 
                color="primary" 
                label="Edit Profile" 
                @click="openEditDialog" 
              />
            </q-card-actions>
          </template>

          <q-card-section v-else>
            <div class="text-center">
              <p>User not found or you need to log in.</p>
              <q-btn to="/auth/login" color="primary" label="Login" />
            </div>
          </q-card-section>
        </q-card>
      </div>
    </div>

    <!-- Edit Profile Dialog -->
    <EditProfileDialog 
      v-model="showEditDialog" 
      :userData="user" 
      @saved="onProfileUpdated" 
    />
  </q-page>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { format } from 'date-fns';
import { UserServices } from 'src/services/api';
import EditProfileDialog from 'src/components/EditProfileDialog.vue';

const user = ref(null);
const loading = ref(true);
const showEditDialog = ref(false);

// Computed property for full name
const fullName = computed(() => {
  if (!user.value) return '';
  return [
    user.value.firstName || '',
    user.value.surname || '',
    user.value.lastName || ''
  ].filter(Boolean).join(' ');
});

// Helper function to get auth token
const getAuthToken = () => {
  try {
    return JSON.parse(localStorage.getItem("authUser"));
  } catch (error) {
    console.error("Error parsing authUser:", error);
    return null;
  }
};

// Format date for display
function formatDate(dateString) {
  if (!dateString) return '';
  try {
    return format(new Date(dateString), 'MMMM dd, yyyy');
  } catch (e) {
    console.error('Error formatting date:', e);
    return dateString;
  }
}

// Get current user data
async function fetchUserData() {
  try {
    loading.value = true;    
    // Get user details from token or another API call
    const response = await UserServices.getUserByEmail(getAuthToken());
    user.value = response.data;
  } catch (error) {
    console.error('Error fetching user data:', error);
  } finally {
    loading.value = false;
  }
}

// Open edit dialog
function openEditDialog() {
  showEditDialog.value = true;
}

// Handle profile update
function onProfileUpdated(updatedUser) {
  user.value = updatedUser;
}

// Fetch user data on component mount
onMounted(fetchUserData);
</script>