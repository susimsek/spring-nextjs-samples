// pages/404.tsx
import {Alert, Button, Container} from 'react-bootstrap';
import Head from 'next/head';
import Link from 'next/link';
import {useTranslation} from 'next-i18next';
import {getStaticPaths, makeStaticProps} from '@/lib/getStatic';
import React from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';

const Custom404 = () => {
  const {t} = useTranslation(['404', 'common']);

  return (
    <>
      <Head>
        <title>{t('404:404.title')}</title>
      </Head>

      <div className="d-flex flex-column min-vh-100">
        <Header/>

        <Container
          fluid
          className="d-flex flex-grow-1 justify-content-center align-items-center"
          style={{
            backgroundColor: "var(--background-color)"
          }}
        >
          <Alert
            variant="danger"
            className="text-center d-flex flex-column align-items-center justify-content-center"
            style={{width: '100%', maxWidth: '600px', padding: '2rem'}}
          >
            <h1 className="h4 mb-3">{t('404:404.h1')}</h1>
            <p>{t('404:404.description')}</p>
            <Link href="/" passHref>
              <Button variant="primary" type="button" className="mt-4">
                {t('common:common.back-to-home')}
              </Button>
            </Link>
          </Alert>
        </Container>

        <Footer/>
      </div>
    </>
  );
};

export default Custom404;

const getStaticProps = makeStaticProps(['404', 'common', 'footer']);
export {getStaticPaths, getStaticProps};
