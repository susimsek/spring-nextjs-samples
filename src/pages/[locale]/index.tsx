// pages/index.tsx
import {Container, Button, Spinner, Alert} from 'react-bootstrap';
import Head from 'next/head';
import { useTranslation } from 'next-i18next';
import { makeStaticProps, getStaticPaths } from '@/lib/getStatic';
import React, {useEffect, useState} from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';
import {fetchHelloMessage} from "@/api/helloApi";
import {HelloDTO} from "@/types/helloDTO";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faSave, faTimes} from "@fortawesome/free-solid-svg-icons";
import withAuth from "@/components/withAuth";

const Home = () => {
  const { t } = useTranslation(['common', 'home']);
  const [messageData, setMessageData] = useState<HelloDTO | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [showAlert, setShowAlert] = useState<string | null>(null); // Alert için state

  useEffect(() => {
    const getMessage = async () => {
      try {
        const data = await fetchHelloMessage();
        setMessageData(data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    getMessage();
  }, []);

  const handleSave = () => {
    setShowAlert(t('common:common.saveSuccess'));
  };

  const handleCancel = () => {
    setShowAlert(t('common:common.cancelAction'));
  };

  return (
    <>
      <Head>
        <title>{t('common:common.siteTitle')}</title>
      </Head>

      <Header />

      <Container className="mt-4 mb-4">
        <h1>
          {t('home:home.heading')}
        </h1>
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

        {showAlert && (
          <Alert variant="info" onClose={() => setShowAlert(null)} dismissible>
            {showAlert}
          </Alert>
        )}

        <div className="d-flex gap-2 mb-4">
          <Button variant="primary" onClick={handleSave}>
            <FontAwesomeIcon icon={faSave} className="me-2"/>
            {t('common:common.save')}
          </Button>
          <Button variant="secondary" className="ms-2" onClick={handleCancel}>
            <FontAwesomeIcon icon={faTimes} className="me-2"/>
            {t('common:common.cancel')}
          </Button>
        </div>
      </Container>

      <Footer/>
    </>
  );
};

export default withAuth(Home);

const getStaticProps = makeStaticProps(['common', 'home', 'footer']);
export {getStaticPaths, getStaticProps};
