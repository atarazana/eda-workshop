import React, { FunctionComponent, useEffect, useState } from 'react';

import { Table, TableHeader, TableBody } from '@patternfly/react-table';
import { Divider, PageSection, TextContent, Title, TitleSizes, Text } from '@patternfly/react-core';
import { bottom } from '@patternfly/react-core/dist/js/helpers/Popper/thirdparty/popper-core';

export interface IAggregateMetric {
    name: string;
    value: number;
    unit: string;
    qualifier: string; // AVG / MAX / MIN / SUM / COUNT
    from: string;    
    groupByClause: string;
    timestamp: string;
  }

const AggregateMetrics: FunctionComponent<{ initial?: number }> = ({ initial = 0 }) => {
    const [choice, toggle] = useState('default');
    const [aggregateMetrics, setAggregateMetrics] = useState<IAggregateMetric[]>([]);
    const [rows, setRows] = useState<string[][]>([]);

    const columns = ['Name', 'Value', 'Unit', 'Qualifier', 'From', 'Group By', 'Timestamp'];

    useEffect(() => {
        const timeout = 5;

        const EVENT_SOURCE_URL = (process.env.NODE_ENV === 'development') ? 'http://localhost:8080/aggregate-metrics/stream' : '/aggregate-metrics/stream'; 

        console.log(`process.env.NODE_ENV=${process.env.NODE_ENV}`);

        let eventSource = new EventSource(EVENT_SOURCE_URL)
        eventSource.onmessage = e => {
            const aggregateMetric = JSON.parse(e.data) as IAggregateMetric;
            addAggregateMetric(aggregateMetric);   

            const row: string[] = [aggregateMetric.name, String(Math.round(aggregateMetric.value)), aggregateMetric.unit, aggregateMetric.qualifier, aggregateMetric.from, aggregateMetric.groupByClause, aggregateMetric.timestamp];
            addRow(row);
        }

        fetch('/aggregate-metrics')
        .then(res => res.json())
        .then(data => {
            setAggregateMetrics(data);
            setRows(data.map((aggregateMetric: IAggregateMetric) => [aggregateMetric.name, String(aggregateMetric.unit == 'EUR' ? aggregateMetric.value.toFixed(2) : aggregateMetric.value), aggregateMetric.unit, aggregateMetric.qualifier, aggregateMetric.from, aggregateMetric.groupByClause, aggregateMetric.timestamp]));
        })
        .catch(console.log)
    }, []);

    const addAggregateMetric = (aggregateMetric: IAggregateMetric) => setAggregateMetrics(prevMetrics => [...prevMetrics, aggregateMetric])
    const addRow = (row: string[]) => setRows(prevRows => [...prevRows, row])
    
    return <React.Fragment>
            <PageSection>
                <TextContent style={{paddingBottom: 10}}>
                    <Text component="h1">Aggregate Metrics</Text>
                    <Text component="p">
                    Here you'll find all the aggregate metrics cached in Red Had Data Grid.<br />
                    </Text>
                </TextContent>

                <Table
                    aria-label="Simple Table"
                    variant={'compact'}
                    borders={choice !== 'compactBorderless'}
                    cells={columns}
                    rows={rows}>
                    <TableHeader />
                    <TableBody />
                </Table>
            </PageSection>
        </React.Fragment>
    ;
}

export { AggregateMetrics };
