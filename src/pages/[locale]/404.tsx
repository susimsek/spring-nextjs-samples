// pages/404.tsx
import { Container, Button, Alert } from 'react-bootstrap';
import Head from 'next/head';
import Link from 'next/link';
import { useTranslation } from 'next-i18next';
import { makeStaticProps, getStaticPaths } from '@/lib/getStatic';
import React from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';

const Custom404 = () => {
  const { t } = useTranslation(['404', 'common']);

  return (
    <>
      <Head>
        <title>{t('404:404.title')}</title>
      </Head>

      <Header />

      <Container className="text-center my-5">
        <Alert variant="danger" className="p-3" style={{ maxWidth: '800px', margin: '0 auto' }}>
          <h1 className="h4 mb-2">{t('404:404.h1')}</h1>
          <p>{t('404:404.description')}</p>
        </Alert>

        <Link href="/" passHref>
          <Button variant="primary" type="button" className="mt-3">
            {t('common:common.back-to-home')}
          </Button>
        </Link>
      </Container>

      <Footer />
    </>
  );
};

export default Custom404;

const getStaticProps = makeStaticProps(['404', 'common', 'footer']);
export { getStaticPaths, getStaticProps };
