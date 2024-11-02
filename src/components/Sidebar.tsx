// components/Sidebar.tsx
import React from 'react';
import { Nav } from 'react-bootstrap';
import { useTranslation } from 'next-i18next';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome, faDatabase, faCode } from '@fortawesome/free-solid-svg-icons';
import LinkComponent from '../components/Link';

const Sidebar: React.FC = () => {
  const { t } = useTranslation('common');

  return (
    <Nav className="d-flex flex-column sidebar p-3 flex-grow-1">
      <Nav.Item className="mb-3">
        <LinkComponent href="/">
          <FontAwesomeIcon icon={faHome} className="me-2" />
          {t('common:common.menu.home')}
        </LinkComponent>
      </Nav.Item>
      <Nav.Item className="mb-3">
        <a href="/swagger-ui.html" target="_blank" rel="noopener noreferrer" className="nav-link">
          <FontAwesomeIcon icon={faCode} className="me-2" />
          {t('common:common.menu.api')}
        </a>
      </Nav.Item>
      <Nav.Item>
        <a href="/h2-console" target="_blank" rel="noopener noreferrer" className="nav-link">
          <FontAwesomeIcon icon={faDatabase} className="me-2" />
          {t('common:common.menu.database')}
        </a>
      </Nav.Item>
    </Nav>
  );
};

export default Sidebar;
