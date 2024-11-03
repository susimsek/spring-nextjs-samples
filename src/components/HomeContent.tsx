import React from 'react';
import { Spinner } from 'react-bootstrap';
import { HelloDTO } from '@/types/helloDTO';
import { useTranslation } from 'next-i18next';

type HomeContentProps = {
  messageData: HelloDTO | null;
  loading: boolean;
};

const HomeContent: React.FC<HomeContentProps> = ({ messageData, loading }) => {
  const { t } = useTranslation(['home', 'common']);

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
};

export default HomeContent;
