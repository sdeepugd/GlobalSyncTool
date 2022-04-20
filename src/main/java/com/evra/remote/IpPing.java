package com.evra.remote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.evra.fullbuild.database.DatabaseManager;

@Path("/ipping")
public class IpPing {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public Map<String,String> ping(IpResult ipResult) {
        Map<String,String> response = new HashMap<String,String>();
        try {
            DatabaseManager.sharedInstance().deleteTopIpStatus();
            DatabaseManager.sharedInstance().addIpStatus(ipResult);
            response.put("Status", "Success");
        } catch (Exception e) {
            response.put("Status", "Failure");
        }
        return response;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public IpResult getIpResult() {
        try {
            return DatabaseManager.sharedInstance().getIpResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
