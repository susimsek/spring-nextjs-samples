// redirect.tsx
import { useEffect } from 'react';
import { useRouter } from 'next/router';
import languageDetector from './languageDetector';

// Helper function for language-based redirection
const detectAndRedirect = (router, targetPath) => {
  const detectedLng = languageDetector.detect() as string;

  if (targetPath.startsWith('/' + detectedLng) && router.route === '/404') {
    router.replace('/' + detectedLng + router.route);
    return;
  }

  if (languageDetector.cache) {
    languageDetector.cache(detectedLng);
  }

  router.replace('/' + detectedLng + targetPath);
};

// Custom hook for language-based redirection
export const useRedirect = (to?: string): JSX.Element => {
  const router = useRouter();
  const targetPath = to || router.asPath;

  useEffect(() => {
    detectAndRedirect(router, targetPath);
  }, [router, targetPath]);

  return <></>;
};

// Functional components for direct usage
export const Redirect: React.FC = () => {
  useRedirect();
  return <></>;
};


// Higher-order function for specific path redirection
export const getRedirect = (to: string): React.FC => () => {
  useRedirect(to);
  return <></>;
};
