// components/Header.tsx
import React from 'react';
import { Navbar, Nav, Container, NavDropdown, Button } from 'react-bootstrap';
import { useTranslation } from 'next-i18next';
import i18nextConfig from '../../next-i18next.config';
import LanguageSwitchLink from '../components/LanguageSwitchLink';
import LinkComponent from '../components/Link';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faGlobe,
  faHome,
  faSignOutAlt,
  faSignInAlt,
  faSun,
  faMoon,
  faBars
} from '@fortawesome/free-solid-svg-icons';
import { useAppSelector, useAppDispatch } from '@/config/store';
import { logout } from '@/reducers/authentication';
import { toggleTheme } from '@/reducers/theme'; // Import theme toggle action
import { useRouter } from 'next/router';

const Header: React.FC = () => {
  const { t } = useTranslation(['common']);
  const dispatch = useAppDispatch();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const currentTheme = useAppSelector(state => state.theme.theme); // Get the current theme
  const router = useRouter();
  const currentLocale = router.query.locale || i18nextConfig.i18n.defaultLocale;

  const handleLogout = () => {
    dispatch(logout());
    router.replace(`/${currentLocale}/login`);
  };

  const handleThemeToggle = () => {
    dispatch(toggleTheme());
  };

  return (
    <Navbar bg="light" expand="lg" className="p-3">
        <Navbar.Brand as="span" className="d-flex align-items-center">
          <LinkComponent href="/">
            <img
              src="/logo.png"
              alt={t('common:common.siteTitle')}
              width="40"
              height="40"
              className="d-inline-block align-top me-2"
            />
          </LinkComponent>
          <span className="fw-bold">{t('common:common.siteTitle')}</span>
        </Navbar.Brand>

        <Navbar.Toggle aria-controls="basic-navbar-nav">
          <FontAwesomeIcon icon={faBars} className="navbar-toggler-icon" />
        </Navbar.Toggle>

        <Navbar.Collapse id="basic-navbar-nav" className="p-3">
          <Nav className="me-auto">
            <LinkComponent href="/">
              <FontAwesomeIcon icon={faHome} className="me-2" />
              {t('common:common.home')}
            </LinkComponent>
          </Nav>
          <Nav>
            <NavDropdown title={<><FontAwesomeIcon icon={faGlobe} className="me-2" />{t('common:common.language')}</>} id="language-selector">
              {i18nextConfig.i18n.locales.map((locale) => {
                if (locale === currentLocale) return null;
                return <LanguageSwitchLink locale={locale} key={locale} />;
              })}
            </NavDropdown>
            <Button
              onClick={handleThemeToggle}
              className="theme-toggle ps-0 ps-lg-3 me-2"
            >
              <FontAwesomeIcon icon={currentTheme === 'dark' ? faSun : faMoon} className="me-2" />
              {currentTheme === 'dark' ? t('common:common.theme.lightMode') : t('common:common.theme.darkMode')}
            </Button>
            {isAuthenticated ? (
              <Nav.Link as="span" onClick={handleLogout}>
                <FontAwesomeIcon icon={faSignOutAlt} className="me-2" />
                {t('common:common.logout')}
              </Nav.Link>
            ) : (
              <LinkComponent href="/login">
                <FontAwesomeIcon icon={faSignInAlt} className="me-2" />
                {t('common:common.login')}
              </LinkComponent>
            )}
          </Nav>
        </Navbar.Collapse>
    </Navbar>
  );
};

export default Header;
