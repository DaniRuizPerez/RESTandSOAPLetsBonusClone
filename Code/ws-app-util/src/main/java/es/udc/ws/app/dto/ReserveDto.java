package es.udc.ws.app.dto;

import java.util.Calendar;

import es.udc.ws.app.format.FormatUtils;

public class ReserveDto {

	private Long reserveId;
	private Long offerId;
	private String userMail;
	private String userCreditCard;
	private ReserveDtoState status;
	private Calendar reserveDate;
	
	public ReserveDto() {
	}
	
	public enum ReserveDtoState { 
		PENDANT,
		CLOSED;
	}
	
	public ReserveDto(Long reserveId, Long offerId, String userMail,
			String userCreditCard, ReserveDtoState status, Calendar reserveDate) {
		this.reserveId = reserveId;
		this.offerId = offerId;
		this.userMail = userMail;
		this.userCreditCard = userCreditCard;
		this.status = status;
		this.reserveDate = reserveDate;
		if (reserveDate != null) {
            this.reserveDate.set(Calendar.MILLISECOND, 0);
        }
	}

	public Long getReserveId() {
		return reserveId;
	}

	public void setReserveId(Long reserveId) {
		this.reserveId = reserveId;
	}

	public Long getOfferId() {
		return offerId;
	}

	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public String getUserCreditCard() {
		return userCreditCard;
	}

	public void setUserCreditCard(String userCreditCard) {
		this.userCreditCard = userCreditCard;
	}

	public ReserveDtoState getStatus() {
		return status;
	}

	public void setStatus(ReserveDtoState status) {
		this.status = status;
	}
	public void setReserveDate(Calendar reserveDate) {
		this.reserveDate = reserveDate;
		if (reserveDate != null) {
            this.reserveDate.set(Calendar.MILLISECOND, 0);
        }
	}
	public Calendar getReserveDate() {
		return this.reserveDate;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((offerId == null) ? 0 : offerId.hashCode());
		result = prime * result
				+ ((reserveDate == null) ? 0 : reserveDate.hashCode());
		result = prime * result
				+ ((reserveId == null) ? 0 : reserveId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((userCreditCard == null) ? 0 : userCreditCard.hashCode());
		result = prime * result
				+ ((userMail == null) ? 0 : userMail.hashCode());
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
		ReserveDto other = (ReserveDto) obj;
		if (offerId == null) {
			if (other.offerId != null)
				return false;
		} else if (!offerId.equals(other.offerId))
			return false;
		if (reserveDate == null) {
			if (other.reserveDate != null)
				return false;
		} else if (!reserveDate.equals(other.reserveDate))
			return false;
		if (reserveId == null) {
			if (other.reserveId != null)
				return false;
		} else if (!reserveId.equals(other.reserveId))
			return false;
		if (status != other.status)
			return false;
		if (userCreditCard == null) {
			if (other.userCreditCard != null)
				return false;
		} else if (!userCreditCard.equals(other.userCreditCard))
			return false;
		if (userMail == null) {
			if (other.userMail != null)
				return false;
		} else if (!userMail.equals(other.userMail))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Id: " + reserveId+ "\n"+
	           "Offer: " + offerId + "\n"+
	           "User mail: " + userMail + "\n"+
	           "User credit card: " + userCreditCard + "\n"+
	           "State: " + status + "\n" + 
	           "Date: " + FormatUtils.calendarToStringXML(reserveDate)+"\n";
	}	

	

}


