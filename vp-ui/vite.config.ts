import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  define: {
    'oss.env': {
      OSS_REGION: JSON.stringify(process.env.OSS_REGION),
      OSS_ACCESS_KEY_ID: JSON.stringify(process.env.OSS_ACCESS_KEY_ID),
      OSS_ACCESS_KEY_SECRET: JSON.stringify(process.env.OSS_ACCESS_KEY_SECRET),
      OSS_BUCKET_NAME: JSON.stringify(process.env.OSS_BUCKET_NAME),
    },
  },
})
