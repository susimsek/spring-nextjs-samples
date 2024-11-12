import { Col, Container, Row } from 'react-bootstrap';
import Head from 'next/head';
import { useTranslation } from 'next-i18next';
import React, { useEffect, useState } from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';
import Sidebar from '@/components/Sidebar';
import HomeContent from '@/components/HomeContent';
import { getStaticPaths, makeStaticProps } from '@/lib/getStatic';
import EmbeddedContentFrame from '@/components/EmbeddedContentFrame';
import withAuth from '@/components/withAuth';
import { HelloDto, useGetHelloMessageQuery, useHelloSubscriptionSubscription } from '@/generated/graphql';
import { useRouter } from 'next/router';
import i18nextConfig from '../../../next-i18next.config';

const Home = () => {
  const router = useRouter();
  const { t } = useTranslation(['common', 'home']);
  const [activePage, setActivePage] = useState<string>('home');
  const [isSidebarOpen, setIsSidebarOpen] = useState<boolean>(true);

  const currentLocale = router.query.locale || i18nextConfig.i18n.defaultLocale;

  const { data, loading, refetch } = useGetHelloMessageQuery();


  // Subscription for real-time updates
  const { data: subscriptionData } = useHelloSubscriptionSubscription({
    variables: { locale: currentLocale },
  });

  // Determine which message to display: real-time or initial
  const messageData: HelloDto | null = subscriptionData?.helloSubscription || data?.hello || null;

  // Sidebar starts as open
  // Function to toggle sidebar visibility
  const toggleSidebar = () => setIsSidebarOpen(prev => !prev);

  useEffect(() => {
    if (activePage === 'home') {
      refetch();
    }
  }, [activePage, refetch]);

  const renderContent = () => {
    switch (activePage) {
      case 'home':
        return <HomeContent messageData={messageData} loading={loading} />;
      case 'api':
        return <EmbeddedContentFrame src="/swagger-ui.html" title={t('common:common.menu.api')} />;
      case 'gqlApi':
        return <EmbeddedContentFrame src="/graphiql" title={t('common:common.menu.gqlApi')} />;
      case 'database':
        return <EmbeddedContentFrame src="/h2-console" title={t('common:common.menu.database')}
                                     backgroundColor="#ffffff" />;

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

export default withAuth(Home);

const getStaticProps = makeStaticProps(['common', 'home', 'footer']);
export { getStaticPaths, getStaticProps };
