import {
  faGlobe,
  faHome,
  faSignOutAlt,
  faSignInAlt,
  faSun,
  faMoon,
  faBars,
  faExclamationTriangle,
  faInfoCircle,
  faCheckCircle,
  faExclamationCircle,
  faDatabase,
  faCode,
  faEye,
  faEyeSlash
} from '@fortawesome/free-solid-svg-icons';
import { library } from '@fortawesome/fontawesome-svg-core';

export const loadIcons = () => {
  library.add(
    faGlobe,
    faHome,
    faSignOutAlt,
    faSignInAlt,
    faSun,
    faMoon,
    faBars,
    faExclamationTriangle,
    faInfoCircle,
    faCheckCircle,
    faExclamationCircle,
    faDatabase,
    faCode,
    faEye,
    faEyeSlash
  );
};
