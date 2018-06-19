package kz.alfabank.alfaordersbpm.domain.models.pkb;

public class PkbData {
    private String requestDate;
    private String responseDate;
    private String requestBody;
    private String soapResult;
    private String exception;
    private String xmlName;
    private String htmlName;

    public PkbData() {
    }

    public PkbData(String requestDate, String responseDate, String requestBody, String soapResult, String exception, String xmlName, String htmlName) {
        this.requestDate = requestDate;
        this.responseDate = responseDate;
        this.requestBody = requestBody;
        this.soapResult = soapResult;
        this.exception = exception;
        this.xmlName = xmlName;
        this.htmlName = htmlName;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getSoapResult() {
        return soapResult;
    }

    public void setSoapResult(String soapResult) {
        this.soapResult = soapResult;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public String getHtmlName() {
        return htmlName;
    }

    public void setHtmlName(String htmlName) {
        this.htmlName = htmlName;
    }

    @Override
    public String toString() {
        return "PkbData{" +
                "requestDate='" + requestDate + '\'' +
                ", responseDate='" + responseDate + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", soapResult='" + soapResult + '\'' +
                ", exception='" + exception + '\'' +
                ", xmlName='" + xmlName + '\'' +
                ", htmlName='" + htmlName + '\'' +
                '}';
    }
}
