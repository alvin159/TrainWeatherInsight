package compse110.Entity;

public class TrainRequestError {

    private String queryString;
    private String code;
    private String errorMessage;

    public TrainRequestError() {
    }

    public TrainRequestError(String queryString, String code, String errorMessage) {
        this.queryString = queryString;
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public String getQueryString() { 
		 return this.queryString; } 

    public void setQueryString(String queryString) { 
		 this.queryString = queryString; } 

    public String getCode() { 
		 return this.code; } 

    public void setCode(String code) { 
		 this.code = code; } 

    public String getErrorMessage() { 
		 return this.errorMessage; } 

    public void setErrorMessage(String errorMessage) { 
		 this.errorMessage = errorMessage; } 
}
