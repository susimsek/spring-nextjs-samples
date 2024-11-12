import React, { ReactNode } from 'react';
import Link, { LinkProps } from 'next/link';
import { useRouter } from 'next/router';
import classNames from 'classnames';

interface LinkComponentProps extends Omit<LinkProps, 'href'> {
  children: ReactNode;
  skipLocaleHandling?: boolean;
  href?: string;
  className?: string; // Additional custom classes
}

const LinkComponent: React.FC<LinkComponentProps> = ({
  children,
  skipLocaleHandling = false,
  href,
  className,
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
      <a className={classNames('nav-link', className)}>{children}</a>
    </Link>
  );
};

export default LinkComponent;
