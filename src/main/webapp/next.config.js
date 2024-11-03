const path = require('path');

const isDev = process.env.NODE_ENV === 'development';

const options = {
  tls: false,
};

const sources = [
  '/api/:path*',
  '/h2-console',
  '/swagger-ui.html',
  '/swagger-ui/:path*',
  '/v3/api-docs/:path*'];

const nextConfig = {
  distDir: 'build',
  output: 'export',
  eslint: {
    ignoreDuringBuilds: true,
  },
  ...(isDev && {
    async rewrites() {
      return sources.map((source) => ({
        source,
        destination: `http${options.tls ? 's' : ''}://localhost:8080${source}`,
      }));
    },
  }),
};

module.exports = nextConfig;
