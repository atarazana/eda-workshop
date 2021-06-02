//package com.redhat.eda;
//
//import io.quarkus.infinispan.client.Remote;
//import org.infinispan.client.hotrod.RemoteCache;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.inject.Inject;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//
//@Path("/clients")
//public class ClientDomainResource {
//
//    private static final Logger LOG = LoggerFactory.getLogger(ClientDomainResource.class);
//
//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public String hello() {
//        return "Hello RESTEasy";
//    }
//
//    @Inject
//    @Remote("clients")
//    RemoteCache<Integer, String> cache;
//
//    @GET
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getClientById(@PathParam("id") String id) {
//        LOG.info("Getting client from DataGrid: {}", id);
//
//        return cache.get(Integer.valueOf(id));
//    }
//
//}
