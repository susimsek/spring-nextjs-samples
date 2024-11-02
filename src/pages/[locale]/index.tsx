// pages/index.tsx
import { Container, Row, Col, Spinner, Alert } from 'react-bootstrap';
import Head from 'next/head';
import { useTranslation } from 'next-i18next';
import React, { useEffect, useState } from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';
import Sidebar from '@/components/Sidebar';
import { fetchHelloMessage } from "@/api/helloApi";
import { HelloDTO } from "@/types/helloDTO";
import {getStaticPaths, makeStaticProps} from "@/lib/getStatic";

const Home = () => {
  const { t } = useTranslation(['common', 'home']);
  const [activePage, setActivePage] = useState<string>('home');
  const [messageData, setMessageData] = useState<HelloDTO | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [showAlert, setShowAlert] = useState<string | null>(null);

  useEffect(() => {
    if (activePage === 'home') {
      const getMessage = async () => {
        setLoading(true);
        try {
          const data = await fetchHelloMessage();
          setMessageData(data);
        } catch (error) {
          console.error(error);
          setShowAlert('An error occurred while fetching the message.');
        } finally {
          setLoading(false);
        }
      };

      getMessage();
    }
  }, [activePage]);

  const renderContent = () => {
    switch (activePage) {
      case 'home':
        return (
          <div className="p-4">
            <h1>{t('home:home.heading')}</h1>
            <p style={{ color: 'var(--text-color)' }}>{t('home:home.description')}</p>

            <div className="my-3">
              {loading ? (
                <Spinner animation="border" role="status">
                  <span className="visually-hidden">{t('common:common.loading')}</span>
                </Spinner>
              ) : (
                <p className="text-primary" style={{ fontWeight: 'bold' }}>
                  {messageData?.message}
                </p>
              )}
            </div>
          </div>
        );
      case 'api':
        return (
          <iframe
            src="/swagger-ui.html"
            width="100%"
            height="600px"
            style={{
              width: '100%',
              height: '100vh',
              border: 'none',
              flexGrow: 1,
            }}
            title="API Documentation"
          />
        );
      case 'database':
        return (
          <iframe
            src="/h2-console"
            style={{
              width: '100%',
              height: '100vh',
              border: 'none',
              flexGrow: 1,
            }}
            title="Database Console"
          />
        );
      default:
        return <p>{t('home:defaultDescription')}</p>;
    }
  };

  return (
    <>
      <Head>
        <title>{t('common:common.siteTitle')}</title>
      </Head>

      <Header />

      <Container fluid className="d-flex flex-column min-vh-100 p-0">
        <Row className="flex-grow-1 g-0">
          {/* Sidebar Column */}
          <Col xs={12} md={3} className="p-0 d-flex flex-column">
            <Sidebar onNavigate={(page) => setActivePage(page)} />
          </Col>

          {/* Main Content Column */}
          <Col xs={12} md={9}>
            {renderContent()}
          </Col>
        </Row>

        <Footer />
      </Container>
    </>
  );
};

export default Home;

const getStaticProps = makeStaticProps(['common', 'home', 'footer']);
export { getStaticPaths, getStaticProps };
