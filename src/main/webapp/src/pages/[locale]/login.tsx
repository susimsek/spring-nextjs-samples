// pages/login.tsx
import { useState, useEffect } from 'react';
import { Form, Button, Container, Row, Col, Alert, Spinner, Card, InputGroup } from 'react-bootstrap';
import { useTranslation } from 'next-i18next';
import Head from 'next/head';
import Header from '@/components/Header';
import Footer from '@/components/Footer';
import { makeStaticProps, getStaticPaths } from '@/lib/getStatic';
import { login } from '@/api/authApi';
import { LoginRequestDTO } from '@/types/loginRequestDTO';
import { useForm, SubmitHandler } from 'react-hook-form';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import { useAppDispatch } from '@/config/store';
import { login as loginAction } from '@/reducers/authentication';
import { useRedirect } from '@/hooks/useRedirect';
import {ALPHANUMERIC_PATTERN} from "@/config/constants";

interface LoginFormInputs {
  username: string;
  password: string;
}

const Login = () => {
  const { t } = useTranslation(['common', 'login']);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [passwordVisible, setPasswordVisible] = useState(false);
  const redirect = useRedirect();
  const dispatch = useAppDispatch();

  const { register, handleSubmit, formState: { errors }, watch } = useForm<LoginFormInputs>({
    mode: 'onChange',
  });

  const onSubmit: SubmitHandler<LoginFormInputs> = async (data) => {
    setError(null);
    setLoading(true);

    const { username, password } = data;

    try {
      const loginData: LoginRequestDTO = { username, password };
      const tokenData = await login(loginData);
      dispatch(loginAction(tokenData.accessToken));
      redirect('/');
    } catch (error) {
      setError(t('login:login.form.errorInvalidCredentials'));
    } finally {
      setLoading(false);
    }
  };

  const username = watch('username');
  const password = watch('password');

  useEffect(() => {
    if (username || password) {
      setError(null);
    }
  }, [username, password]);

  return (
    <>
      <Head>
        <title>{t('login:login.pageTitle')}</title>
      </Head>

      <Header />

      <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '80vh' }}>
        <Row className="w-100">
          <Col xs={12} md={10} lg={6} className="mx-auto">
            <Card className="shadow-lg border-0">
              <Card.Header className="text-center" style={{fontWeight: 'bold', fontSize: '1.5rem', padding: '1rem' }}>
                {t('login:login.form.heading')}
              </Card.Header>

              <Card.Body className="p-4">
                <Form onSubmit={handleSubmit(onSubmit)}>
                  <Form.Group controlId="formUsername" className="mb-3">
                    <Form.Label>{t('login:login.form.usernameLabel')}</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder={t('login:login.form.usernamePlaceholder')}
                      {...register("username", {
                        required: t('common:common.validation.required'),
                        minLength: { value: 3, message: t('common:common.validation.minlength', { min: 3 }) },
                        maxLength: { value: 50, message: t('common:common.validation.maxlength', { max: 50 }) },
                        pattern: {
                          value: ALPHANUMERIC_PATTERN,
                          message: t('common:common.validation.alphanumeric')
                        }
                      })}
                      isInvalid={!!errors.username}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errors.username?.message}
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Form.Group controlId="formPassword" className="mb-3">
                    <Form.Label>{t('login:login.form.passwordLabel')}</Form.Label>
                    <InputGroup>
                      <Form.Control
                        type={passwordVisible ? 'text' : 'password'}
                        placeholder={t('login:login.form.passwordPlaceholder')}
                        {...register("password", {
                          required: t('common:common.validation.required'),
                          minLength: { value: 4, message: t('common:common.validation.minlength', { min: 4 }) },
                          maxLength: { value: 100, message: t('common:common.validation.maxlength', { max: 100 }) }
                        })}
                        isInvalid={!!errors.password}
                      />
                      <Button variant="outline-secondary" onClick={() => setPasswordVisible(!passwordVisible)}>
                        <FontAwesomeIcon icon={passwordVisible ? faEyeSlash : faEye} />
                      </Button>
                      <Form.Control.Feedback type="invalid">
                        {errors.password?.message}
                      </Form.Control.Feedback>
                    </InputGroup>
                  </Form.Group>

                  {error && (
                    <Alert variant="danger" className="mt-2 mb-4">
                      {error}
                    </Alert>
                  )}

                  <Button variant="primary" type="submit" className="w-100" size="lg" disabled={loading}>
                    {loading ? (
                      <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" />
                    ) : (
                      t('login:login.form.submitButton')
                    )}
                  </Button>
                </Form>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>

      <Footer />
    </>
  );
};

export default Login;

const getStaticProps = makeStaticProps(['common', 'login', 'footer']);
export { getStaticPaths, getStaticProps };
