import React from 'react';
import { Nav, Offcanvas } from 'react-bootstrap';
import { useTranslation } from 'next-i18next';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import useMediaQuery from '../hooks/useMediaQuery';
import Image from 'next/image';
import LinkComponent from '../components/Link';
import { useAppSelector } from '../config/store';

type SidebarProps = {
  onClose: () => void;
  onNavigate: (page: string) => void;
};

const Sidebar: React.FC<SidebarProps> = ({ onClose, onNavigate }) => {
  const { t } = useTranslation('common');
  const { theme } = useAppSelector(state => state.theme);
  const isMobile = useMediaQuery('(max-width: 768px)');

  const handleNavigate = (page: string) => {
    onNavigate(page);
    if (isMobile) {
      onClose();
    }
  };

  const renderNav = () => (
    <Nav className="d-flex flex-column sidebar p-2 flex-grow-1">
      <Nav.Item className="mb-3">
        <a href="#" onClick={() => handleNavigate('home')} className="nav-link">
          <FontAwesomeIcon icon="home" className="me-2" />
          {t('common:common.menu.home')}
        </a>
      </Nav.Item>
      <Nav.Item className="mb-3">
        <a href="#" onClick={() => handleNavigate('api')} className="nav-link">
          <FontAwesomeIcon icon="code" className="me-2" />
          {t('common:common.menu.api')}
        </a>
      </Nav.Item>
      <Nav.Item className="mb-3">
        <a href="#" onClick={() => handleNavigate('gqlApi')} className="nav-link">
          <FontAwesomeIcon icon="code" className="me-2" />
          {t('common:common.menu.gqlApi')}
        </a>
      </Nav.Item>
      <Nav.Item>
        <a href="#" onClick={() => handleNavigate('database')} className="nav-link">
          <FontAwesomeIcon icon="database" className="me-2" />
          {t('common:common.menu.database')}
        </a>
      </Nav.Item>
    </Nav>
  );

  return (
    <>
      {isMobile && (
        <Offcanvas
          show={isMobile}
          onHide={onClose}
          className={`offcanvas ${theme === 'dark' ? 'dark-theme' : 'light-theme'}`}
          placement="start"
        >
          <Offcanvas.Header closeButton={false}>
            <div className="d-flex align-items-center brand">
              <LinkComponent href="/home" onClick={onClose}>
                <Image
                  src="/images/logo.png"
                  alt={t('common:common.siteTitle')}
                  width={40}
                  height={40}
                  className="d-inline-block align-top me-2"
                />
              </LinkComponent>
              <span className="fw-bold">{t('common:common.siteTitle')}</span>
            </div>
            <button
              type="button"
              className="close-icon position-absolute top-0 end-0 m-3"
              aria-label="Close"
              onClick={onClose}
            >
              <FontAwesomeIcon
                icon="times"
                style={{
                  fontSize: '16px',
                }}
              />
            </button>
          </Offcanvas.Header>
          <Offcanvas.Body>{renderNav()}</Offcanvas.Body>
        </Offcanvas>
      )}
      {!isMobile && renderNav()}
    </>
  );
};

export default Sidebar;
