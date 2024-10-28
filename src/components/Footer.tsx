// components/Footer.tsx
import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { useTranslation } from 'next-i18next';

const Footer: React.FC = () => {
  const { t } = useTranslation('footer');

  return (
    <footer className="footer">
      <Container>
        <Row>
          <Col md={6}>
            <p className="footer-text">{t('footer.text')}</p>
          </Col>
          <Col md={6} className="text-md-end">
            <p className="footer-rights">
              &copy; {new Date().getFullYear()} {t('footer.rightsReserved')}
            </p>
          </Col>
        </Row>
      </Container>
    </footer>
  );
};

export default Footer;
