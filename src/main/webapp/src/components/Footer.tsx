// components/Footer.tsx
import React from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import { useTranslation } from 'next-i18next';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCopyright } from '@fortawesome/free-solid-svg-icons';

const Footer: React.FC = () => {
  const { t } = useTranslation('footer');

  return (
    <footer className={`py-3`}>
      <Container>
        <Row className="text-center">
          <Col>
            <p className="footer-text mb-0">{t('footer.text')}</p>
            <p className="footer-rights mb-0 d-flex align-items-center justify-content-center">
              <FontAwesomeIcon icon={faCopyright} className="me-1" />
              {new Date().getFullYear()} {t('footer.rightsReserved')}
            </p>
          </Col>
        </Row>
      </Container>
    </footer>
  );
};

export default Footer;
