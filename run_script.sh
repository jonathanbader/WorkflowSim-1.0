#!/bin/bash

nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq normal 0.05 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq normal 0.10 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq normal 0.15 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq normal 0.20 &

nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq exponential 0.05 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq exponential 0.10 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq exponential 0.15 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar methylseq exponential 0.20 &

nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager normal 0.05 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager normal 0.10 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager normal 0.15 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager normal 0.20 &

nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager exponential 0.05 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager exponential 0.10 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager exponential 0.15 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar eager exponential 0.20 &

nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar chipseq normal 0.05 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar chipseq normal 0.10 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar chipseq normal 0.15 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar chipseq normal 0.20 &

nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar chipseq exponential 0.05 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar chipseq exponential 0.10 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar chipseq exponential 0.15 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar chipseq exponential 0.20 &

nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar sarek normal 0.05 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar sarek normal 0.10 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar sarek normal 0.15 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar sarek normal 0.20 &

nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar sarek exponential 0.05 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar sarek exponential 0.10 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar sarek exponential 0.15 &
nohup java -jar WorkflowSim-1.0-1.0-SNAPSHOT.jar sarek exponential 0.20 &