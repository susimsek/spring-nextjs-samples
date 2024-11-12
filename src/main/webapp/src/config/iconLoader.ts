import {
  faBars,
  faCheckCircle,
  faCode,
  faDatabase,
  faExclamationCircle,
  faExclamationTriangle,
  faEye,
  faEyeSlash,
  faGlobe,
  faHome,
  faInfoCircle,
  faMoon,
  faSave,
  faSearch,
  faSignInAlt,
  faSignOutAlt,
  faSun,
  faTrash
} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';

export const loadIcons = () => {
  library.add(
    faGlobe,
    faHome,
    faSave,
    faSearch,
    faSignOutAlt,
    faSignInAlt,
    faSun,
    faMoon,
    faBars,
    faExclamationTriangle,
    faInfoCircle,
    faCheckCircle,
    faExclamationCircle,
    faTrash,
    faDatabase,
    faCode,
    faEye,
    faEyeSlash
  );
};
