import React, { FunctionComponent, useEffect, useState } from 'react';

import { Table, TableHeader, TableBody } from '@patternfly/react-table';
import { Alert, AlertActionCloseButton, AlertActionLink, AlertGroup, AlertVariant, PageSection, Title, TitleSizes, ToggleGroup, ToggleGroupItem } from '@patternfly/react-core';

export interface IAlert {
    id: string;
    name: string;
    variant: AlertVariant;
    definition: string;
    expression: string;
    duration: string;
    labels: any;
    annotations: any;
    timestamp: string;
  }

const Alerts: FunctionComponent<{ initial?: number }> = ({ initial = 0 }) => {
    const [clicks, setClicks] = useState(initial);
    const [choice, toggle] = useState('default');
    const [alerts, setAlerts] = useState<IAlert[]>([]);
    const [rows, setRows] = useState<string[][]>([]);

    const columns = ['Id', 'Name', 'Definition', 'Expression', 'Duration', 'Timestamp'];

    useEffect(() => {
        const timeout = 5;

        const EVENT_SOURCE_URL = (process.env.NODE_ENV === 'development') ? 'http://localhost:8080/alerts/stream' : '/alerts/stream'; 

        console.log(`process.env.NODE_ENV=${process.env.NODE_ENV}`);

        let eventSource = new EventSource(EVENT_SOURCE_URL)
        eventSource.onmessage = e => {
            const alert = JSON.parse(e.data) as IAlert;
            // addAlert(alert);   

            const row: string[] = [alert.id, alert.name, alert.definition, alert.expression, alert.duration, alert.timestamp];
            addRow(row);
        }

        fetch('/alerts')
        .then(res => res.json())
        .then( data => {
            //setAlerts(data);
            setRows(data.map((element: IAlert) => [element.id, element.name, element.definition, element.expression, element.duration, element.timestamp]));
        })
        .catch(console.log)
    }, []);

    // const addAlert = (alert: IAlert) => setAlerts(prevAlerts => [...prevAlerts, alert])
    const addRow = (row: string[]) => setRows(prevRows => [...prevRows, row])
    
    // const removeAlert = (alert: IAlert) => setAlerts(prevAlerts => {
    //     return prevAlerts.filter(element => element.id != alert.id);
    // })

    return <PageSection>
            <React.Fragment>
                <Title headingLevel="h1" size={TitleSizes['lg']}>
                Alerts
                </Title>
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

export { Alerts };
