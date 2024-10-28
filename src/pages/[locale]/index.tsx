// pages/index.tsx
import {Container, Button, Spinner} from 'react-bootstrap';
import Head from 'next/head';
import { useTranslation } from 'next-i18next';
import { makeStaticProps, getStaticPaths } from '@/lib/getStatic';
import React, {useEffect, useState} from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';
import {fetchHelloMessage, HelloDTO} from "@/api/helloApi";

const Home = () => {
  const { t } = useTranslation(['common', 'home']);
  const [messageData, setMessageData] = useState<HelloDTO | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const getMessage = async () => {
      try {
        const data = await fetchHelloMessage();
        setMessageData(data);
      } catch (error) {
        console.error(error);
        setMessageData({ message: 'Failed to fetch message' });
      } finally {
        setLoading(false);
      }
    };

    getMessage();
  }, []);


  return (
    <>
      <Head>
        <title>{t('common:common.siteTitle')}</title>
      </Head>

      <Header />

      <Container className="mt-4">
        <h1>{t('home:home.heading')}</h1>
        <p>{t('home:home.description')}</p>

        <div className="my-3">
          {loading ? (
            <Spinner animation="border" role="status">
              <span className="visually-hidden">Loading...</span>
            </Spinner>
          ) : (
            <p className="text-primary" style={{fontWeight: 'bold'}}>
              {messageData?.message}
            </p>
          )}
        </div>

        <Button variant="primary" className="mb-4">
          {t('common:common:save')}
        </Button>
        <Button variant="secondary" className="ms-2 mb-4">
          {t('common:common:cancel')}
        </Button>
      </Container>

      <Footer/>
    </>
  );
};

export default Home;

const getStaticProps = makeStaticProps(['common', 'home', 'footer']);
export {getStaticPaths, getStaticProps};
