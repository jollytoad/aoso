package com.jonmort.plugin.aoso;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Path("/store/{scope}/{generic}/{specific}/{dataid}")
public class StoreResource {
    private final StorageService storageService;

    public StoreResource(StorageService storageService) {
        this.storageService = storageService;
    }


    @GET
    public Response get(@PathParam("scope") String scope,
                        @PathParam("generic") String generic,
                        @PathParam("specific") String specific,
                        @PathParam("dataid") String dataid) {
        Storage storage = storageService.get(scope, generic, specific, dataid);
        if (storage != null) {
            return Response.ok(StorageRepresentation.fromStorage(storage)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    public Response put(@PathParam("scope") String scope,
                        @PathParam("generic") String generic,
                        @PathParam("specific") String specific,
                        @PathParam("dataid") String dataid,
                        String data) {
        Storage storage = storageService.get(scope, generic, specific, dataid);
        if (storage == null) {
            storageService.create(scope, generic, specific, dataid, data);
        } else {
            update(scope, generic, specific, dataid, data);
        }
        final URI location = UriBuilder.fromResource(StoreResource.class).build(scope, generic, specific, dataid);
        return Response.created(location).build();
    }

    @POST
    public Response update(@PathParam("scope") String scope,
                           @PathParam("generic") String generic,
                           @PathParam("specific") String specific,
                           @PathParam("dataid") String dataid,
                           String data) {
        Storage storage = storageService.get(scope, generic, specific, dataid);
        if (storage != null) {
            storage.setData(data);
            storageService.store(storage);
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    public Response remove(@PathParam("scope") String scope,
                           @PathParam("generic") String generic,
                           @PathParam("specific") String specific,
                           @PathParam("dataid") String dataid) {
        Storage storage = storageService.get(scope, generic, specific, dataid);
        if (storage != null) {
            storageService.remove(storage);
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
