package org.workflowsim.scheduling;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.workflowsim.CondorVM;
import org.workflowsim.Job;
import org.workflowsim.Task;
import org.workflowsim.WorkflowSimTags;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ReshiSchedulingAlgorithm extends BaseSchedulingAlgorithm {


    List<ReshiTask> reshiTaskList;

    private ReshiStrategy reshiStrategy;

    NormalDistribution normalDistribution;
    Random random;

    public ReshiSchedulingAlgorithm(ReshiStrategy reshiStrategy) {

        this.reshiStrategy = reshiStrategy;
        reshiTaskList = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("/home/joba/IdeaProjects/WorkflowSim-1.0/config/ranking/ranks_methylseq_Stochastic Gradient Descent_3.csv"))) {
            String s = null;
            bufferedReader.readLine();
            while ((s = bufferedReader.readLine()) != null) {
                String[] entries = s.split(",");
                reshiTaskList.add(new ReshiTask(entries[1], entries[0], entries[2], Double.parseDouble(entries[3])));
            }

            BufferedReader seedReader = new BufferedReader(new FileReader("seed.txt"));
            long seed = Long.parseLong(seedReader.readLine());
            seedReader.close();
            random = new Random(seed);

            RandomGenerator rg = new JDKRandomGenerator();
            rg.setSeed(seed);

            normalDistribution = new NormalDistribution(rg, 1, 0.5, 1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    @Override
    public void run() throws Exception {

        List<Job> cloudlets = getCloudletList();

        List<CondorVM> vmList = getVmList();

        // V1
        if (reshiStrategy == ReshiStrategy.V1) {
            Collections.sort(cloudlets, (j1, j2) -> {

                int descandantsJ1 = j1.getChildList().size();
                int descandantsJ2 = j2.getChildList().size();

                if (descandantsJ1 < descandantsJ2) {
                    return 1;
                } else if (descandantsJ1 > descandantsJ2) {
                    return -1;
                } else {
                    return 0;
                }

            });
        } else if (reshiStrategy == ReshiStrategy.V2) {
            // V2
            Collections.sort(cloudlets, (j1, j2) -> {

                int descandantsJ1 = j1.getChildList().size();
                int descandantsJ2 = j2.getChildList().size();

                if (descandantsJ1 < descandantsJ2) {
                    return 1;
                } else if (descandantsJ1 > descandantsJ2) {
                    return -1;
                } else {
                    return 0;
                }
            });
        } else if (reshiStrategy == ReshiStrategy.V3) {
            //V3
            Collections.sort(cloudlets, (j1, j2) -> {

                int descandantsJ1 = depthDescendants(j1,0);
                int descandantsJ2 = depthDescendants(j2,0);

                if (descandantsJ1 < descandantsJ2) {
                    return 1;
                } else if (descandantsJ1 > descandantsJ2) {
                    return -1;
                } else {
                    return 0;
                }
            });
        }
        System.out.println("Sort:" + cloudlets.stream().map(c -> c.getChildList().size()).collect(Collectors.toList()));

        for (Job task : cloudlets) {


            List<CondorVM> freeVMs = new ArrayList<>();
            for (int l = 0; l < vmList.size(); l++) {
                CondorVM vm = vmList.get(l);
                if (vm.getState() == WorkflowSimTags.VM_STATUS_IDLE) {
                    freeVMs.add(vm);
                }
            }

            if (freeVMs.size() == 0) {
                break;
            }

            String nodeToSchedule = determineBestMachine(task, freeVMs).node;

            CondorVM vmToAssign = freeVMs.stream().filter(vm -> vm.getName().equals(nodeToSchedule)).findFirst().get();

            if (vmToAssign == null) {
                break;
            }
            vmToAssign.setState(WorkflowSimTags.VM_STATUS_BUSY);
            task.setVmId(vmToAssign.getId());
            getScheduledList().add(task);


        }

    }


    private ReshiTask determineBestMachine(Job taskToLookup, List<CondorVM> freeVMs) {

        // Für den initialen Task, welcher die Files fetcht wird random eine Node ausgewählt
        if (taskToLookup.getTaskList().size() == 0) {
            List<ReshiTask> filteredList = reshiTaskList.stream().filter(t -> freeVMs.stream().map(vm -> vm.getName()).collect(Collectors.toList()).contains(t.node))
                    .collect(Collectors.toList());
            //Collections.shuffle(filteredList);
            return filteredList.get(0);
        }
        // Ranking nach dem Task filtern und sortieren
        List<ReshiTask> filteredList = reshiTaskList.stream().filter(t -> t.taskName.equals(taskToLookup.getTaskList().get(0).getType()))
                .filter(t -> freeVMs.stream().map(vm -> vm.getName()).collect(Collectors.toList()).contains(t.node))
                .collect(Collectors.toList());

        // falls der Task nicht gerankt wurde
        if (filteredList.size() == 0) {
            List<ReshiTask> list = reshiTaskList.stream().filter(t -> freeVMs.stream().map(vm -> vm.getName()).collect(Collectors.toList()).contains(t.node))
                    .collect(Collectors.toList());
            // Collections.shuffle(list);
            return list.get(0);
        }


        Collections.sort(filteredList);
        return filteredList.get(0);
    }


    private class ReshiTask implements Comparable {

        String workflow;
        String taskName;
        String node;
        Double rank;

        public ReshiTask(String taskName, String workflow, String node, Double rank) {
            this.workflow = workflow;
            this.taskName = taskName;
            this.node = node;
            this.rank = rank;
        }

        @Override
        public int compareTo(Object o) {
            if (this.rank < ((ReshiTask) o).rank) {
                return 1;
            } else if (this.rank > ((ReshiTask) o).rank) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    // V2
    private int totalDescendants(Task job) {

        int acc = job.getChildList().size();

        for (Task t : job.getChildList()) {
            acc = totalDescendants(t) + acc;
        }

        return acc;
    }

    // V3
    private static int depthDescendants(Task job, int depth) {

        for (Task t : job.getChildList()) {
            return Math.max(depthDescendants(t, depth + 1), depth);
        }
        return depth;
    }


}
