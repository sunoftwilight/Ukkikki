import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'
<<<<<<< HEAD
// import mkcert from'vite-plugin-mkcert'

// https://vitejs.dev/config/
export default defineConfig({
  // plugins: [react(), mkcert()],
  plugins: [react()],
=======
import mkcert from'vite-plugin-mkcert'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react(), mkcert()],
  // plugins: [react()],
>>>>>>> a0753ef6b4f2e4a0f1008039f10c7d9b325eb9f4
  resolve: {
    alias: [{ find: '@', replacement: '/src' }],
  },
  // server:{
  //   https: true,
  // }
})
