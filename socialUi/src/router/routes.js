const routes = [
  {
    path: '/',
    component: () => import('src/layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('src/pages/IndexPage.vue') }],
  },
  {
    path: '/group',
    component: () => import('src/layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('src/pages/GroupPage.vue') }],
  },
  {
    path: '/chat',
    component: () => import('src/layouts/MainLayout.vue'),
    children: [{ path: '', component: () => import('src/pages/ChatsPage.vue') }],
  },

  //
  {
    path: '/:catchAll(.*)*',
    component: () => import('src/pages/ErrorNotFound.vue'),
  },
];

export default routes;
