// pages/index.tsx
import { Container, Button } from 'react-bootstrap';
import Head from 'next/head';
import { useTranslation } from 'next-i18next';
import { makeStaticProps, getStaticPaths } from '@/lib/getStatic';
import React from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';

const Home = () => {
  const { t } = useTranslation(['common', 'home']);

  return (
    <>
      <Head>
        <title>{t('home:home.title')}</title>
      </Head>

      <Header />

      <Container className="mt-4">
        <h1>{t('home:home.heading')}</h1>
        <p>{t('home:home.description')}</p>

        <Button variant="primary" className="mb-4">
          {t('common:common:save')}
        </Button>
        <Button variant="secondary" className="ms-2 mb-4">
          {t('common:common:cancel')}
        </Button>
      </Container>

      <Footer />
    </>
  );
};

export default Home;

const getStaticProps = makeStaticProps(['common', 'home', 'footer']);
export { getStaticPaths, getStaticProps };
