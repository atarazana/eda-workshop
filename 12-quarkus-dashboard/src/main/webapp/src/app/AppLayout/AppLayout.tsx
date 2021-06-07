import * as React from 'react';
import { useEffect, useState } from 'react';

import { NavLink, useLocation, useHistory } from 'react-router-dom';
import {
  Nav,
  NavList,
  NavItem,
  NavExpandable,
  Page,
  PageHeader,
  PageSidebar,
  SkipToContent,
  PageHeaderTools,
  Avatar,
  Button,
  ButtonVariant,
  Dropdown,
  DropdownToggle,
  KebabToggle,
  PageHeaderToolsGroup,
  PageHeaderToolsItem,
  DropdownItem,
  DropdownGroup,
  Alert,
  AlertActionCloseButton,
  AlertGroup,
  Badge,
  NotificationBadge
} from '@patternfly/react-core';
import { routes, IAppRoute, IAppRouteGroup } from '@app/routes';
import logo from '@app/bgimages/Patternfly-Logo.svg';
import { CogIcon, HelpIcon } from '@patternfly/react-icons';

import imgAvatar from '@app/bgimages/avatarImg.svg';

import { IAlert } from '@app/Alerts/Alerts';

interface IAppLayout {
  children: React.ReactNode;
}

const AppLayout: React.FunctionComponent<IAppLayout> = ({ children }) => {
  const [isNavOpen, setIsNavOpen] = React.useState(true);
  const [isMobileView, setIsMobileView] = React.useState(true);
  const [isNavOpenMobile, setIsNavOpenMobile] = React.useState(false);
  const [isUpperToolbarKebabDropdownOpen, setIsUpperToolbarKebabDropdownOpen] = React.useState(false);
  const [isUpperToolbarDropdownOpen, setIsUpperToolbarDropdownOpen] = React.useState(false);
  
  const [alerts, setAlerts] = useState<IAlert[]>([]);
  const [unReadNotifications, setUnReadNotifications] = useState<number>(0);

  const onNavToggleMobile = () => {
    setIsNavOpenMobile(!isNavOpenMobile);
  };
  const onNavToggle = () => {
    setIsNavOpen(!isNavOpen);
  }
  const onPageToolbarKebabDropdownToggle = () => {
    setIsUpperToolbarKebabDropdownOpen(!isUpperToolbarKebabDropdownOpen);
  };
  const onPageDropdownSelect = () => {
    setIsUpperToolbarDropdownOpen(!isUpperToolbarDropdownOpen);
  };
  const onPageDropdownToggle = isUpperToolbarDropdownOpen => {
    setIsUpperToolbarDropdownOpen(isUpperToolbarDropdownOpen);
  };

  const onPageResize = (props: { mobileView: boolean; windowSize: number }) => {
    setIsMobileView(props.mobileView);
  };

  const kebabDropdownItems = [
    <DropdownItem key="kebab-settings">
      <CogIcon /> Settings
    </DropdownItem>,
    <DropdownItem key="kebab-help">
      <HelpIcon /> Help
    </DropdownItem>
  ];
  const userDropdownItems = [
    <DropdownGroup key="group 2">
      <DropdownItem key="group 2 profile">My profile</DropdownItem>
      <DropdownItem key="group 2 user" component="button">
        User management
      </DropdownItem>
      <DropdownItem key="group 2 logout">Logout</DropdownItem>
    </DropdownGroup>
  ];
  
  const addAlert = (alert: IAlert) => setAlerts(prevAlerts => {
    console.log('HI');
    setUnReadNotifications((prevUnreadNotificationsCount) => prevUnreadNotificationsCount+1);
    return [...prevAlerts, alert];
  });
    
  const removeAlert = (alert: IAlert) => setAlerts(prevAlerts => {
      return prevAlerts.filter(element => element.id != alert.id);
  });

  const resetUnreadNotificationsCount = () => setUnReadNotifications(0);

  const handleAlertServerEvent = (alert: IAlert) => {
    addAlert(alert);
  }

  useEffect(() => {
      const EVENT_SOURCE_URL = (process.env.NODE_ENV === 'development') ? 'http://localhost:8080/alerts/stream' : '/alerts/stream'; 

      console.log(`process.env.NODE_ENV=${process.env.NODE_ENV}`);
      
      let eventSource = new EventSource(EVENT_SOURCE_URL)

      eventSource.onmessage = e => handleAlertServerEvent(JSON.parse(e.data) as IAlert);

      eventSource.onerror = () => { eventSource.close(); }

      return () => {
          eventSource.close();
      };
  }, []);

  function LogoImg() {
    const history = useHistory();
    function handleClick() {
      history.push('/');
    }
    return (
      <img src={logo} onClick={handleClick} alt="PatternFly Logo" />
    );
  }

  const headerTools = (
    <PageHeaderTools>
      <PageHeaderToolsGroup
        visibility={{
          default: 'hidden',
          lg: 'visible'
        }} /** the settings and help icon buttons are only visible on desktop sizes and replaced by a kebab dropdown for other sizes */
      >
        <PageHeaderToolsItem>
          <Button aria-label="Settings actions" variant={ButtonVariant.plain}>
            <CogIcon />
          </Button>
        </PageHeaderToolsItem>
        <PageHeaderToolsItem>
          <NotificationBadge variant={unReadNotifications > 0 ? 'unread' : 'read'} onClick={resetUnreadNotificationsCount} count={unReadNotifications} aria-label="Notifications" />
        </PageHeaderToolsItem>
      </PageHeaderToolsGroup>
      <PageHeaderToolsGroup>
        <PageHeaderToolsItem
          visibility={{
            lg: 'hidden'
          }} /** this kebab dropdown replaces the icon buttons and is hidden for desktop sizes */
        >
          <Dropdown
            isPlain
            position="right"
            // onSelect={onKebabDropdownSelect}
            toggle={<KebabToggle onToggle={onPageToolbarKebabDropdownToggle} />}
            isOpen={isUpperToolbarKebabDropdownOpen}
            dropdownItems={kebabDropdownItems}
          />
        </PageHeaderToolsItem>
        <PageHeaderToolsItem
          visibility={{ default: 'hidden', md: 'visible' }} /** this user dropdown is hidden on mobile sizes */
        >
          <Dropdown
            isPlain
            position="right"
            onSelect={onPageDropdownSelect}
            isOpen={isUpperToolbarDropdownOpen}
            toggle={<DropdownToggle onToggle={onPageDropdownToggle}>John Smith</DropdownToggle>}
            dropdownItems={userDropdownItems}
          />
        </PageHeaderToolsItem>
      </PageHeaderToolsGroup>
      <Avatar src={imgAvatar} alt="Avatar image" />
    </PageHeaderTools>
  );
  
  const Header = (
    <PageHeader
      logo={<LogoImg />}
      showNavToggle
      isNavOpen={isNavOpen}
      headerTools={headerTools}
      onNavToggle={isMobileView ? onNavToggleMobile : onNavToggle}
    />
  );

  const location = useLocation();

  const renderNavItem = (route: IAppRoute, index: number) => (
    <NavItem key={`${route.label}-${index}`} id={`${route.label}-${index}`}>
      <NavLink exact={route.exact} to={route.path} activeClassName="pf-m-current">
        {route.label}
      </NavLink>
    </NavItem>
  );

  const renderNavGroup = (group: IAppRouteGroup, groupIndex: number) => (
    <NavExpandable
      key={`${group.label}-${groupIndex}`}
      id={`${group.label}-${groupIndex}`}
      title={group.label}
      isActive={group.routes.some((route) => route.path === location.pathname)}
    >
      {group.routes.map((route, idx) => route.label && renderNavItem(route, idx))}
    </NavExpandable>
  );

  const Navigation = (
    <Nav id="nav-primary-simple" theme="dark">
      <NavList id="nav-list-simple">
        {routes.map(
          (route, idx) => route.label && (!route.routes ? renderNavItem(route, idx) : renderNavGroup(route, idx))
        )}
      </NavList>
    </Nav>
  );

  const Sidebar = (
    <PageSidebar
      theme="dark"
      nav={Navigation}
      isNavOpen={isMobileView ? isNavOpenMobile : isNavOpen} />
  );

  const pageId = 'primary-app-container';

  const PageSkipToContent = (
    <SkipToContent onClick={(event) => {
      event.preventDefault();
      const primaryContentContainer = document.getElementById(pageId);
      primaryContentContainer && primaryContentContainer.focus();
    }} href={`#${pageId}`}>
      Skip to Content
    </SkipToContent>
  );
  return (
    
    <Page
      mainContainerId={pageId}
      header={Header}
      sidebar={Sidebar}
      onPageResize={onPageResize}
      skipToContent={PageSkipToContent}>
      {children}
      <AlertGroup isToast>
          {alerts.map((alert) => (
              <Alert
              isLiveRegion
              variant={alert.variant}
              title={alert.name}
              timeout
              actionClose={
                  <AlertActionCloseButton
                  title={alert.name}
                  variantLabel={`${alert.variant} alert`}
                  onClose={() => removeAlert(alert)}
                  />
              }
              key={alert.id} />
          ))}
      </AlertGroup>
    </Page>

  );
}

export { AppLayout };
