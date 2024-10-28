import { useEffect } from 'react';
import { useRouter } from 'next/router';
import languageDetector from './languageDetector';

type RedirectProps = {
  to?: string;
};

// Custom hook to detect language and redirect
export const useRedirect = (to?: string): JSX.Element => {
  const router = useRouter();
  const targetPath = to || router.asPath;

  // language detection and redirection logic
  useEffect(() => {
    const detectedLng = languageDetector.detect() as string;

    if (targetPath.startsWith('/' + detectedLng) && router.route === '/404') {
      // prevent endless loop for 404 error pages
      router.replace('/' + detectedLng + router.route);
      return;
    }

    // Call cache only if it is defined
    if (languageDetector.cache) {
      languageDetector.cache(detectedLng);
    }

    router.replace('/' + detectedLng + targetPath);
  }, [router, targetPath]);

  return <></>;
};

// Functional component for redirection
export const Redirect: React.FC = () => {
  useRedirect();
  return <></>;
};

// Higher-order function for specific path redirection
// eslint-disable-next-line react/display-name
export const getRedirect = (to: string): React.FC => () => {
  useRedirect(to);
  return <></>;
};
