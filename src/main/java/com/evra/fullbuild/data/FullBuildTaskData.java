package com.evra.fullbuild.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FullBuildTaskData 
{
    public enum TaskStatus {
        CREATED(0), STARTED(1), COMPLETED(2);
        private final int value;
        TaskStatus(int i) {
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

    private String taskId;
    private String enlistmentName;
    private Integer buildConfiguration;
    private Integer buildDeviceType;
    private Integer taskStatus;

    /**
     * @return String return the taskId
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

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

    /**
     * @return Integer return the taskStatus
     */
    public TaskStatus getTaskStatus() {
        return TaskStatus.values()[taskStatus];
    }

    /**
     * @param taskStatus the taskStatus to set
     */
    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

}
