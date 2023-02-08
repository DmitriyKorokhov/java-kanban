package service;

public class ManagerSaveException extends RuntimeException{
    //Сделал вывод причины
    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
