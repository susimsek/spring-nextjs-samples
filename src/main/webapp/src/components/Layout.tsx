import { ReactNode, useState } from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';
import Sidebar from '@/components/Sidebar';
import { Col, Container, Row } from 'react-bootstrap';

type LayoutProps = {
  children: ReactNode;
  showSidebar?: boolean;
  onNavigate?: (page: string) => void;
};

export default function Layout({
  children,
  showSidebar = false,
  onNavigate = () => {}, // Default empty function to avoid undefined
}: LayoutProps) {
  const [isSidebarOpen, setIsSidebarOpen] = useState<boolean>(showSidebar);

  const toggleSidebar = () => setIsSidebarOpen(prev => !prev);

  return (
    <>
      <Header onToggleSidebar={toggleSidebar} showSidebarToggle={showSidebar} />

      <Container fluid className="d-flex flex-column min-vh-100 p-0">
        <Row className="flex-grow-1 g-0">
          {showSidebar && isSidebarOpen && (
            <Col xs={12} md={3} lg={2} className="p-0 d-flex flex-column">
              <Sidebar onNavigate={onNavigate} />
            </Col>
          )}
          <Col xs={12} md={showSidebar && isSidebarOpen ? 9 : 12} lg={showSidebar && isSidebarOpen ? 10 : 12}>
            {children}
          </Col>
        </Row>
        <Footer />
      </Container>
    </>
  );
}
