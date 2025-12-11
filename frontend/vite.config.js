import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/matches': 'http://localhost:8080',
      '/players': 'http://localhost:8080',
      '/bets': 'http://localhost:8080'
    }
  }
});

