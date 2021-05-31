import React from 'react';
import { ChartDonutUtilization } from '@patternfly/react-charts';


import { PageSection, Title } from '@patternfly/react-core';

const Dashboard: React.FunctionComponent = () => (
  <PageSection>
  <div style={{ height: '230px', width: '230px' }}>
    <ChartDonutUtilization
      ariaDesc="Storage capacity"
      ariaTitle="Donut utilization chart example"
      constrainToVisibleArea={true}
      data={{ x: 'GBps capacity', y: 70 }}
      labels={({ datum }) => datum.x ? `${datum.x}: ${datum.y}%` : null}
      subTitle="of 100 GBps"
      title="70%"
    />
  </div>
  </PageSection>
)

export { Dashboard };
