/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uma.jmetal.experiment;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.impl.SymbolicExpressionGrammarProblem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.fileoutput.SolutionSetOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 *
 * @author thaina
 */
public class SymbolicExpressionGrammarExperiment {
    
    Problem problem = new SymbolicExpressionGrammarProblem("symbolicexpression.bnf");

    Algorithm algorithm = new GenerationalGeneticAlgorithm
            

    //NonDominatedSolutionList nonDominatedSolutions = new NonDominatedSolutionList();
    NonDominatedSolutionListArchive nonDominatedSolutions = new NonDominatedSolutionListArchive();
    String path = String.format("experiment/%s/%s/%s-%s-%s", ExperimentUtil.getInstanceName(mutationParameters.getInstance()), mutationParameters.getAlgo(), mutationParameters.getPopulationSize(), mutationParameters.getGenerations(), String.valueOf(mutationParameters.getBeta()));
    List<Integer> numberOfTimesAppliedAllRuns = new ArrayList<>();
    FileWriter fileWriter = new FileWriter(path + "/HHResults");

            NonDominatedSolutionListArchive actualNonDominatedSolutions = new NonDominatedSolutionListArchive();

        System.out.println("Run: " + i);

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
        List<Solution> population = ((HHNSGAIII) algorithm).getResult();
        long computingTime = algorithmRunner.getComputingTime();
        System.out.println("Total time of execution: " + computingTime);

        /* Log messages */
        String pathFun = String.format("%s/FUN_%s", path, i);
        String pathVar = String.format("%s/VAR_%s", path, i);

        for (Solution solution : population) {
            nonDominatedSolutions.add(solution);
            actualNonDominatedSolutions.add(solution);
        }

        System.out.println("Variables values have been writen to file " + pathVar);
        System.out.println("Objectives values have been writen to file " + pathFun);
        new SolutionSetOutput.Printer(actualNonDominatedSolutions.getSolutionList())
                .setSeparator("\t")
                .setVarFileOutputContext(new DefaultFileOutputContext(pathVar))
                .setFunFileOutputContext(new DefaultFileOutputContext(pathFun))
                .print();

}
