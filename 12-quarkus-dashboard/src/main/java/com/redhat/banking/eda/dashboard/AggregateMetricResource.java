package com.redhat.banking.eda.dashboard;

import com.redhat.banking.eda.model.dto.AggregateMetricDTO;
import io.quarkus.infinispan.client.Remote;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.dsl.QueryResult;
import org.jboss.resteasy.reactive.RestSseElementType;
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
    Multi<AggregateMetricDTO> metrics;

    @Inject
    @Remote("aggregate-metrics")
    RemoteCache<String, AggregateMetricDTO> cache;

    RemoteCacheManager remoteCacheManager;

    @Inject
    AggregateMetricResource(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }

    @GET
    public List<AggregateMetricDTO> allAggregateMetrics() {
        LOG.info("allAggregateMetrics");

        ArrayList<AggregateMetricDTO> aggregateMetrics = new ArrayList<AggregateMetricDTO>();
        for (Map.Entry<String, AggregateMetricDTO> alert : cache.entrySet()) {
            aggregateMetrics.add(alert.getValue());
        }

        LOG.info("size = " + cache.keySet().size());

        return aggregateMetrics;
    }

    @GET
    @Path("/by-name")
    public List<AggregateMetricDTO> aggregateMetricsByName(@QueryParam("name") String name,
                                                           @QueryParam("groupByClause") String groupByClause,
                                                           @DefaultValue("300") @QueryParam("period") int periodInSeconds) {
        LOG.info("aggregateMetricsByName");

        QueryFactory queryFactory = Search.getQueryFactory(cache);
        String groupByClauseFull = groupByClause != null ? "AND groupByClause = '" + groupByClause + "'" : "";
        Query<AggregateMetricDTO> query = queryFactory.create(
                "FROM eda.workshop.AggregateMetricDTO WHERE name = '" + name + "' " +
                        groupByClauseFull +
                        " ORDER BY timestamp");

        // Execute the query
        QueryResult<AggregateMetricDTO> queryResult = query.execute();

        LOG.info("size = " + queryResult.hitCount());

        return queryResult.list()
                .stream()
                .filter(c -> c.getTimestamp().compareTo(Instant.now().minusSeconds(periodInSeconds)) > 0)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/by-name-region")
    public List<AggregateMetricDTO> aggregateMetricsByNameAndRegion(@QueryParam("name") String name,
                                                                    @QueryParam("region") String region) {
        LOG.info("aggregateMetricsByName");

        QueryFactory queryFactory = Search.getQueryFactory(cache);
        Query<AggregateMetricDTO> query = queryFactory.create(
                "FROM eda.workshop.AggregateMetricDTO WHERE name = '" + name + "' and from = '" + region
                        + "' ORDER BY timestamp");

        // Execute the query
        QueryResult<AggregateMetricDTO> queryResult = query.execute();

        LOG.info("size = " + queryResult.hitCount());

        return queryResult.list();
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestSseElementType(MediaType.APPLICATION_JSON)
    public Multi<AggregateMetricDTO> stream() {
        return metrics;
    }

}
