import React, { FunctionComponent, useEffect, useState } from 'react';

import { Table, TableHeader, TableBody } from '@patternfly/react-table';
import { Alert, AlertActionCloseButton, AlertActionLink, AlertGroup, AlertVariant, PageSection, Title, ToggleGroup, ToggleGroupItem } from '@patternfly/react-core';

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
            let alert = JSON.parse(e.data) as IAlert;

            console.log(alert);
            
            // const alertComponent = <Alert title="Default timeout Alert" timeout={timeout} actionLinks={
            //     <React.Fragment>
            //       <AlertActionLink>View details</AlertActionLink>
            //       <AlertActionLink>Ignore</AlertActionLink>
            //     </React.Fragment>
            //   }>
            //        This alert will dismiss after {`${timeout / 1000} seconds`}
            //     </Alert>

            addAlert(alert);   

            const row: string[] = [alert.id, alert.name, alert.definition, alert.expression, alert.duration, alert.timestamp];
            addRow(row);
        }
    }, []);

    const addAlert = (alert: IAlert) => setAlerts(prevAlerts => [...prevAlerts, alert])
    const addRow = (row: string[]) => setRows(prevRows => [...prevRows, row])
    
    const removeAlert = (alert: IAlert) => setAlerts(prevAlerts => {
        return prevAlerts.filter(element => element.id != alert.id);
    })

    return <PageSection>
            <React.Fragment>
                <ToggleGroup aria-label="Default with single selectable">
                <ToggleGroupItem
                    text={"Default " + alerts.length}
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
            </React.Fragment>
            </PageSection>
    ;
}

export { Alerts };
