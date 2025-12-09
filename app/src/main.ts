import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";
import { useUserStore } from "./stores/user";
import "./styles.css";
import { useTodosStore } from "./stores/todos";

const app = createApp(App);

const pinia = createPinia();
app.use(pinia);
app.use(router);

try {
  const userStore = useUserStore();
  userStore.init();
  const todosStore = useTodosStore();
  todosStore.init();
} catch (e) {
  console.error("Erro inicialização stores:", e);
}

app.mount("#app");
