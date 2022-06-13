#!/bin/bash

nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager normal 0 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager normal 0.05 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager normal 0.10 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager normal 0.15 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager normal 0.20 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager normal 0.25 &

nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager exponential 0 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager exponential 0.05 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager exponential 0.10 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager exponential 0.15 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager exponential 0.20 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager exponential 0.25 &
