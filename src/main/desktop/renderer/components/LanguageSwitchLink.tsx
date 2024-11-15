import { useRouter } from 'next/router';
import Link from 'next/link';
import { Button } from 'react-bootstrap';
import React from 'react';

interface LanguageSwitchLinkProps {
  locale: string;
  href?: string;
}

const LanguageSwitchLink: React.FC<LanguageSwitchLinkProps> = ({ locale, href, ...rest }) => {
  const router = useRouter();

  const currentPath = router.pathname;
  const currentHref = currentPath.replace('[locale]', locale);

  const handleLocaleChange = () => {
    // Push the new locale to the URL and trigger navigation
    window.ipc.setLocale(locale);
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
