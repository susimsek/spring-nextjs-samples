import React, { ReactNode } from 'react';
import Link, { LinkProps } from 'next/link';
import { useRouter } from 'next/router';

interface LinkComponentProps extends Omit<LinkProps, 'href'> { // 'href' dışında diğer LinkProps
  children: ReactNode;
  skipLocaleHandling?: boolean;
  href?: string;
}

const LinkComponent: React.FC<LinkComponentProps> = ({
                                                       children,
                                                       skipLocaleHandling = false,
                                                       href,
                                                       ...rest
                                                     }) => {
  const router = useRouter();
  const locale = (rest.locale as string) || (router.query.locale as string) || '';

  let computedHref = href || router.asPath;

  if (computedHref.startsWith('http')) {
    skipLocaleHandling = true;
  }

  if (locale && !skipLocaleHandling) {
    computedHref = computedHref ? `/${locale}${computedHref}` : router.pathname.replace('[locale]', locale);
  }

  return (
    <Link href={computedHref} legacyBehavior {...rest}>
      <a>{children}</a>
    </Link>
  );
};

export default LinkComponent;
