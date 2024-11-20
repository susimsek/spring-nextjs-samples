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
  faTimes,
  faTrash,
} from '@fortawesome/free-solid-svg-icons';
import { IconDefinition, library } from '@fortawesome/fontawesome-svg-core';

// Custom Sidebar Icon Definition
const faSidebar: IconDefinition = {
  prefix: 'fas',
  iconName: 'sidebar',
  icon: [
    20,
    20,
    [],
    '',
    'M7 3h-5v14h5v-14zM9 3v14h9v-14h-9zM0 3c0-1.1 0.9-2 2-2h16c1.105 0 2 0.895 2 2v0 14c0 1.105-0.895 2-2 2v0h-16c-1.105 0-2-0.895-2-2v0-14zM3 4h3v2h-3v-2zM3 7h3v2h-3v-2zM3 10h3v2h-3v-2z', // SVG Path
  ],
};

// Load icons into FontAwesome library
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
    faEyeSlash,
    faCheckCircle,
    faExclamationCircle,
    faExclamationTriangle,
    faInfoCircle,
    faTimes,
    faSidebar,
  );
};
