// components/Header.tsx
import React from 'react';
import { Navbar, Nav, Container, NavDropdown } from 'react-bootstrap';
import { useTranslation } from 'next-i18next';
import { useRouter } from 'next/router';
import i18nextConfig from '../../next-i18next.config';
import LanguageSwitchLink from './LanguageSwitchLink';
import Link from 'next/link';
import {faGlobe, faHome} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const Header: React.FC = () => {
  const { t } = useTranslation(['common']);
  const router = useRouter();
  const currentLocale = router.query.locale || i18nextConfig.i18n.defaultLocale;

  return (
    <Navbar bg="light" expand="lg" className="mb-4">
      <Container>
        <Navbar.Brand href="/" className="d-flex align-items-center">
          <img
            src="/logo.png" // Path to logo
            alt={t('common:common.siteTitle')}
            width="40"
            height="40"
            className="d-inline-block align-top me-2"
          />
          <span className="fw-bold">{t('common:common.siteTitle')}</span>
        </Navbar.Brand>

        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="/">
              <FontAwesomeIcon icon={faHome} className="me-2" />
              {t('common:common.home')}
            </Nav.Link>
          </Nav>
          <Nav>
            <NavDropdown title={<><FontAwesomeIcon icon={faGlobe} className="me-2" />{t('common:common.language')}</>} id="language-selector">
              {i18nextConfig.i18n.locales.map((locale) => {
                if (locale === currentLocale) return null;
                return <LanguageSwitchLink locale={locale} key={locale} />;
              })}
            </NavDropdown>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
