import { useRouter } from 'next/router';
import { useCallback } from 'react';
import i18nextConfig from '../../next-i18next.config';

export const useRedirect = () => {
  const router = useRouter();
  const currentLocale = router.query.locale || i18nextConfig.i18n.defaultLocale;

  return useCallback(
    (targetPath: string = '/') => {
      router.replace(`/${currentLocale}${targetPath}`);
    },
    [router, currentLocale],
  );
};
