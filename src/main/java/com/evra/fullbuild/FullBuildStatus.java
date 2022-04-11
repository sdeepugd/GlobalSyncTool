package com.evra.fullbuild;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.evra.fullbuild.data.FullBuildStatusData;
import com.evra.fullbuild.database.DatabaseManager;


@Path("/fullbuildstatus")
public class FullBuildStatus {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addFullBuildStatus(FullBuildStatusData status) {
        DatabaseManager.sharedInstance().addFullbuildStatus(status);
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public FullBuildStatusData getFullBuildStatus() {
        FullBuildStatusData status = new FullBuildStatusData();
        status.setEnlistmentName("EnlistmentName");
        status.setBuildStatus(0);
        status.setBuildStartTime("BuildStartTime");
        status.setBuildDuration("BuildDuration");
        status.setBuildConfiguration(0);
        status.setBuildDeviceType(0);
        return status;
    }

}
