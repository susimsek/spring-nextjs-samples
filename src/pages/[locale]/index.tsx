import { Container, Row, Col, Spinner, Alert } from 'react-bootstrap';
import Head from 'next/head';
import { useTranslation } from 'next-i18next';
import React, { useEffect, useState } from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';
import Sidebar from '@/components/Sidebar';
import HomeContent from '@/components/HomeContent';
import { fetchHelloMessage } from "@/api/helloApi";
import { HelloDTO } from "@/types/helloDTO";
import { getStaticPaths, makeStaticProps } from "@/lib/getStatic";
import EmbeddedContentFrame from "@/components/EmbeddedContentFrame";

const Home = () => {
  const { t } = useTranslation(['common', 'home']);
  const [activePage, setActivePage] = useState<string>('home');
  const [messageData, setMessageData] = useState<HelloDTO | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [showAlert, setShowAlert] = useState<string | null>(null);
  const [isSidebarOpen, setIsSidebarOpen] = useState<boolean>(true);  // Sidebar starts as open

  // Function to toggle sidebar visibility
  const toggleSidebar = () => setIsSidebarOpen(prev => !prev);

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
        return <HomeContent messageData={messageData} loading={loading} />;
      case 'api':
        return <EmbeddedContentFrame src="/swagger-ui.html" title={t('common:common.menu.api')} />;
      case 'database':
        return <EmbeddedContentFrame src="/h2-console" title={t('common:common.menu.database')} backgroundColor="#ffffff" />;

      default:
        return <p>{t('home:defaultDescription')}</p>;
    }
  };

  return (
    <>
      <Head>
        <title>{t('common:common.siteTitle')}</title>
      </Head>

      <Header
        onToggleSidebar={toggleSidebar}       // Pass toggle function to Header
        isSidebarOpen={isSidebarOpen}          // Sidebar open state
        showSidebarToggle={true}               // Show toggle switch in Header
      />

      <Container fluid className="d-flex flex-column min-vh-100 p-0">
        <Row className="flex-grow-1 g-0">
          {/* Sidebar Column - visible only if isSidebarOpen is true */}
          {isSidebarOpen && (
            <Col xs={12} md={3} lg={2} className="p-0 d-flex flex-column">
              <Sidebar onNavigate={(page) => setActivePage(page)} />
            </Col>
          )}

          {/* Main Content Column */}
          <Col xs={12} md={isSidebarOpen ? 9 : 12} lg={isSidebarOpen ? 10 : 12}>
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
