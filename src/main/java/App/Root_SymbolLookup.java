package App;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Root_SymbolLookup implements Serializable {
	private List<Symbol> result;
	private int count;

	public Root_SymbolLookup() {

	}

	public List<Symbol> getResult() {
		return result;
	}

	public void setResult(List<Symbol> result) {
		this.result = result;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
