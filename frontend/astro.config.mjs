import { defineConfig } from "astro/config";
import react from "@astrojs/react";

export default defineConfig({
  integrations: [react()],
  server: {
    port: 5173,
    host: true
  }
});
