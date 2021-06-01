#!/bin/sh

export PROJECT_NAME=eda-workshop
export DEPLOYMENT_NAME=dashboard

export TELEPRESENCE_USE_OCP_IMAGE=NO
oc project ${PROJECT_NAME}
telepresence --swap-deployment ${DEPLOYMENT_NAME} --expose 8080
