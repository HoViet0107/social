<template>
    <!-- <div class="flex items-center justify-center" style="max-width: 400px"> -->
    <div class="flex items-center justify-center min-h-screen">

        <q-form @submit="onSubmit" class="q-gutter-md -mt-8">
            <!-- email input filed -->
            <q-input filled v-model="user.email" label="Your Email *" hint="abc@email.com" lazy-rules
                :rules="[val => val && val.length > 0 || 'This field is required']" />

            <!-- password input filed -->
            <q-input v-model="user.password" filled :type="isPwd ? 'password' : 'text'" hint="Password with toggle">
                <template v-slot:append>
                    <q-icon :name="isPwd ? 'visibility_off' : 'visibility'" class="cursor-pointer"
                        @click="isPwd = !isPwd" />
                </template>
            </q-input>

            <div class="float-right">
                <q-btn label="" type="submit" color="primary">
                    <q-icon name="login"></q-icon>
                </q-btn>
            </div>
        </q-form>

    </div>
</template>

<script>
// import { useQuasar } from 'quasar'
import { UserServices } from 'src/services/api';
import { ref } from 'vue'

export default {
    setup() {
        // const $q = useQuasar()
        const user = ref({
            email: "",
            password: ""
        });
        const isPwd = ref(true);

        const onSubmit = async () => {
            try {
                const response = await UserServices.login(user.value); // Call login API
                if (response.data) {
                    localStorage.setItem("authUser", JSON.stringify(response.data.token)); // Save token to localStorage

                    window.location.href = '/'; // navigate to home page when login success
                }
            } catch (error) {
                console.error("Failed to login: ", error.response.data);
            }
        };

        return {
            user,
            isPwd,
            onSubmit,
        };
    }
}
</script>
