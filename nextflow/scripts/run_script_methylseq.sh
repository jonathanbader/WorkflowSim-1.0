#!/bin/bash

nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq normal 0 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq normal 0.05 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq normal 0.10 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq normal 0.15 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq normal 0.20 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq normal 0.25 &

nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq exponential 0 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq exponential 0.05 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq exponential 0.10 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq exponential 0.15 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq exponential 0.20 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq exponential 0.25 &
