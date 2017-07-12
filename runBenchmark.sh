#!/bin/sh

chmod 755 src/main/resources/insecureCmd.sh
mvn clean package cargo:run -Pdeploy -Dhdiv.agent=$1 -Dhdiv.config.dir=$2