<template>
    <div class="absolute-top-right">
        <q-btn flat round dense icon="more_vert">
            <q-menu fit anchor="bottom right" self="top right">
                <q-list dense>
                    <q-item clickable v-close-popup @click="edit">
                        <q-item-section avatar>
                            <q-icon name="edit" />
                        </q-item-section>
                        <q-item-section>Chỉnh sửa</q-item-section>
                    </q-item>

                    <q-item clickable v-close-popup @click="remove">
                        <q-item-section avatar>
                            <q-icon name="delete" />
                        </q-item-section>
                        <q-item-section>Xóa</q-item-section>
                    </q-item>

                    <q-item clickable v-close-popup @click="report">
                        <q-item-section avatar>
                            <q-icon name="flag" />
                        </q-item-section>
                        <q-item-section>Báo cáo</q-item-section>
                    </q-item>
                </q-list>
            </q-menu>
        </q-btn>
    </div>
</template>

<script setup>
const emit = defineEmits(["edit", "remove", "report"]);

const edit = () => {
    emit("edit");
};

const remove = () => {
    emit("remove");
};

const report = () => {
    emit("report");
};
</script>
