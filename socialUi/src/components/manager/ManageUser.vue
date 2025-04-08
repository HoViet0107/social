<template>
    <div>
        <div class="row justify-between items-center q-mb-md">
            <div class="text-h5">Manage Users</div>
            <q-btn label="Refresh" icon="refresh" color="primary" @click="fetchUsers" />
        </div>

        <q-table :rows="users" :columns="columns" row-key="userId" flat bordered dense>
            <template v-slot:body-cell-actions="props">
                <q-btn flat dense icon="edit" color="primary" @click="editUser(props.row)" />
                <q-btn flat dense icon="delete" color="negative" @click="deleteUser(props.row)" />
            </template>
        </q-table>
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { UserServices } from "src/services/api";

const users = ref([]);
const columns = [
    { name: "userId", label: "ID", align: "left", field: "userId" },
    { name: "email", label: "Email", align: "left", field: "email" },
    { name: "firstName", label: "First Name", align: "left", field: "firstName" },
    { name: "lastName", label: "Last Name", align: "left", field: "lastName" },
    { name: "actions", label: "Actions", align: "center" }
];

const fetchUsers = async () => {
    try {
        const response = await UserServices.getUsers();
        users.value = response.data;
    } catch (error) {
        console.error("Error fetching users:", error);
    }
};

const editUser = (user) => {
    console.log("Editing user:", user);
};

const deleteUser = async (user) => {
    if (confirm(`Are you sure you want to delete user ID ${user.userId}?`)) {
        try {
            await UserServices.deleteUser(user.userId);
            users.value = users.value.filter(u => u.userId !== user.userId);
        } catch (error) {
            console.error("Error deleting user:", error);
        }
    }
};

onMounted(fetchUsers);
</script>