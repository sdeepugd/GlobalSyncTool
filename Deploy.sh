#!/bin/bash
mvn clean -f "/Users/desriniv/Documents/Analysis/Java/Tomcat/GlobalSyncTool/GlobalSyncTool/globalsynctool/pom.xml" &&
mvn package -f "/Users/desriniv/Documents/Analysis/Java/Tomcat/GlobalSyncTool/GlobalSyncTool/globalsynctool/pom.xml" &&
docker build --pull --rm -f "Dockerfile" -t globalsynctool "." &&
docker login SyncTool.azurecr.io --username SyncTool -p Tf2dJ5JC3+H9WSEpMyKJusns1R1qIOo0 &&
docker tag globalsynctool SyncTool.azurecr.io/globalsynctool &&
docker push SyncTool.azurecr.io/globalsynctool