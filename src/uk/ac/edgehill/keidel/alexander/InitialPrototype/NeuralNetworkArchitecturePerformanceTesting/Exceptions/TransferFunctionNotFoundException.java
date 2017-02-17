package uk.ac.edgehill.keidel.alexander.InitialPrototype.NeuralNetworkArchitecturePerformanceTesting.Exceptions;

/**
 * Created by Alexander Keidel, 22397868 on 17/02/2017.
 * Custom exception
 * Thrown when a transfer function could not be located.
 * See
 * https://www.mkyong.com/java/java-custom-exception-examples/
 */
public class TransferFunctionNotFoundException extends Exception {
    public TransferFunctionNotFoundException(String message){
        super(message);
    }
}
