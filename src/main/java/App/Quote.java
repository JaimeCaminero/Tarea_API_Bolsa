package App;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote implements Serializable {
	@JsonProperty(value = "c")
	private Float currentPrice;
	@JsonProperty(value = "dp")
	private Float percentChange;
	@JsonProperty(value = "o")
	private Float openPriceOfTheDay;

	public Quote() {

	}

	public Float getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Float currentPrice) {
		this.currentPrice = currentPrice;
	}

	public Float getPercentChange() {
		return percentChange;
	}

	public void setPercentChange(Float percentChange) {
		this.percentChange = percentChange;
	}

	public Float getOpenPriceOfTheDay() {
		return openPriceOfTheDay;
	}

	public void setOpenPriceOfTheDay(Float openPriceOfTheDay) {
		this.openPriceOfTheDay = openPriceOfTheDay;

	}

}
