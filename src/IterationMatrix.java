import java.util.Arrays;

public class IterationMatrix {
    private double[][] matrix;
    private double maxDeviation;
    private int iterations = 1;
    private double[] approximation;
    private double[] previousApproximation;

    public IterationMatrix(double[][] initialMatrix) {
        this.matrix = initialMatrix;
    }

    public void transformMatrixToXFormed() {
        for (int i = 0; i < matrix.length; i++) {
            double Aii = matrix[i][i];
            for (int j = 0; j < matrix[i].length; j++) {
                if (j != i) {
                    if (matrix[i].length - 1 != j)
                        matrix[i][j] = matrix[i][j] / -Aii;
                    else
                        matrix[i][j] = matrix[i][j] / Aii;

                } else
                    matrix[i][j] = 0;
            }
        }
    }

    public double[] getConstantTermsVector() {
        double[] constants = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            constants[i] = matrix[i][matrix[i].length - 1];

        }
        return constants;
    }


    public double[] computeXUsingPreviousApproximation(final double[] previousApproximation) {
        double[] answer = new double[matrix.length];
        this.maxDeviation = 0;
        for (int i = 0; i < matrix.length; i++) {
            answer[i] = 0;
            for (int j = 0; j < matrix[i].length - 1; j++) {
                answer[i] += matrix[i][j] * previousApproximation[j];
            }
            answer[i] += matrix[i][matrix[i].length - 1];

            double deviation = Math.abs(answer[i] - previousApproximation[i]);
            if (deviation > this.maxDeviation)
                this.maxDeviation = deviation;
        }
        return answer;
    }

    public void iterateToTheGivenEpsilon() {

        if (iterations == 1) {
            approximation = computeXUsingPreviousApproximation(getConstantTermsVector());
            previousApproximation = approximation;
        } else {
            previousApproximation = approximation;
            approximation = computeXUsingPreviousApproximation(approximation);
        }
        iterations++;
    }

    public boolean transformToDominant(int r, boolean[] V, int[] R) {
        int n = matrix.length;

        if (r == matrix.length) {
            double[][] T = new double[n][n + 1];
            for (int i = 0; i < R.length; i++) {
                for (int j = 0; j < n + 1; j++)
                    T[i][j] = matrix[R[i]][j];
            }
            matrix = T;
            return true;
        }
        for (int i = 0; i < n; i++) {
            if (V[i]) continue;

            double sum = 0;

            for (int j = 0; j < n; j++)
                sum += Math.abs(matrix[i][j]);

            if (2 * Math.abs(matrix[i][r]) > sum) {

                V[i] = true;
                R[r] = i;
                if (transformToDominant(r + 1, V, R))
                    return true;
                V[i] = false;
            }
        }

        return false;
    }

    public boolean makeDominant() {

        boolean[] visited = new boolean[matrix.length];
        int[] rows = new int[matrix.length];

        Arrays.fill(visited, false);

        return transformToDominant(0, visited, rows);
    }


    public double getMaxDeviation() {
        return maxDeviation;
    }

    public int getIterations() {
        return iterations;
    }

    public double[] getApproximation() {
        return approximation;
    }

    public double[] getPreviousApproximation() {
        return previousApproximation;
    }

}
