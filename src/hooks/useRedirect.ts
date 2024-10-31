// hooks/useRedirect.ts
import {useRouter} from 'next/router';
import {useCallback} from 'react';

export const useRedirect = () => {
  const router = useRouter();

  return useCallback((targetPath: string = '/') => {
    const locale = (router.query.locale as string) || '';
    router.replace(`/${locale}${targetPath}`);
  }, [router]);
};
