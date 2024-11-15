import { useRouter } from 'next/router';
import Link from 'next/link';
import { Button } from 'react-bootstrap';
import React, { useEffect } from 'react';

interface LanguageSwitchLinkProps {
  locale: string;
  href?: string;
}

const LanguageSwitchLink: React.FC<LanguageSwitchLinkProps> = ({ locale, href, ...rest }) => {
  const router = useRouter();

  useEffect(() => {
    window.ipc.setLocale(locale);
  }, [locale]);

  let currentHref = href || router.asPath;
  let currentPath = router.pathname;

  // Replace dynamic route segments with the correct values
  Object.keys(router.query).forEach(key => {
    if (key === 'locale') {
      currentPath = currentPath.replace(`[${key}]`, locale);
      return;
    }
    currentPath = currentPath.replace(`[${key}]`, String(router.query[key]));
  });

  // Ensure the locale is correctly added to the path
  if (locale) {
    currentHref = href ? `/${locale}${href}` : currentPath;
  }
  if (!currentHref.startsWith(`/${locale}`)) {
    currentHref = `/${locale}${currentHref}`;
  }

  const handleLocaleChange = () => {
    // Push the new locale to the URL and trigger navigation
    router.push(currentHref);
  };

  return (
    <Link href={currentHref} passHref legacyBehavior>
      <Button {...rest} variant="link" size="sm" className="button-link" onClick={handleLocaleChange}>
        {locale.toUpperCase()}
      </Button>
    </Link>
  );
};

export default LanguageSwitchLink;
