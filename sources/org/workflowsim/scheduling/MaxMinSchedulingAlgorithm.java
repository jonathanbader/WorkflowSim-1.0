/**
 * Copyright 2012-2013 University Of Southern California
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.workflowsim.scheduling;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.workflowsim.CondorVM;
import org.workflowsim.Job;
import org.workflowsim.Task;
import org.workflowsim.WorkflowSimTags;

/**
 * MaxMin algorithm.
 *
 * @author Weiwei Chen
 * @since WorkflowSim Toolkit 1.0
 * @date Apr 9, 2013
 */
public class MaxMinSchedulingAlgorithm extends BaseSchedulingAlgorithm {

    List<LinkedHashMap<String, Object>> arr;

    NormalDistribution normalDistribution = new NormalDistribution(1, 0.5);
    Random random = new Random();

    /**
     * Initialize a MaxMin scheduler.
     */
    public MaxMinSchedulingAlgorithm() {
        super();
    }

    /**
     * the check point list.
     */
    private final List<Boolean> hasChecked = new ArrayList<>();

    @Override
    public void run() {

        maxmin();

        /*
        //Log.printLine("Schedulin Cycle");
        int size = getCloudletList().size();
        hasChecked.clear();
        for (int t = 0; t < size; t++) {
            hasChecked.add(false);
        }
        for (int i = 0; i < size; i++) {
            int maxIndex = 0;
            Cloudlet maxCloudlet = null;
            for (int j = 0; j < size; j++) {
                Cloudlet cloudlet = (Cloudlet) getCloudletList().get(j);
                if (!hasChecked.get(j)) {
                    maxCloudlet = cloudlet;
                    maxIndex = j;
                    break;
                }
            }
            if (maxCloudlet == null) {
                break;
            }

            for (int j = 0; j < size; j++) {
                Cloudlet cloudlet = (Cloudlet) getCloudletList().get(j);
                if (hasChecked.get(j)) {
                    continue;
                }
                long length = cloudlet.getCloudletLength();
                if (length > maxCloudlet.getCloudletLength()) {
                    maxCloudlet = cloudlet;
                    maxIndex = j;
                }
            }
            hasChecked.set(maxIndex, true);

            int vmSize = getVmList().size();
            CondorVM firstIdleVm = null;//(CondorVM)getVmList().get(0);
            for (int j = 0; j < vmSize; j++) {
                CondorVM vm = (CondorVM) getVmList().get(j);
                if (vm.getState() == WorkflowSimTags.VM_STATUS_IDLE) {
                    firstIdleVm = vm;
                    break;
                }
            }
            if (firstIdleVm == null) {
                break;
            }
            for (int j = 0; j < vmSize; j++) {
                CondorVM vm = (CondorVM) getVmList().get(j);
                if ((vm.getState() == WorkflowSimTags.VM_STATUS_IDLE)
                        && vm.getCurrentRequestedTotalMips() > firstIdleVm.getCurrentRequestedTotalMips()) {
                    firstIdleVm = vm;

                }
            }
            firstIdleVm.setState(WorkflowSimTags.VM_STATUS_BUSY);
            maxCloudlet.setVmId(firstIdleVm.getId());
            getScheduledList().add(maxCloudlet);
            Log.printLine("Schedules " + maxCloudlet.getCloudletId() + " with "
                    + maxCloudlet.getCloudletLength() + " to VM " + firstIdleVm.getId()
                    + " with " + firstIdleVm.getCurrentRequestedTotalMips());

        }
         */
    }

    public void maxmin() {

        ArrayList<Integer> checked = new ArrayList<>();

        List<Job> cloudlets = new ArrayList<>(getCloudletList());

        List<CondorVM> vmList = getVmList();

        int size = cloudlets.size();

        if (getCloudletList().size() == 0) {
            return;
        }

        for (int i = 0; i < size; i++) {

            Task minTask = null;

            CondorVM minVm = null;

            long minTime = Long.MAX_VALUE;

            if (cloudlets.get(i).getTaskList().size() == 0) {
                minVm = vmList.get((int) Math.round(random.nextDouble() * (vmList.size() - 1)));
                minTask = cloudlets.get(i);
                checked.add(minTask.getCloudletId());
                Task finalMinTask = minTask;
                Job minJob = cloudlets.stream().filter(c -> c.getCloudletId() == finalMinTask.getCloudletId()).collect(Collectors.toList()).get(0);
                minJob.setVmId(minVm.getId());
                minVm.setState(WorkflowSimTags.VM_STATUS_BUSY);
                getScheduledList().add(minJob);
                return;
            }

            System.out.println("Filter:" + cloudlets.stream().filter(c -> checked.contains(c.getCloudletId())).collect(Collectors.toList()));
            for (Job job : cloudlets.stream().filter(c -> !checked.contains(c.getTaskList().get(0).getCloudletId())).collect(Collectors.toList())) {
                System.out.println("Has checked:" + checked);
                Task task = job.getTaskList().get(0);
                System.out.println("Job:" + task.getType() + "(" + job.getCloudletId() + ")");

                List<CondorVM> freeVMs = new ArrayList<>();
                for (int l = 0; l < vmList.size(); l++) {
                    CondorVM vm = vmList.get(l);
                    if (vm.getState() == WorkflowSimTags.VM_STATUS_IDLE) {
                        freeVMs.add(vm);
                    }
                }

                for (CondorVM vm : freeVMs) {

                    AtomicInteger runtimeSum = new AtomicInteger();
                    AtomicInteger count = new AtomicInteger();
                    this.arr.forEach(entry -> {

                        if (task.getType().contains(((String) entry.get("taskName"))) &&
                                vm.getName().equals((String) entry.get("instanceType")) &&
                                ((String) entry.get("wfName")).contains(task.getWorkflow())) {
                            runtimeSum.addAndGet((Integer) entry.get("realtime"));
                            count.getAndIncrement();
                        }
                    });

                    long lengthWithNoise;

                    if (random.nextDouble() > 0.5 && count.get() != 0) {
                        lengthWithNoise = (long) ((runtimeSum.get() / count.get()) * (1 + normalDistribution.sample() * 0.15));
                    } else if (count.get() != 0) {
                        lengthWithNoise = (long) ((runtimeSum.get() / count.get()) * (1 - normalDistribution.sample() * 0.15));
                    } else {
                        lengthWithNoise = (long) (task.getCloudletLength() * (1 - normalDistribution.sample() * 0.15));
                    }

                    if (minTime < lengthWithNoise) {
                        minTask = task;
                        minVm = vm;
                        minTime = lengthWithNoise;
                    }

                }
            }
            System.out.println("Assgined: " + minTask.getType() + "(" + minTask.getCloudletId() + ")");
            checked.add(minTask.getCloudletId());
            Task finalMinTask = minTask;
            Job minJob = cloudlets.stream().filter(c -> c.getTaskList().get(0).getCloudletId() == finalMinTask.getCloudletId()).collect(Collectors.toList()).get(0);
            minJob.setVmId(minVm.getId());
            minVm.setState(WorkflowSimTags.VM_STATUS_BUSY);
            getScheduledList().add(minJob);
        }
    }
}
