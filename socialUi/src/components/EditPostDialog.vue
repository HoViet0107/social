<template>
    <q-dialog v-model="dialogVisible">
        <q-card class="q-pa-md" style="max-width: 500px;">
            <q-card-section>
                <div class="text-h6">Edit {{ editData?.type === 'post' ? 'Post' : 'Comment' }}</div>
            </q-card-section>
            <q-card-section>
                <q-input v-model="localEditData.content" autogrow label="Content" filled />
            </q-card-section>
            <q-card-actions align="right">
                <q-btn flat label="Cancel" color="negative" v-close-popup />
                <q-btn flat label="Save" color="primary" @click="saveEdit" />
            </q-card-actions>
        </q-card>
    </q-dialog>
</template>

<script setup>
import { PostServices } from "src/services/api";
import { defineProps, defineEmits, computed } from "vue";

const props = defineProps({
    modelValue: Boolean,
    editData: Object,
});

const emit = defineEmits(["update:modelValue", "save"]);

// ✅ Tạo `computed` để xử lý `v-model`
const dialogVisible = computed({
    get: () => props.modelValue,
    set: (value) => emit("update:modelValue", value),
});

// ✅ Tạo `computed` để chỉnh sửa nội dung bài viết
const localEditData = computed({
    get: () => ({ ...props.editData }), // Tạo bản sao của editData để chỉnh sửa
    set: (value) => emit("save", value), // Khi thay đổi, gửi dữ liệu lên component cha
});

const saveEdit = async () => {
    const token = JSON.parse(localStorage.getItem("authUser"));
    try {
        // Structure the data properly to match what backend expects
        const postData = {
            content: localEditData.value.content,
            post: {
                postId: localEditData.value.postId
            }
        };

        const response = await PostServices.updatePost(postData, token);
        // Pass the updated data back to parent component
        emit("save", {
            ...localEditData.value,
            content: response.data.content,
            updatedAt: response.data.updatedAt
        });
        dialogVisible.value = false;
    } catch (error) {
        console.error("Failed to update post:", error);
    }
};


</script>
