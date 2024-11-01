const isDev = process.env.NODE_ENV === 'development';

const options = {
  tls: false,
};

const nextConfig = {
  distDir: 'target/classes/static',
  output: 'export',
  eslint: {
    ignoreDuringBuilds: true,
  },
  ...(isDev && {
    async rewrites() {
      return [
        {
          source: '/api/:path*',
          destination: `http${options.tls ? 's' : ''}://localhost:8080/api/:path*`,
        },
      ];
    },
  }),
};

module.exports = nextConfig;
