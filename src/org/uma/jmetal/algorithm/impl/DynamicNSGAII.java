package org.uma.jmetal.algorithm.impl;

import java.util.List;
import org.uma.jmetal.algorithm.components.impl.EvaluationsCountProgress;
import org.uma.jmetal.algorithm.components.impl.MaxEvaluationsCondition;
import org.uma.jmetal.algorithm.components.impl.OnlyPopulationSelection;
import org.uma.jmetal.algorithm.components.impl.ParetoRankingAndCrowdingDistanceReplacement;
import org.uma.jmetal.algorithm.components.impl.RandomPopulationInitialization;
import org.uma.jmetal.algorithm.components.impl.TwoSolutionsReproduction;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

public class DynamicNSGAII<S extends Solution<?>> extends DefaultDynamicGeneticAlgorithm<S> {

    public DynamicNSGAII(Problem<S> problem, int maxEvaluations, int populationSize,
            CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
            SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator) {
        super(problem,
                populationSize,
                new EvaluationsCountProgress(),
                new MaxEvaluationsCondition(maxEvaluations),
                new RandomPopulationInitialization<>(),
                evaluator,
                new OnlyPopulationSelection<>(),
                new TwoSolutionsReproduction<>(),
                new ParetoRankingAndCrowdingDistanceReplacement<>(),
                null,
                selectionOperator,
                crossoverOperator,
                mutationOperator);
    }

}