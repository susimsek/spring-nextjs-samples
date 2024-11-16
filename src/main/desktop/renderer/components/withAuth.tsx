// components/withAuth.tsx
import React, { useEffect } from 'react';
import { useAppSelector } from '../config/store';
import { Spinner } from 'react-bootstrap';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';

const withAuth = (WrappedComponent: React.ComponentType) => {
  const AuthHOC: React.FC = props => {
    const {
      i18n: { language: locale },
    } = useTranslation();
    const router = useRouter();
    const { isAuthenticated } = useAppSelector(state => state.authentication);

    useEffect(() => {
      if (!isAuthenticated) {
        console.log(isAuthenticated);
        router.push(`/${locale}/login`);
      }
    }, [isAuthenticated, router, locale]);

    if (!isAuthenticated) {
      return (
        <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
          <Spinner animation="border" role="status" />
        </div>
      );
    }

    return <WrappedComponent {...props} />;
  };

  return AuthHOC;
};

export default withAuth;
