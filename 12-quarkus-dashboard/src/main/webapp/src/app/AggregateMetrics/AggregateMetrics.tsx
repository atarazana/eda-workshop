import React, { FunctionComponent, useEffect, useState } from 'react';

import { Table, TableHeader, TableBody } from '@patternfly/react-table';
import { PageSection, ToggleGroup, ToggleGroupItem } from '@patternfly/react-core';

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

            const row: string[] = [aggregateMetric.name, String(aggregateMetric.value), aggregateMetric.unit, aggregateMetric.qualifier, aggregateMetric.from, aggregateMetric.groupByClause, aggregateMetric.timestamp];
            addRow(row);
        }

        fetch('/aggregate-metrics')
        .then(res => res.json())
        .then(data => {
            setAggregateMetrics(data);
            setRows(data.map((aggregateMetric: IAggregateMetric) => [aggregateMetric.name, String(aggregateMetric.value), aggregateMetric.unit, aggregateMetric.qualifier, aggregateMetric.from, aggregateMetric.groupByClause, aggregateMetric.timestamp]));
        })
        .catch(console.log)
    }, []);

    const addAggregateMetric = (aggregateMetric: IAggregateMetric) => setAggregateMetrics(prevMetrics => [...prevMetrics, aggregateMetric])
    const addRow = (row: string[]) => setRows(prevRows => [...prevRows, row])
    
    return <PageSection>
            <React.Fragment>
                <ToggleGroup aria-label="Default with single selectable">
                <ToggleGroupItem
                    text={"Default " + rows.length}
                    buttonId="default"
                />
                
                </ToggleGroup>
                <Table
                aria-label="Simple Table"
                // variant={choice !== 'default' ? 'compact' : null}
                borders={choice !== 'compactBorderless'}
                cells={columns}
                rows={rows}
                >
                <TableHeader />
                <TableBody />
                </Table>
            </React.Fragment>
            </PageSection>
    ;
}

export { AggregateMetrics };
