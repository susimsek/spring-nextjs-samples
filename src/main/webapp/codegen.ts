import type { CodegenConfig } from '@graphql-codegen/cli';

const config: CodegenConfig = {
  overwrite: true,
  schema: "http://localhost:8080/graphql",
  documents: "src/graphql/**/*.graphql",
  generates: {
    'src/generated/graphql.tsx': {
      plugins: [
        'typescript',
        'typescript-operations',
        'typescript-react-apollo'
      ],
      config: {
        withHooks: true,
        useIndexSignature: true,
      }
    },
  },
  hooks: {
    afterOneFileWrite: ["prettier --write"],
  }
};

export default config;
