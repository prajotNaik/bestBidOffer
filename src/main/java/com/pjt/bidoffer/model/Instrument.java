package com.pjt.bidoffer.model;

public class Instrument {

	private String instrumentId;
	private String instrumentIdentifierType;

	public Instrument(String instrumentId, String instrumentIdentifierType) {
		super();
		this.instrumentId = instrumentId;
		this.instrumentIdentifierType = instrumentIdentifierType;
	}

	public String getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}

	public String getInstrumentIdentifierType() {
		return instrumentIdentifierType;
	}

	public void setInstrumentIdentifierType(String instrumentIdentifierType) {
		this.instrumentIdentifierType = instrumentIdentifierType;
	}

	@Override
	public String toString() {
		return "Instrument [instrumentId=" + instrumentId + ", instrumentIdentifierType=" + instrumentIdentifierType
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instrumentId == null) ? 0 : instrumentId.hashCode());
		result = prime * result + ((instrumentIdentifierType == null) ? 0 : instrumentIdentifierType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instrument other = (Instrument) obj;
		if (instrumentId == null) {
			if (other.instrumentId != null)
				return false;
		} else if (!instrumentId.equals(other.instrumentId))
			return false;
		if (instrumentIdentifierType == null) {
			if (other.instrumentIdentifierType != null)
				return false;
		} else if (!instrumentIdentifierType.equals(other.instrumentIdentifierType))
			return false;
		return true;
	}

	
}
