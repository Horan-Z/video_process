import './assets/main.css'
import ElementPlus from 'element-plus'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

createApp(App)
  .use(ElementPlus)
  .use(router)
  .mount('#app')
