<template>
    <q-dialog v-model="dialog" persistent>
        <q-card style="min-width: 350px">
            <q-card-section>
                <div class="text-h6">{{ getDialogTitle() }}</div>
            </q-card-section>

            <q-card-section>
                <q-input v-model="content" type="textarea" label="Content" autofocus :rows="5" />
            </q-card-section>

            <q-card-actions align="right">
                <q-btn flat label="Cancel" color="primary" v-close-popup />
                <q-btn flat label="Save" color="primary" @click="saveChanges" />
            </q-card-actions>
        </q-card>
    </q-dialog>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
    modelValue: {
        type: Boolean,
        default: false
    },
    item: {
        type: Object,
        default: () => ({})
    },
    type: {
        type: String,
        default: 'post'
    }
});

const emit = defineEmits(['update:modelValue', 'save']);

const dialog = ref(props.modelValue);
const content = ref('');

// Watch for changes in the dialog prop
watch(() => props.modelValue, (newVal) => {
    dialog.value = newVal;
});

// Watch for changes in the dialog value
watch(() => dialog.value, (newVal) => {
    emit('update:modelValue', newVal);
});

// Watch for changes in the item prop
watch(() => props.item, (newVal) => {
    if (newVal) {
        content.value = newVal.content || '';
    }
}, { immediate: true });

const saveChanges = () => {
    if (!content.value.trim()) return; // return if empty content

    // Create a copy of the original item and update the content
    const updatedItem = {
        ...props.item,
        content: content.value.trim(),
        type: props.type
    };

    // Emit the save event with the updated item
    emit('save', updatedItem);

    // Reset and close the dialog
    content.value = '';
    dialog.value = false;
};



const getDialogTitle = () => {
    switch (props.type) {
        case 'post':
            return 'Edit Post';
        case 'comment':
            return 'Edit Comment';
        case 'reply':
            return 'Edit Reply';
        default:
            return 'Edit';
    }
};
</script>
