package com.redhat.banking.eda.dashboard;

import com.redhat.banking.eda.dashboard.valueobjects.AggregateMetric;
import io.quarkus.infinispan.client.Remote;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.dsl.QueryResult;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A simple resource retrieving the in-memory "my-data-stream" and sending the items as server-sent events.
 */
@Path("/aggregate-metrics")
public class AggregateMetricResource {

    private static final Logger LOG = LoggerFactory.getLogger(AggregateMetricResource.class);

    @Inject
    @Channel("aggregate-metrics-stream")
    Publisher<AggregateMetric> metrics;

    @Inject
    @Remote("aggregate-metrics")
    RemoteCache<String, AggregateMetric> cache;

    RemoteCacheManager remoteCacheManager;

    @Inject
    AggregateMetricResource(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    @GET
    public List<AggregateMetric> allAggregateMetrics() {
        LOG.info("allAggregateMetrics");

        ArrayList<AggregateMetric> aggregateMetrics = new ArrayList<AggregateMetric>();
        for (Map.Entry<String, AggregateMetric> alert : cache.entrySet()) {
            aggregateMetrics.add(alert.getValue());
        }

        LOG.info("size = " + cache.keySet().size());

        return aggregateMetrics;
    }

    @GET
    @Path("/by-name")
    public List<AggregateMetric> aggregateMetricsByName(@QueryParam("name") String name,
                                                        @QueryParam("groupByClause") String groupByClause,
                                                        @DefaultValue("30") @QueryParam("period") int period) {
        LOG.info("aggregateMetricsByName");

        QueryFactory queryFactory = Search.getQueryFactory(cache);
        String groupByClauseFull = groupByClause != null ? "AND groupByClause = '" + groupByClause + "'" : "";
        Query<AggregateMetric> query = queryFactory.create(
                "FROM eda.workshop.AggregateMetric WHERE name = '" + name + "' " +
                        groupByClauseFull +
                        " ORDER BY timestamp");

        // Execute the query
        QueryResult<AggregateMetric> queryResult = query.execute();

        LOG.info("size = " + queryResult.hitCount());

        return queryResult.list()
                .stream()
                .filter(c -> c.getTimestamp().compareTo(Instant.now().minusSeconds(period)) > 0)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/by-name-region")
    public List<AggregateMetric> aggregateMetricsByNameAndRegion(@QueryParam("name") String name,
                                                                 @QueryParam("region") String region) {
        LOG.info("aggregateMetricsByName");

        QueryFactory queryFactory = Search.getQueryFactory(cache);
        Query<AggregateMetric> query = queryFactory.create(
                "FROM eda.workshop.AggregateMetric WHERE name = '" + name + "' and from = '" + region
                        + "' ORDER BY timestamp");

        // Execute the query
        QueryResult<AggregateMetric> queryResult = query.execute();

        LOG.info("size = " + queryResult.hitCount());

        return queryResult.list();
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<AggregateMetric> stream() {
        return metrics;
    }

}
