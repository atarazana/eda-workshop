import React, { FunctionComponent, useEffect, useState } from 'react';

import { IAggregateMetric } from './AggregateMetrics';
import { Chart, ChartVoronoiContainer, ChartAxis, ChartGroup, ChartLine, ChartLegendTooltip, createContainer, ChartThemeColor, ChartLabel } from '@patternfly/react-charts';
import { Title, TitleSizes } from '@patternfly/react-core';

interface ILegendDataItem {
    name?: string;
    childName?: string,
    symbol?: {
        fill?: string;
        type?: string;
    }
}

interface IDataItem {
    name?: string,
    x?: string,
    y: number
}

const LineChartByName: FunctionComponent<{metricName: string, timePeriod?: number, timeRange?: number,  metrics?: IAggregateMetric[]}> = ({ metricName, timePeriod = 30*60, timeRange = 5*60, metrics = [] }) => {
    const [name, setName] = useState<string>(metricName);
    const [period, setPeriod] = useState<number>(timePeriod);
    const [range, setRange] = useState<number>(timeRange);
    const [aggregateMetrics, setAggregateMetrics] = useState<IAggregateMetric[]>([]);
    const [ticksX, setTicksX] = useState<string[]>([]);
    const [legendData, setLegendData] = useState<ILegendDataItem[]>([]);
    const [data, setData] = useState<Map<string, IDataItem[]>>(new Map());

    const handleServerEvent = (metric: IAggregateMetric) => {
        if (metric.name === name) {
            console.log(`*** metric = ${metric.name} accepted!`)
            fetchData();
        }
        else {
            console.log(`*** metric = ${metric.name} discarded!`)
        }
    }

    const findNextDifferentToValue = (items: IDataItem[], i: number, value: number) => {
        items.find(item => item.y != value)
        for(let j = i; j < items.length; j++) {

        }
    }

    const fetchData = () => {
        fetch(`aggregate-metrics/by-name?name=${name}&period=${period}`)
        .then(res => res.json())
        .then(rawData => {
            let _aggregateMetrics: IAggregateMetric[] = [];
            let _data = new Map<string, IDataItem[]>();
            let _dates: Date[] = [];
            let periodOrigin = new Date();
            periodOrigin.setMinutes(periodOrigin.getMinutes() - timePeriod);
            rawData.forEach((metric: IAggregateMetric) => {
                _aggregateMetrics.push(metric);
                const _date: Date = new Date(metric.timestamp);
                if (_date >= periodOrigin) {
                    _dates.push(_date);
                    const dataItem: IDataItem = {name: `${metric.groupByClause}`, x: `${_date.getHours()}:${String(_date.getMinutes()).padStart(2, '0')}`, y: metric.value};
                    const key: string = metric.groupByClause;
                    if (_data.get(key) === undefined) {
                        _data.set(key, [dataItem]);
                    } else {
                        const currentData: IDataItem[] = _data.get(key)!;
                        _data.set(key, [...currentData, dataItem]);
                    }
                }
            });

            // There have to be as many data items as different date/times, we fill with y: -1 the gaps
            let i: number = 0;
            // For each date of all different timestamps recorded in all metrics
            _dates.forEach(_date => {
                // Let's build the date string from the timestamp
                let _dateString = `${_date.getHours()}:${String(_date.getMinutes()).padStart(2, '0')}`;
                // Let's run through all the elements in each set of data. Key will be the name of the data set.
                _data.forEach((_dataItems, key) => {
                    // If we still have data
                    if (i < _dataItems.length) {
                        // If the current date string is NOT its position (i)
                        if (_dateString != _dataItems[i]?.x) {
                            // If current date string is < the one in the array
                            if (_dataItems[i] === undefined) {
                                console.log("STOP HERE")
                            }
                            let _dataItemDateString = _dataItems[i].x!;
                            if (_dateString < _dataItemDateString) {
                                // Insert a copy of the value before
                                _dataItems.splice(i, 0, {name: key, x: _dateString, y: _dataItems[i].y!});
                            } else {
                                _dataItems.push({name: key, x: _dateString, y: _dataItems[i].y!});
                            }
                        }
                    } else {
                        _dataItems.push({name: key, x: _dateString, y: _dataItems[i - 1].y!});
                    }
                    

                    // // If we haven't reached the end of the data array
                    // if (i < _dataItems.length - 1) {
                    //     // If the current date string is NOT its position (i)
                    //     if (_dateString != _dataItems[i]?.x) {
                    //         // Copy the next value to the current position
                    //         _dataItems.splice(i, 0, {name: key, x: _dateString, y: _dataItems[i+1]?.y!});
                    //     }
                    //     // Else, do nothing, x is ok
                    // } else {
                    //     // There are no more elements so let's copy the previous value
                    //     if (i > 0) {
                    //         _dataItems.push({name: key, x: _dateString, y: _dataItems[i-1].y});
                    //     } else {
                    //         _dataItems.push({name: key, x: _dateString, y: 0});
                    //     }
                        
                    // }
                });
                i++;
            });
            
            setAggregateMetrics([..._aggregateMetrics]);
            setData(_data);

            let _legendData: ILegendDataItem[] = Array.from(_data.keys()).map((key) => {
                return {name: key, childName: key.toLowerCase()};
            });
            setLegendData(_legendData);

            let firstTick: Date = periodOrigin;
            let _ticksX: string[] = ['9:55', '10:00', '10:05', '10:10', '10:15', '10:20'];
            const numberOfTicks = timePeriod / timeRange;
            const minutesToNextTick = firstTick.getMinutes() % timeRange;
            
            // firstTick.setMinutes(firstTick.getMinutes() + minutesToNextTick);
            // for (let i = 0; i < numberOfTicks; i++) {

            //     let nextTick = firstTick;
            //     nextTick.setMinutes(firstTick.getMinutes() + i * RANGE);
            //     _ticksX.push(`${nextTick.getHours()}:${String(nextTick.getMinutes()).padStart(2, '0')}`)
            // }
            setTicksX(_ticksX);
        })
        .catch(console.log);
    }

    useEffect(() => {
        const timeout = 5;

        const EVENT_SOURCE_URL = (process.env.NODE_ENV === 'development') ? 'http://localhost:8080/aggregate-metrics/stream' : '/aggregate-metrics/stream'; 

        console.log(`process.env.NODE_ENV=${process.env.NODE_ENV}`);

        fetchData();

        let eventSource = new EventSource(EVENT_SOURCE_URL);

        eventSource.onmessage = e => handleServerEvent(JSON.parse(e.data) as IAggregateMetric);

        eventSource.onerror = () => { eventSource.close(); }

        return () => {
            eventSource.close();
        };
    }, []);

    const addAggregateMetric = (aggregateMetric: IAggregateMetric) => setAggregateMetrics(prevMetrics => [...prevMetrics, aggregateMetric]);
    
    // {components.map((component, key) => {}
    // <component key={key}/>
    // }

    const CursorVoronoiContainer = createContainer("voronoi", "cursor");

    return  <React.Fragment>
            <Title headingLevel="h1" size={TitleSizes['lg']}>
            {name}
            </Title>
            <Chart
                animate={{
                    duration: 1000,
                    easing: "bounce"
                }}
                ariaDesc={name}
                ariaTitle={name}
                containerComponent={<ChartVoronoiContainer labels={({ datum }) => `${datum.name}: ${Math.round(datum.y)}`} constrainToVisibleArea />}
                // containerComponent={
                //     <CursorVoronoiContainer
                //       cursorDimension="x"
                //       labels={({ datum }) => `${datum.y}`}
                //       labelComponent={<ChartLegendTooltip legendData={legendData} title={(datum) => datum.x}/>}
                //       mouseFollowTooltips
                //       voronoiDimension="x"
                //       voronoiPadding={50}
                //     />
                //   }
                legendData={legendData}
                // legendOrientation="vertical"
                legendPosition="bottom"
                height={350}
                // maxDomain={{y: 10}}
                // minDomain={{y: 0}}
                width={600}
                padding={{
                    bottom: 70,
                    left: 70,
                    right: 50, 
                    top: 50
                  }}
                themeColor={ChartThemeColor.multi}
                >
                <ChartAxis scale={'time'} tickCount={timePeriod/timeRange} />
                <ChartAxis dependentAxis showGrid  />
                <ChartGroup>
                    {Array.from(data.entries()).map((dataItem) => (
                    <ChartLine key={dataItem[0]} name={dataItem[0]} data={dataItem[1]} />
                    ))}
                </ChartGroup>
            </Chart>
        </React.Fragment>
    ;
}

export { LineChartByName };

