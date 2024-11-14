import Document, { DocumentContext, Head, Html, Main, NextScript } from 'next/document';
import i18next from '../../next-i18next.config.js';

export default class MyDocument extends Document {
  static async getInitialProps(ctx: DocumentContext) {
    const initialProps = await Document.getInitialProps(ctx);
    return { ...initialProps };
  }

  render() {
    return (
      <Html lang={i18next.i18n.defaultLocale as string}>
        <Head />
        <body>
          <Main />
          <NextScript />
        </body>
      </Html>
    );
  }
}
