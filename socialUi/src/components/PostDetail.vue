<template>
    <q-dialog v-model="isOpen">
        <q-card class="q-pa-md full-width" style="max-width: 600px;">
            <q-card-actions align="right">
                <!-- <q-btn flat label="Close" color="primary" @click="$emit('close')" /> -->
                <q-btn flat label="Close" color="primary" @click="closeDialog" />
            </q-card-actions>
            <q-card-section>
                <div class="row items-center">
                    <q-avatar>
                        <q-icon name="account_circle" />
                    </q-avatar>
                    <div class="q-ml-sm text-grey text-caption text-weight-bold">
                        {{ post.user ? post.user.firstName : "Unknown" }}&nbsp;
                        {{ post.user ? post.user.surname : "Unknown" }}&nbsp;
                        {{ post.user ? post.user.lastName : "Unknown" }}
                    </div>
                </div>

                <div class="text-body2">{{ post.content }}</div>
                <div class="text-caption text-grey">Created at: {{ formatDate(post.createdAt) }}</div>
            </q-card-section>

            <q-separator />

            <!-- Component hiển thị comment -->
            <CommentSection :postId="post.postId" :userId="post.userId" />

        </q-card>
    </q-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, watch } from "vue";
import CommentSection from "src/components/CommentSection.vue";
import { format } from "date-fns";

const props = defineProps({
    modelValue: Boolean, // receive `v-model` from parent component
    post: Object, // receive `post` obj from parent component
});

const emit = defineEmits(["update:modelValue"]); // allow child component to emit `v-model`

const isOpen = ref(props.modelValue); // local state for dialog

//watch `modelValue` changed to update `isOpen`
watch(() => props.modelValue, (newVal) => {
    isOpen.value = newVal;
});

// Close dialog and emit event to parent
const closeDialog = () => {
    emit("update:modelValue", false); // emit `v-model` to parent
};

// convert date to dd/MM/yyyy HH:mm:ss
const formatDate = (isoString) => {
    if (!isoString) return "N/A";
    return format(new Date(isoString), "dd/MM/yyyy HH:mm:ss");
};
</script>
