import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'
import mkcert from'vite-plugin-mkcert'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react(), mkcert()],
  // plugins: [react()],
  resolve: {
    alias: [{ find: '@', replacement: '/src' }],
  },
  server:{
    https: true,
  }
})
