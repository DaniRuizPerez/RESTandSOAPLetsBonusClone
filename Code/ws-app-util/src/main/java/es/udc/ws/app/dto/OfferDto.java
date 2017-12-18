package es.udc.ws.app.dto;

import java.util.Calendar;

import es.udc.ws.app.format.FormatUtils;

public class OfferDto {

    private Long offerId;
    private String name;
    private String description;
    private float realPrice;
    private float discountedPrice;
    private Integer maxPeople;
    private Calendar startDate;
    private Calendar endDate;
    private Calendar enjoyDate;

	public OfferDto() {
	}

	public OfferDto(Long offerId, String name, String description,
			float realPrice, float discountedPrice, Integer maxPeople,
			Calendar startDate, Calendar endDate, Calendar enjoyDate) {
		super();
		this.offerId = offerId;
		this.name = name;
		this.description = description;
		this.realPrice = realPrice;
		this.discountedPrice = discountedPrice;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + Float.floatToIntBits(discountedPrice);
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result
				+ ((enjoyDate == null) ? 0 : enjoyDate.hashCode());
		result = prime * result
				+ ((maxPeople == null) ? 0 : maxPeople.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((offerId == null) ? 0 : offerId.hashCode());
		result = prime * result + Float.floatToIntBits(realPrice);
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
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
		OfferDto other = (OfferDto) obj;
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
		return true;
	}

	@Override
	public String toString() {
		return "Id: " + offerId +"\n"+
               "Name: " + name + "\n"+
               "Description: " + description + "\n"+
               "Real price: " + realPrice+ "\n"+
               "Discounted price: " + discountedPrice+"\n"+
               "Max. reserves: " + maxPeople+"\n"+
               "Start date: " + FormatUtils.calendarToStringSql(startDate)+"\n"+
               "End date: " + FormatUtils.calendarToStringSql(endDate)+"\n"+
               "Enjoy date: " + FormatUtils.calendarToStringSql(enjoyDate);
	}
	
	


}