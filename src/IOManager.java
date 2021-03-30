import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class IOManager {
    private double epsilon = 0;
    private IterationMatrix iterationMatrix;
    public void readMatrix(){
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print(
                    "Добро пожаловать действие" +
                            "\n если хотите ввести матрицу с консоли - нажмите 0" +
                            "\n если хотите ввести матрицу чере файл - нажмите 1" +
                            "\n если хотите выйти из программы - нажмите q"

            );
            System.out.println();
            String choice = scanner.next();
            switch (choice) {
                case "0":
                    iterationMatrix = readMatrix(scanner);
                    while (epsilon == 0) {
                        System.out.println("Введите эпсилон");
                        epsilon = Double.parseDouble(scanner.next().replaceAll(",", "."));
                    }
                    break;
                case "q":
                    System.out.print("Вы вышли из программы");
                    System.exit(0);
                    break;
                case "1":
                    iterationMatrix = readMatrixByFile();
                    break;
            }
        } catch (Exception e) {
            System.out.println("Произошла ошибка ввода данных");
        }
    }
    public int readSize(Scanner scanner) {
        while (true) {
            System.out.println("Введите размерность важей матрицы.Число от 1 до 20");
            try {
                int size = scanner.nextInt();
                if (size < 1 || size > 20)
                    throw new IllegalArgumentException();
                return size;
            } catch (Exception e) {
                System.out.println("Неверно задана размерность матрицы");
                scanner.nextLine();
            }
        }
    }

    public IterationMatrix readMatrix(Scanner scanner) {
        int size = readSize(scanner);
        System.out.println("Введите коэффиценты самой матрицы");
        double matrix[][] = new double[size][size + 1];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size + 1; j++) {
                matrix[i][j] = Double.parseDouble(scanner.next().replaceAll(",", "."));
            }
        }
        return new IterationMatrix(matrix);
    }

    public IterationMatrix readMatrixByFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/matrix.txt"));
        int size = scanner.nextInt();
        double[][] matrix = new double[size][size + 1];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size + 1; j++) {
                matrix[i][j] = Double.parseDouble(scanner.next().replaceAll(",", "."));
            }
        }
        while (epsilon == 0) {
            epsilon = Double.parseDouble(scanner.next().replaceAll(",", "."));
        }
        return new IterationMatrix(matrix);
    }
    public void calculateAnswer() {
        if (!iterationMatrix.makeDominant()){
            System.out.println("Нету диагонального преобладания");
            System.out.println();
            return;
        }
        iterationMatrix.transformMatrixToXFormed();
        do {
            iterationMatrix.iterateToTheGivenEpsilon();
        } while (iterationMatrix.getMaxDeviation() > epsilon);

        double[] result = iterationMatrix.getApproximation();

        int iters = iterationMatrix.getIterations();
        System.out.printf("Вектора:\n");
        for (int i = 0; i < result.length; i++) {
            System.out.printf("x%s: %.4f\n", i+1, result[i]);
        }
        System.out.printf("Количество итераций: \n%s\n", iters);
        double[] prevResult = iterationMatrix.getPreviousApproximation();
        System.out.println("Погрешности векторов:");
        for (int i = 0; i < result.length; i++) {
            System.out.printf("x%s^(%s)-x%s^(%s): %.6f\n", i+1, iters, i+1, iters-1, Math.abs(result[i]-prevResult[i]));
        }
    }
}
