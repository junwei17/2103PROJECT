package util.exception;



public class EmployeeUsernameExistException extends Exception
{
    public EmployeeUsernameExistException()
    {
    }
    
    
    
    public EmployeeUsernameExistException(String msg)
    {
        super(msg);
    }
}