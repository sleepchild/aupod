package mrtubbs.postman.sampleobjects;

public class CustomError{
    //
    private int errCode = 0;
    private String errMessage;
    //
    CustomError(int errCode, String message){
		this.errCode=errCode;
		this.errMessage=message;
	}
    
    CustomError(String errorMessage){
        this.errMessage = errorMessage;   
    }
    
    public int getErrCode(){
        return errCode;
    }
    
    public String getMessage(){
        return errMessage != null ? errMessage : "an error occured";
    }
}
