import React from 'react';
import Head from 'next/head';
import { useTranslation } from 'next-i18next';

import LanguageSwitcher from '../../components/LanguageSwitcher';
import { getStaticPaths, makeStaticProps } from '../../lib/get-static';

export default function HomePage() {
  const {
    i18n: { language: locale },
    t,
  } = useTranslation('common');

  return (
    <React.Fragment>
      <Head>
        <title>{t('common:common.siteTitle')}</title>
      </Head>
      <div>
        <h1>{t('common:common.siteTitle')}</h1>
        <p>
          {t('common:common.language')}: {locale}
        </p>
        <p>⚡ Electron + Next.js ⚡</p>
      </div>
      <div>
        <LanguageSwitcher />
      </div>
    </React.Fragment>
  );
}

const getStaticProps = makeStaticProps(['common', 'home', 'footer']);
export { getStaticPaths, getStaticProps };
