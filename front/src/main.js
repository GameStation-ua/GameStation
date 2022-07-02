import {createApp} from 'vue'
import App from './App.vue'
import Vuesax from 'vuesax3'
import 'vuesax3/dist/vuesax.css'
import router from './router'
import './styles/global.css'
import store from './store'
import Notifications from '@kyvg/vue3-notification'
import VueSocketIO from 'vue-socket.io';

let app = createApp(App).use(store)
app.use(router)
app.use(Vuesax)
app.use(Notifications)
app.mount('#app')
app.use(VueSocketIO, 'http://localhost:8443/notifications')
