import React from 'react';

import { Grid, GridItem, PageSection, Title } from '@patternfly/react-core';
import { LineChartByName } from '@app/AggregateMetrics/LineChartByName';

const Dashboard: React.FunctionComponent = () => (
  <PageSection>
    <Grid hasGutter>
      <GridItem span={6}><LineChartByName metricName="Balance by Region" /></GridItem>
      <GridItem span={6}><LineChartByName metricName="Accounts Closed by Region" /></GridItem>
      
      <GridItem span={6}><LineChartByName metricName="Accounts Inactive by Region" /></GridItem>
      <GridItem span={6}><LineChartByName metricName="Accounts Active by Region" /></GridItem>
    </Grid>
  </PageSection>
)

export { Dashboard };
