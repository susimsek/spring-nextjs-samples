import React from 'react';
import Head from 'next/head';
import { useTranslation } from 'next-i18next';
import LanguageSwitcher from '../../components/LanguageSwitcher';
import { getStaticPaths, makeStaticProps } from '../../lib/get-static';
import { Button, Col, Container, Row } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { toggleTheme } from '../../reducers/theme';
import { useAppDispatch, useAppSelector } from '../../config/store';

export default function HomePage() {
  const {
    i18n: { language: locale },
    t,
  } = useTranslation('common');

  const dispatch = useAppDispatch();

  const theme = useAppSelector(state => state.theme.theme);
  // Handle theme toggle
  const handleThemeToggle = () => {
    dispatch(toggleTheme()); // Dispatch theme toggle action
  };

  return (
    <React.Fragment>
      <Head>
        <title>{t('common:common.siteTitle')}</title>
      </Head>
      <Container className="mt-5">
        <Row>
          <Col>
            <h1>
              <FontAwesomeIcon icon="globe" /> {t('common:common.siteTitle')}
            </h1>
            <div>
              <span>{t('common:common.language')}:</span> <span>{locale}</span>
            </div>
            <div>⚡ Electron + Next.js ⚡</div>
          </Col>
        </Row>
        <Row className="mt-4">
          <Col>
            <Button variant="primary" className="me-2">
              <FontAwesomeIcon icon="save" /> {t('common:save')}
            </Button>
            <Button variant="danger">
              <FontAwesomeIcon icon="trash" /> {t('common:delete')}
            </Button>
          </Col>
        </Row>
        <Row className="mt-4">
          <Col>
            <LanguageSwitcher />
          </Col>
        </Row>
        <Row className="mt-4">
          <Col>
            <Button variant={theme === 'dark' ? 'light' : 'dark'} onClick={handleThemeToggle}>
              {theme === 'dark' ? t('common:light_mode') : t('common:dark_mode')}
            </Button>
          </Col>
        </Row>
      </Container>
    </React.Fragment>
  );
}

const getStaticProps = makeStaticProps(['common', 'home', 'footer']);
export { getStaticPaths, getStaticProps };
