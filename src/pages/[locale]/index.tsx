// pages/index.tsx
import {Container, Button, Spinner} from 'react-bootstrap';
import Head from 'next/head';
import { useTranslation } from 'next-i18next';
import { makeStaticProps, getStaticPaths } from '@/lib/getStatic';
import React, {useEffect, useState} from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';
import {fetchHelloMessage} from "@/api/helloApi";
import {HelloDTO} from "@/types/helloDTO";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHome, faSave, faTimes} from "@fortawesome/free-solid-svg-icons";
import NotificationToast, {TostMessage} from "@/components/NotificationToast";
import {ProblemDetail} from "@/types/problemDetail";
import axios from 'axios';

const Home = () => {
  const { t } = useTranslation(['common', 'home']);
  const [messageData, setMessageData] = useState<HelloDTO | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [toast, setToast] = useState<TostMessage| null>(null);

  const showToast = () => {
    setToast({
      messageKey: 'common:common:saveSuccess',
      variant: "success"
    });
  };

  const closeToast = () => setToast(null);


  useEffect(() => {
    const getMessage = async () => {
      try {
        const data = await fetchHelloMessage();
        setMessageData(data);
      } catch (error) {
        console.error(error);
        handleError(error)
      } finally {
        setLoading(false);
      }
    };

    getMessage();
  }, []);


  const handleError = (error: any) => {
    let messageKey: string | undefined = undefined;
    let errorMessage = t('common:common.error.http.default');

    if (axios.isAxiosError(error) && error.response) {
      const status = error.response.status;
      const problemDetail: ProblemDetail = error.response.data;

      switch (status) {
        case 500:
        case 503:
        case 405:
          messageKey = `common:common.error.http.${status}`;
          errorMessage = t(messageKey);
          break;
        default:
          errorMessage = problemDetail.detail || errorMessage;
          break;
      }

      if (problemDetail.violations && problemDetail.violations.length > 0) {
        const validationErrors = problemDetail.violations
          .map(v => `${v.field}: ${v.message}`)
          .join('\n');
        errorMessage += `\n${t("common:common.error.validationErrors")}\n${validationErrors}`;
      }
    }
    setToast({ messageKey, message: errorMessage, variant: "danger"});
  };


  return (
    <>
      <Head>
        <title>{t('common:common.siteTitle')}</title>
      </Head>

      {toast && (
        <NotificationToast
          message={toast.message}
          messageKey={toast.messageKey}
          data={toast.data}
          show={!!toast}
          onClose={closeToast}
          variant={toast.variant}
          dismissible={true}
          size="lg"
        />
      )}

      <Header />

      <Container className="mt-4">
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

        <div className="d-flex gap-2 mb-4">
          <Button variant="primary" className="mb-4" onClick={showToast}>
            <FontAwesomeIcon icon={faSave} className="me-2"/>
            {t('common:common.save')}
          </Button>
          <Button variant="secondary" className="ms-2 mb-4">
            <FontAwesomeIcon icon={faTimes} className="me-2"/>
            {t('common:common.cancel')}
          </Button>
        </div>
      </Container>

      <Footer/>
    </>
  );
};

export default Home;

const getStaticProps = makeStaticProps(['common', 'home', 'footer']);
export {getStaticPaths, getStaticProps};
