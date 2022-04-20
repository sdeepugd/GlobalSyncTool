package com.evra.fullbuild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.evra.fullbuild.data.FullBuildStatusData;
import com.evra.fullbuild.database.DatabaseManager;


@Path("/fullbuildstatus")
public class FullBuildStatus {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public Map<String,String> addFullBuildStatus(FullBuildStatusData status) {
        String apiStatus = "Success";
        if(checkEnlistmentAlreadyExists(status))
        {
            try {
                DatabaseManager.sharedInstance().updateFullBuildStatus(status);
            } catch (Exception e) {
                apiStatus = "Failure";
            }
        }
        else{
            try {
                DatabaseManager.sharedInstance().addFullbuildStatus(status);
            } catch (Exception e) {
                apiStatus = "Failure";
            }
        }
        Map<String,String> response = new HashMap<String,String>();
        response.put("Status", apiStatus);
        return response;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<FullBuildStatusData> getFullBuildStatus() {
        try {
            return DatabaseManager.sharedInstance().getFullbuildStatus();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON })
    public Map<String,String> updateFullBuildStatus(FullBuildStatusData status) {
        
        String apiStatus = "Success";
        try {
            DatabaseManager.sharedInstance().updateFullBuildStatus(status);
        } catch (Exception e) {
            apiStatus = "Failure";
        }

        Map<String,String> response = new HashMap<String,String>();
        response.put("Status", apiStatus);
        return response;
    }

    public Boolean checkEnlistmentAlreadyExists(FullBuildStatusData passedStatus)
    {
        Boolean enlistmentAlreadyExists = false;
        List<FullBuildStatusData> fullBuildStatusList = new ArrayList<FullBuildStatusData>();
        try {
            fullBuildStatusList = DatabaseManager.sharedInstance().getFullbuildStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(FullBuildStatusData fullBuildStatus : fullBuildStatusList)
        {
            if(fullBuildStatus.getEnlistmentName().equals(passedStatus.getEnlistmentName()))
            {
                enlistmentAlreadyExists = true;
                break;
            }
        }
        return enlistmentAlreadyExists;
    }

}
