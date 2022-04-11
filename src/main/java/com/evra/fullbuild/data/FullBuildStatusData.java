package com.evra.fullbuild.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FullBuildStatusData {

    public enum BuildStatus {
        SUCCESS(0), FAILURE(0), PROCESSING(0);
        private final int value;
        BuildStatus(int i) {
            this.value = i;
        }
        public int getValue() {
            return value;
        }
    }
    
    public enum BuildDeviceType {
        DEVICE(0), SIMULATOR(1);
        private final int value;
        BuildDeviceType(int i) {
            this.value = i;
        }
        public int getValue() {
            return value;
        }
    }
    
    public enum BuildConfiguration {
        DEBUG(0), SHIP(1);
        private final int value;
        BuildConfiguration(int i) {
            this.value = i;
        }
        public int getValue() {
            return value;
        }
    }

    private String enlistmentName;
    private Integer buildStatus;
    private String buildStartTime;
    private String buildDuration;
    private Integer buildConfiguration;
    private Integer buildDeviceType;
    

    /**
     * @return String return the enlistmentName
     */
    public String getEnlistmentName() {
        return enlistmentName;
    }

    /**
     * @param enlistmentName the enlistmentName to set
     */
    public void setEnlistmentName(String enlistmentName) {
        this.enlistmentName = enlistmentName;
    }

    /**
     * @return BuildStatus return the buildStatus
     */
    public BuildStatus getBuildStatus() {
        return BuildStatus.values()[buildStatus];
    }

    /**
     * @param buildStatus the buildStatus to set
     */
    public void setBuildStatus(Integer buildStatus) {
        this.buildStatus = buildStatus;
    }

    /**
     * @return String return the buildStartTime
     */
    public String getBuildStartTime() {
        return buildStartTime;
    }

    /**
     * @param buildStartTime the buildStartTime to set
     */
    public void setBuildStartTime(String buildStartTime) {
        this.buildStartTime = buildStartTime;
    }

    /**
     * @return String return the buildDuration
     */
    public String getBuildDuration() {
        return buildDuration;
    }

    /**
     * @param buildDuration the buildDuration to set
     */
    public void setBuildDuration(String buildDuration) {
        this.buildDuration = buildDuration;
    }

    /**
     * @return BuildConfiguration return the buildConfiguration
     */
    public BuildConfiguration getBuildConfiguration() {
        return BuildConfiguration.values()[buildConfiguration];
    }

    /**
     * @param buildConfiguration the buildConfiguration to set
     */
    public void setBuildConfiguration(Integer buildConfiguration) {
        this.buildConfiguration = buildConfiguration;
    }

    /**
     * @return     public BuildDeviceType getBuildDeviceType() {
 return the buildDeviceType
     */
    public BuildDeviceType getBuildDeviceType() {
        return BuildDeviceType.values()[buildDeviceType];
    }

    /**
     * @param buildDeviceType the buildDeviceType to set
     */
    public void setBuildDeviceType(Integer buildDeviceType) {
        this.buildDeviceType = buildDeviceType;
    }

}
