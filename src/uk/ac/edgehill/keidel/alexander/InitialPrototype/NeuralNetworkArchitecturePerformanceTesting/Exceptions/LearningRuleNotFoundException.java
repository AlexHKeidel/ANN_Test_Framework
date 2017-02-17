package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Exceptions;

import org.neuroph.core.learning.LearningRule;

/**
 * Created by Alexander Keidel, 22397868 on 17/02/2017.
 * Custom exception
 * Thrown when a learning rule could not be found
 * See
 * https://www.mkyong.com/java/java-custom-exception-examples/
 */
public class LearningRuleNotFoundException extends Exception {
    public LearningRuleNotFoundException(String message){
        super(message);
    }
}
