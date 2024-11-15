// components/Sidebar.tsx
import React from 'react';
import { Nav } from 'react-bootstrap';
import { useTranslation } from 'next-i18next';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

type SidebarProps = {
  onNavigate: (page: string) => void;
};

const Sidebar: React.FC<SidebarProps> = ({ onNavigate }) => {
  const { t } = useTranslation('common');

  return (
    <Nav className="d-flex flex-column sidebar p-2 flex-grow-1">
      <Nav.Item className="mb-3">
        <a href="#" onClick={() => onNavigate('home')} className="nav-link">
          <FontAwesomeIcon icon="home" className="me-2" />
          {t('common:common.menu.home')}
        </a>
      </Nav.Item>
      <Nav.Item className="mb-3">
        <a href="#" onClick={() => onNavigate('api')} className="nav-link">
          <FontAwesomeIcon icon="code" className="me-2" />
          {t('common:common.menu.api')}
        </a>
      </Nav.Item>
      <Nav.Item className="mb-3">
        <a href="#" onClick={() => onNavigate('gqlApi')} className="nav-link">
          <FontAwesomeIcon icon="code" className="me-2" />
          {t('common:common.menu.gqlApi')}
        </a>
      </Nav.Item>
      <Nav.Item>
        <a href="#" onClick={() => onNavigate('database')} className="nav-link">
          <FontAwesomeIcon icon="database" className="me-2" />
          {t('common:common.menu.database')}
        </a>
      </Nav.Item>
    </Nav>
  );
};

export default Sidebar;
