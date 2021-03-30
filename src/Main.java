public class Main {
    public static void main(String[] args) {
        IOManager ioManager = new IOManager();
        while (true){
            ioManager.readMatrix();
            ioManager.calculateAnswer();
        }
    }
}
