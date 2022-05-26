package org.workflowsim;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

public class MetaGetter {

    private static NormalDistribution normalDistribution;

    private static ExponentialDistribution exponentialDistribution;

    private static Random random;

    private static double error = 0.5;

    private static String workflow = "methylseq";

    private static String distribution = "normal";

    private static ArrayList<Integer> seedStack = null;

    private static int listPointer = 0;

    private static int listPointeroffset = 0;

    private static double getRandomFromNormalDist() {

        random = new Random(seedStack.get(listPointer++));

        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed(seedStack.get(listPointer++));
        normalDistribution = new NormalDistribution(rg, 1, 0.5, 1);

        double factor = -1;
        if (random.nextDouble() > 0.5) {
            factor = (1 + normalDistribution.sample() * error);
        } else {
            factor = Math.max(1 - normalDistribution.sample() * error, 0);
        }
        return factor;
    }

    private static double getRandomFromExponentialDist() {

        random = new Random(seedStack.get(listPointer++));

        RandomGenerator rg = new JDKRandomGenerator();
        rg.setSeed(seedStack.get(listPointer++));
        exponentialDistribution = new ExponentialDistribution(rg, 1, 1);

        double factor = -1;
        if (random.nextDouble() > 0.5) {
            factor = (1 + exponentialDistribution.sample() * error);
        } else {
            factor = Math.max(1 - exponentialDistribution.sample() * error, 0);
        }
        return factor;
    }

    public static double getRandomFactor() {

        BufferedReader bufferedReader = null;

        if (seedStack == null) {
            try {
                seedStack = new ArrayList<>();
                bufferedReader = new BufferedReader(new FileReader("src/main/resources/config/seed.txt"));
                Arrays.stream(bufferedReader.readLine().split(",")).forEach(item -> {
                    seedStack.add(Integer.parseInt(item));
                });
                bufferedReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (distribution.equals("exponential")) {
            return getRandomFromExponentialDist();
        } else if (distribution.equals("normal")) {
            return getRandomFromNormalDist();
        } else {
            return -1;
        }

    }


    public static void resetGenerator() {
        listPointer = 0 + listPointeroffset;

    }

    public static String getWorkflow() {
        return workflow;
    }

    public static double getError() {
        return error;
    }

    public static String getDistribution() {
        return distribution;
    }

    public static void setError(double error) {
        MetaGetter.error = error;
    }

    public static void setWorkflow(String workflow) {
        MetaGetter.workflow = workflow;
    }

    public static void setDistribution(String distribution) {
        MetaGetter.distribution = distribution;
    }

    public static void setListPointeroffset(int listPointeroffset) {
        MetaGetter.listPointeroffset = listPointeroffset;
    }
}
