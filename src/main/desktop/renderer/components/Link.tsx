import React, { ReactNode } from 'react';
import Link, { LinkProps } from 'next/link';
import classNames from 'classnames';
import { useTranslation } from 'next-i18next';

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
  const {
    i18n: { language: locale },
  } = useTranslation();

  let computedHref = href;

  if (computedHref.startsWith('http')) {
    skipLocaleHandling = true;
  }

  if (locale && !skipLocaleHandling) {
    computedHref = `/${locale}${computedHref}`;
  }

  return (
    <Link href={computedHref} legacyBehavior passHref {...rest}>
      <a className={classNames('nav-link', className)}>{children}</a>
    </Link>
  );
};

export default LinkComponent;