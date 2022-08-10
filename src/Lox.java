public class Lox {
    public static void main(String[] args) {
        if(args.length > 1) {
            System.out.println("Usage: lox [path/to/script]");
        } else if (args.length == 1) {
            System.out.println("This will run scripts some day!");
        } else {
            System.out.println("This will start prompts some day!");
        }
    }
}