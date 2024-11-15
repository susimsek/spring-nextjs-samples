/** @type {import('next').NextConfig} */

const isDev = process.env.NODE_ENV === 'development';
const isProd = process.env.NODE_ENV === 'production';

const options = {
  tls: false,
};

const sources = [
  '/api/:path*',
  '/h2-console',
  '/swagger-ui.html',
  '/swagger-ui/:path*',
  '/v3/api-docs/:path*',
  '/graphql',
  '/graphiql',
  '/subscriptions',
];

module.exports = {
  output: 'export',
  distDir: isProd ? '../app' : '.next',
  poweredByHeader: false,
  trailingSlash: true,
  images: {
    unoptimized: true,
  },
  webpack: config => {
    return config;
  },
  ...(isDev && {
    async rewrites() {
      return sources.map(source => ({
        source,
        destination: `http${options.tls ? 's' : ''}://localhost:8080${source}`,
      }));
    },
  }),
};
