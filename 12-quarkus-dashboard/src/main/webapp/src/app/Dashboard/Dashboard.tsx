import React from 'react';

import { Grid, GridItem, PageSection, Stack, StackItem, TextContent, Text, Title, Flex, FlexItem } from '@patternfly/react-core';
import { LineChartByName } from '@app/AggregateMetrics/LineChartByName';

const Dashboard: React.FunctionComponent = () => (
  <PageSection>
  
    <Stack>

    <StackItem>
    <TextContent style={{paddingBottom: 10}}>
        <Text component="h1">Dashboard</Text>
        <Text component="p">
        Simple dashboard based on AggregateMetric objects stored in Red Hat Data Grid<br />
        </Text>
    </TextContent>
    </StackItem>

    <StackItem>
    <Flex justifyContent={{ default: 'justifyContentSpaceEvenly' }}>
      <FlexItem><LineChartByName timePeriod={60*60} timeRange={15*60} metricName="Balance by Region" /></FlexItem>
      <FlexItem><LineChartByName timePeriod={60*60} timeRange={15*60} metricName="Accounts Closed by Region" /></FlexItem>
    </Flex>
    </StackItem>

    <StackItem>
    <Flex justifyContent={{ default: 'justifyContentSpaceEvenly' }}>
      <FlexItem ><LineChartByName timePeriod={60*60} timeRange={15*60} metricName="Accounts Inactive by Region" /></FlexItem>
      <FlexItem ><LineChartByName timePeriod={60*60} timeRange={15*60} metricName="Accounts Active by Region" /></FlexItem>
    </Flex>

    {/* <Grid hasGutter={true}>
      <GridItem span={6}><LineChartByName timePeriod={60*60} timeRange={15*60} metricName="Balance by Region" /></GridItem>
      <GridItem span={6}><LineChartByName timePeriod={60*60} timeRange={15*60} metricName="Accounts Closed by Region" /></GridItem>
      
      <GridItem span={6}><LineChartByName timePeriod={60*60} timeRange={15*60} metricName="Accounts Inactive by Region" /></GridItem>
      <GridItem span={6}><LineChartByName timePeriod={60*60} timeRange={15*60} metricName="Accounts Active by Region" /></GridItem>
    </Grid> */}
    </StackItem>
  </Stack>
  </PageSection>
  
)

export { Dashboard };
