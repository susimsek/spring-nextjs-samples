import React, { useState } from 'react';
import { Spinner } from 'react-bootstrap';

type EmbeddedContentFrameProps = {
  src: string;
  title: string;
  backgroundColor?: string;
};

const EmbeddedContentFrame: React.FC<EmbeddedContentFrameProps> = ({ src, title, backgroundColor }) => {
  const [loading, setLoading] = useState(true);

  return (
    <div className="iframe-wrapper" style={{ backgroundColor: backgroundColor || 'var(--background-color)' }}>
      {loading && (
        <div className="iframe-overlay d-flex justify-content-center align-items-center">
          <Spinner animation="border" role="status" />
        </div>
      )}
      <iframe
        src={src}
        onLoad={() => setLoading(false)}
        title={title}
      />
    </div>
  );
};

export default EmbeddedContentFrame;
