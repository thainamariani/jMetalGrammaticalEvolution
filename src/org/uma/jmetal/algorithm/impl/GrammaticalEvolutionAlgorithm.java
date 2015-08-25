package org.uma.jmetal.algorithm.impl;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import java.util.*;
import org.uma.jmetal.operator.impl.mutation.DuplicationMutation;
import org.uma.jmetal.operator.impl.mutation.PruneMutation;
import org.uma.jmetal.solution.impl.VariableIntegerSolution;

/**
 * Created by ajnebro on 26/10/14.
 */
public class GrammaticalEvolutionAlgorithm extends AbstractGrammaticalEvolutionAlgorithm<VariableIntegerSolution, VariableIntegerSolution> {

    private Comparator<VariableIntegerSolution> comparator;
    private int maxEvaluations;
    private int populationSize;
    private int evaluations;

    private Problem<VariableIntegerSolution> problem;

    private SolutionListEvaluator<VariableIntegerSolution> evaluator;

    /**
     * Constructor
     *
     * @param problem
     * @param maxEvaluations
     * @param populationSize
     * @param crossoverOperator
     * @param mutationOperator
     * @param selectionOperator
     * @param pruneMutationOperator
     * @param duplicationMutationOperator
     * @param evaluator
     */
    public GrammaticalEvolutionAlgorithm(Problem<VariableIntegerSolution> problem, int maxEvaluations, int populationSize,
            CrossoverOperator<VariableIntegerSolution> crossoverOperator, MutationOperator<VariableIntegerSolution> mutationOperator,
            SelectionOperator<List<VariableIntegerSolution>, VariableIntegerSolution> selectionOperator,
            PruneMutation pruneMutationOperator, DuplicationMutation duplicationMutationOperator,
            SolutionListEvaluator<VariableIntegerSolution> evaluator) {
        this.problem = problem;
        this.maxEvaluations = maxEvaluations;
        this.populationSize = populationSize;

        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
        this.selectionOperator = selectionOperator;

        this.pruneMutationOperator = pruneMutationOperator;
        this.duplicationMutationOperator = duplicationMutationOperator;

        this.evaluator = evaluator;

        comparator = new ObjectiveComparator<>(0);
    }

    @Override
    protected boolean isStoppingConditionReached() {
        return (evaluations >= maxEvaluations);
    }

    @Override
    protected List<VariableIntegerSolution> createInitialPopulation() {
        List<VariableIntegerSolution> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            VariableIntegerSolution newIndividual = problem.createSolution();
            population.add(newIndividual);
        }
        return population;
    }

    @Override
    protected List<VariableIntegerSolution> replacement(List<VariableIntegerSolution> population, List<VariableIntegerSolution> offspringPopulation) {
        Collections.sort(population, comparator);
        offspringPopulation.add(population.get(0));
        offspringPopulation.add(population.get(1));
        Collections.sort(offspringPopulation, comparator);
        offspringPopulation.remove(offspringPopulation.size() - 1);
        offspringPopulation.remove(offspringPopulation.size() - 1);

        return offspringPopulation;
    }

    @Override
    protected List<VariableIntegerSolution> reproduction(List<VariableIntegerSolution> matingPopulation) {
        List<VariableIntegerSolution> offspringPopulation = new ArrayList<>(matingPopulation.size() + 2);
        for (int i = 0; i < populationSize; i += 2) {
            List<VariableIntegerSolution> parents = new ArrayList<>(2);
            parents.add(matingPopulation.get(i));
            parents.add(matingPopulation.get(i + 1));

            List<VariableIntegerSolution> offspring = crossoverOperator.execute(parents);
            mutationOperator.execute(offspring.get(0));
            mutationOperator.execute(offspring.get(1));

            pruneMutationOperator.execute(offspring.get(0));
            pruneMutationOperator.execute(offspring.get(1));
            duplicationMutationOperator.execute(offspring.get(0));
            duplicationMutationOperator.execute(offspring.get(1));

            offspringPopulation.add(offspring.get(0));
            offspringPopulation.add(offspring.get(1));
        }
        return offspringPopulation;
    }

    @Override
    protected List<VariableIntegerSolution> selection(List<VariableIntegerSolution> population) {
        List<VariableIntegerSolution> matingPopulation = new ArrayList<>(population.size());
        for (int i = 0; i < populationSize; i++) {
            VariableIntegerSolution solution = selectionOperator.execute(population);
            matingPopulation.add(solution);
        }

        return matingPopulation;
    }

    @Override
    protected List<VariableIntegerSolution> evaluatePopulation(List<VariableIntegerSolution> population) {
        population = evaluator.evaluate(population, problem);

        return population;
    }

    @Override
    public VariableIntegerSolution getResult() {
        Collections.sort(getPopulation(), comparator);
        return getPopulation().get(0);
    }

    @Override
    public void initProgress() {
        evaluations = populationSize;
    }

    @Override
    public void updateProgress() {
        evaluations += populationSize;
    }
}