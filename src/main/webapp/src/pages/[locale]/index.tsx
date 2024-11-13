import Head from 'next/head';
import { useTranslation } from 'next-i18next';
import React, { useEffect, useState } from 'react';
import HomeContent from '@/components/HomeContent';
import { getStaticPaths, makeStaticProps } from '@/lib/getStatic';
import EmbeddedContentFrame from '@/components/EmbeddedContentFrame';
import withAuth from '@/components/withAuth';
import { HelloDto, useGetHelloMessageQuery, useHelloSubscriptionSubscription } from '@/generated/graphql';
import { useRouter } from 'next/router';
import i18nextConfig from '../../../next-i18next.config';
import Layout from '@/components/Layout';

const Home = () => {
  const router = useRouter();
  const { t } = useTranslation(['common', 'home']);
  const [activePage, setActivePage] = useState<string>('home');

  const currentLocale = router.query.locale || i18nextConfig.i18n.defaultLocale;

  const { data, loading, refetch } = useGetHelloMessageQuery();

  // Subscription for real-time updates
  const { data: subscriptionData } = useHelloSubscriptionSubscription({
    variables: { locale: currentLocale },
  });

  // Determine which message to display: real-time or initial
  const messageData: HelloDto | null = subscriptionData?.helloSubscription || data?.hello || null;

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
        return (
          <EmbeddedContentFrame src="/h2-console" title={t('common:common.menu.database')} backgroundColor="#ffffff" />
        );

      default:
        return <p>{t('home:defaultDescription')}</p>;
    }
  };

  return (
    <Layout showSidebar={true} onNavigate={(page: string) => setActivePage(page)}>
      <Head>
        <title>{t('common:common.siteTitle')}</title>
      </Head>
      {renderContent()}
    </Layout>
  );
};

export default withAuth(Home);

const getStaticProps = makeStaticProps(['common', 'home', 'footer']);
export { getStaticPaths, getStaticProps };
