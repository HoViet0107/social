<template>
  <q-page class="column flex-center">
    <q-knob
      v-model="count"
      :min="min"
      :max="max"
      size="80px"
      show-value
      :thickness="0.13"
      color="primary"
      :track-color="$q.dark.isActive ? 'grey-8' : 'dark'"
    >
      <q-avatar size="75px">
        <img alt="Quasar logo" :src="innerLogo" />
      </q-avatar>
    </q-knob>

    <img
      alt="Quasar logo"
      :src="verticalLogo"
      style="width: 200px; height: 140px"
    />

    <div class="q-mt-xl">
      <q-btn
        color="primary"
        dense
        round
        label="-"
        :disable="count === min"
        @click="count--"
      />

      <span class="q-mx-md text-bold">{{ count }}</span>

      <q-btn
        color="primary"
        dense
        round
        label="+"
        :disable="count === max"
        @click="count++"
      />
    </div>

    <div class="q-mt-md" style="width: 200px">
      <q-slider v-model="count" :min="min" :max="max" />
    </div>

    <div class="q-mt-md">
      <q-toggle
        :model-value="$q.dark.isActive"
        :checked-icon="symRoundedDarkMode"
        :unchecked-icon="symRoundedLightMode"
        size="xl"
        @update:model-value="$q.dark.toggle()"
      />
    </div>
  </q-page>
</template>

<script setup>
import {
  symRoundedDarkMode,
  symRoundedLightMode,
} from '@quasar/extras/material-symbols-rounded';
import { Dark } from 'quasar';
import { computed, ref } from 'vue';

import innerLogoDark from 'assets/quasar-logo-inner-dark.svg';
import innerLogoLight from 'assets/quasar-logo-inner-light.svg';
import verticalLogoDark from 'assets/quasar-logo-vertical-dark.svg';
import verticalLogoLight from 'assets/quasar-logo-vertical-light.svg';

const innerLogo = computed(() =>
  Dark.isActive ? innerLogoDark : innerLogoLight,
);

const verticalLogo = computed(() =>
  Dark.isActive ? verticalLogoDark : verticalLogoLight,
);

const count = ref(0);

const min = -5;
const max = 5;
</script>
