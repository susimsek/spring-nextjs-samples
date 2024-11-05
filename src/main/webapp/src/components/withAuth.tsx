// components/withAuth.tsx
import React, { useEffect } from 'react';
import { useAppSelector } from '@/config/store'; // assuming useAppSelector is set up in your store
import { Spinner } from 'react-bootstrap';
import { useRedirect } from '@/hooks/useRedirect';

const withAuth = (WrappedComponent: React.ComponentType) => {
  const AuthHOC: React.FC = (props) => {
    const redirect = useRedirect();
    const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);

    useEffect(() => {
      if (!isAuthenticated) {
        redirect('/login');
      }
    }, [isAuthenticated, redirect]);

    if (!isAuthenticated) {
      return (
        <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
          <Spinner animation="border" role="status"/>
        </div>
      );
    }

    return <WrappedComponent {...props} />;
  };

  return AuthHOC;
};

export default withAuth;
