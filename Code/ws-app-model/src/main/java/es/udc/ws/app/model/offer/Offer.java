package es.udc.ws.app.model.offer;

import java.util.Calendar;
import es.udc.ws.app.format.FormatUtils;


public class Offer {
	
	public enum OfferState { 
		CREATED, 
		COMMITTED, 
		RELEASED;
	}

    private Long offerId;
    private String name;
    private String description;
    private float realPrice;
    private float discountedPrice;
    private int numberOfReserves;
    private int claimedReserves;
    private Integer maxPeople;
    private Calendar startDate;
    private Calendar endDate;
    private Calendar enjoyDate;
    private OfferState status;
    
	public Offer(Long offerId, String name, String description,
			float realPrice, float discountedPrice, int numberOfReserves,
			int claimedReserves, Integer maxPeople, Calendar startDate,
			Calendar endDate, Calendar enjoyDate, OfferState status) {
		this.offerId = offerId;
		this.name = name;
		this.description = description;
		this.realPrice = realPrice;
		this.discountedPrice = discountedPrice;
		this.numberOfReserves = numberOfReserves;
		this.claimedReserves = claimedReserves;
		this.maxPeople = maxPeople;
		this.startDate = startDate;
        if (startDate != null) {
            this.startDate.set(Calendar.MILLISECOND, 0);
        }
		this.endDate = endDate;
        if (endDate != null) {
            this.endDate.set(Calendar.MILLISECOND, 0);
        }
        this.enjoyDate = enjoyDate;
        if (enjoyDate != null) {
            this.enjoyDate.set(Calendar.MILLISECOND, 0);
        }
		this.status = status;
	}

	

	public Offer (String name, String description, float realPrice,
			float discountedPrice, Integer maxPeople, Calendar startDate,
			Calendar endDate, Calendar enjoyDate) {
		/*First time we create an offer (before uploading it to database)*/
		this.offerId = (long)0;
		this.name = name;
		this.description = description;
		this.realPrice = realPrice;
		this.discountedPrice = discountedPrice;
		this.numberOfReserves = 0;
		this.claimedReserves = 0;
		this.maxPeople = maxPeople;
		this.startDate = startDate;
        if (startDate != null) {
            this.startDate.set(Calendar.MILLISECOND, 0);
        }
		this.endDate = endDate;
        if (endDate != null) {
            this.endDate.set(Calendar.MILLISECOND, 0);
        }
        this.enjoyDate = enjoyDate;
        if (enjoyDate != null) {
            this.enjoyDate.set(Calendar.MILLISECOND, 0);
        }
		this.status = OfferState.CREATED;
	}

	public Long getOfferId() {
		return offerId;
	}

	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(float realPrice) {
		this.realPrice = realPrice;
	}

	public float getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(float discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public int getNumberOfReserves() {
		return numberOfReserves;
	}

	public void setNumberOfReserves(int numberOfReserves) {
		this.numberOfReserves = numberOfReserves;
	}

	public int getClaimedReserves() {
		return claimedReserves;
	}

	public void setClaimedReserves(int claimedReserves) {
		this.claimedReserves = claimedReserves;
	}

	public Integer getMaxPeople() {
		return maxPeople;
	}

	public void setMaxPeople(Integer maxPeople) {
		this.maxPeople = maxPeople;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
        if (startDate != null) {
            this.startDate.set(Calendar.MILLISECOND, 0);
        }
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
        if (endDate != null) {
            this.endDate.set(Calendar.MILLISECOND, 0);
        }
	}

	public Calendar getEnjoyDate() {
		return enjoyDate;
	}

	public void setEnjoyDate(Calendar enjoyDate) {
        this.enjoyDate = enjoyDate;
        if (enjoyDate != null) {
            this.enjoyDate.set(Calendar.MILLISECOND, 0);
        }
	}

	public OfferState getStatus() {
		return status;
	}

	public void setStatus(OfferState status) {
		this.status = status;
	}
		

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + claimedReserves;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + Float.floatToIntBits(discountedPrice);
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result
				+ ((enjoyDate == null) ? 0 : enjoyDate.hashCode());
		result = prime * result
				+ ((maxPeople == null) ? 0 : maxPeople.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + numberOfReserves;
		result = prime * result + ((offerId == null) ? 0 : offerId.hashCode());
		result = prime * result + Float.floatToIntBits(realPrice);
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Offer other = (Offer) obj;
		if (claimedReserves != other.claimedReserves)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (Float.floatToIntBits(discountedPrice) != Float
				.floatToIntBits(other.discountedPrice))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (enjoyDate == null) {
			if (other.enjoyDate != null)
				return false;
		} else if (!enjoyDate.equals(other.enjoyDate))
			return false;
		if (maxPeople == null) {
			if (other.maxPeople != null)
				return false;
		} else if (!maxPeople.equals(other.maxPeople))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (numberOfReserves != other.numberOfReserves)
			return false;
		if (offerId == null) {
			if (other.offerId != null)
				return false;
		} else if (!offerId.equals(other.offerId))
			return false;
		if (Float.floatToIntBits(realPrice) != Float
				.floatToIntBits(other.realPrice))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (status != other.status)
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Offer [offerId=" + offerId + ", name=" + name
				+ ", description=" + description + ", realPrice=" + realPrice
				+ ", discountedPrice=" + discountedPrice
				+ ", numberOfReserves=" + numberOfReserves
				+ ", claimedReserves=" + claimedReserves + ", maxPeople="
				+ maxPeople + ", startDate=" + FormatUtils.calendarToStringSql(startDate) 
				+ ", endDate="+ FormatUtils.calendarToStringSql(endDate) + ", "
				+ "enjoyDate=" + FormatUtils.calendarToStringSql(enjoyDate) 
				+ ", status=" + status
				+ "]";
	}


	
}