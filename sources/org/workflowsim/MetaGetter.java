package org.workflowsim;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class MetaGetter {

    private static NormalDistribution normalDistribution;

    private static ExponentialDistribution exponentialDistribution;

    private static Random random;

    private static long seed = -1L;

    private static double error = 0.15;

    private static String workflow = "methylseq";

    private static double getRandomFromNormalDist() {

        BufferedReader bufferedReader = null;
        try {

            if (seed == -1L) {
                bufferedReader = new BufferedReader(new FileReader("seed.txt"));
                seed = Long.parseLong(bufferedReader.readLine());
                bufferedReader.close();
            }
            random = new Random(seed);

            RandomGenerator rg = new JDKRandomGenerator();
            rg.setSeed(seed);
            seed += 3000;
            normalDistribution = new NormalDistribution(rg, 1, 0.5, 1);

            double factor = -1;
            if (random.nextDouble() > 0.5) {
                factor = (1 + normalDistribution.sample() * error);
            } else {
                factor = Math.max(1 - normalDistribution.sample() * error, 0);
            }
            return factor;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static double getRandomFromExponentialDist() {

        BufferedReader bufferedReader = null;

        try {
            if (seed == -1L) {
                bufferedReader = new BufferedReader(new FileReader("seed.txt"));
                seed = Long.parseLong(bufferedReader.readLine());
                bufferedReader.close();
            }
            random = new Random(seed);

            RandomGenerator rg = new JDKRandomGenerator();
            rg.setSeed(seed);
            seed += 1000;
            exponentialDistribution = new ExponentialDistribution(rg, 1, 1);

            double factor = -1;
            if (random.nextDouble() > 0.5) {
                factor = (1 + exponentialDistribution.sample() * error);
            } else {
                factor = Math.max(1 - exponentialDistribution.sample() * error, 0);
            }
            return factor;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static double getRandomFactor() {
        return getRandomFromExponentialDist();
    }


    public static void resetGenerator() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("seed.txt"));
            seed = Long.parseLong(bufferedReader.readLine());
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static String getWorkflow() {
        return workflow;
    }
}
