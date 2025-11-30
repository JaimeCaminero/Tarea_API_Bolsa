package App;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SECFillings implements Serializable{
	private String cik;
	private String form;
	private String filledDate;
	private String acceptedDate;
	private String filingUrl;
	
	public SECFillings() {
		
	}

	public String getCik() {
		return cik;
	}

	public void setCik(String cik) {
		this.cik = cik;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getFilledDate() {
		return filledDate;
	}

	public void setFilledDate(String filledDate) {
		this.filledDate = filledDate;
	}

	public String getAcceptedDate() {
		return acceptedDate;
	}

	public void setAcceptedDate(String acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	public String getFilingUrl() {
		return filingUrl;
	}

	public void setFilingUrl(String filingUrl) {
		this.filingUrl = filingUrl;
	}

	
	
	
	
	

}
