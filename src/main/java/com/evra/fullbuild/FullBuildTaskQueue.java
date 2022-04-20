package com.evra.fullbuild;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.evra.fullbuild.data.FullBuildTaskData;
import com.evra.fullbuild.database.DatabaseManager;

@Path("/fullbuildtaskqueue")
public class FullBuildTaskQueue 
{
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public Map<String,String> addFullBuildTaskQueue(FullBuildTaskData taskData) {
        String apiStatus = "Success";
        Map<String,String> response = new HashMap<String,String>();
        FullBuildTaskData topTaskData = null;
        try {
            topTaskData = DatabaseManager.sharedInstance().getTopFullBuildTask();
        } catch (Exception e) {
            apiStatus = "Failure";
        }
        if(topTaskData == null)
        {
            try {
                DatabaseManager.sharedInstance().addFullBuildTask(taskData);
            } catch (Exception e) {
                apiStatus = "Failure";
                response.put("Reason", "Error while adding task to queue");
            }
        }
        else
        {
            apiStatus = "Failure";
            response.put("Reason", "A Build is Already Started");
        }

        response.put("Status", apiStatus);
        return response;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public FullBuildTaskData getTopMostFullBuildTaskQueue() {
        try {
            return DatabaseManager.sharedInstance().getTopFullBuildTask();
        } catch (Exception e) {
            return null;
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public Map<String,String> updateFullBuildTaskQueue(FullBuildTaskData taskData) {
        String apiStatus = "Success";
        try {
            DatabaseManager.sharedInstance().updateFullBuildTask(taskData);
        } catch (Exception e) {
            apiStatus = "Failure";
        }
        Map<String,String> response = new HashMap<String,String>();
        response.put("Status", apiStatus);
        return response;
    }

    @DELETE
    @Produces({ MediaType.APPLICATION_JSON })
    public Map<String,String> deleteFullBuildTaskQueue() {
        String apiStatus = "Success";
        try {
            DatabaseManager.sharedInstance().deleteTopFullBuildTask();
        } catch (Exception e) {
            apiStatus = "Failure";
        }
        Map<String,String> response = new HashMap<String,String>();
        response.put("Status", apiStatus);
        return response;
    }

}
