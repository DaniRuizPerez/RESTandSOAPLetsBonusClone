package es.udc.ws.app.client.service.soap;

import es.udc.ws.app.dto.ReserveDto;
import es.udc.ws.app.dto.ReserveDto.ReserveDtoState;
import es.udc.ws.app.format.FormatUtils;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;


public class ReserveDtoToSoapReserveDtoConversor {
	
	public static ReserveDtoState SoapReserveStateToReserveState 
			(es.udc.ws.app.client.service.soap.wsdl.ReserveDtoState reserveDtoState) {
		return ReserveDtoState.valueOf(reserveDtoState.toString());
		
	}
	
	public static es.udc.ws.app.client.service.soap.wsdl.ReserveDtoState 
			ReserveStateToSoapReserveState (ReserveDtoState status) {
		return status==null?null:
			es.udc.ws.app.client.service.soap.wsdl.ReserveDtoState.
			valueOf(status.toString());

}
    
    public static es.udc.ws.app.client.service.soap.wsdl.ReserveDto 
            toSoapReserveDto(ReserveDto reserve) throws DatatypeConfigurationException {
    	
        es.udc.ws.app.client.service.soap.wsdl.ReserveDto soapReserveDto = 
                new es.udc.ws.app.client.service.soap.wsdl.ReserveDto();
        soapReserveDto.setOfferId(reserve.getOfferId());
        soapReserveDto.setReserveId(reserve.getReserveId());
        soapReserveDto.setUserCreditCard(reserve.getUserCreditCard());
        soapReserveDto.setUserMail(reserve.getUserMail());
        soapReserveDto.setStatus(es.udc.ws.app.client.service.soap.wsdl.ReserveDtoState.
        		valueOf(reserve.getStatus().toString()));
        soapReserveDto.setReserveDate(FormatUtils.
        		calendarToXMLGregorianCalendar(reserve.getReserveDate()));
        return soapReserveDto;
    }    
    
    public static ReserveDto toReserveDto(
            es.udc.ws.app.client.service.soap.wsdl.ReserveDto reserve) {
        return new ReserveDto(reserve.getReserveId(), reserve.getOfferId(), 
        		reserve.getUserMail(), reserve.getUserCreditCard(), 
        		SoapReserveStateToReserveState(reserve.getStatus()),
        		FormatUtils.XMLGregorianCalendarToCalendar(reserve.getReserveDate()));
    }
    
    
    
    public static List<ReserveDto> toReserveDtos(
            List<es.udc.ws.app.client.service.soap.wsdl.ReserveDto> reserves) {
        List<ReserveDto> reserveDtos = new ArrayList<>(reserves.size());
        for (int i = 0; i < reserves.size(); i++) {
            es.udc.ws.app.client.service.soap.wsdl.ReserveDto reserve = 
                    reserves.get(i);
            reserveDtos.add(toReserveDto(reserve));
            
        }
        return reserveDtos;
    }    
    
}
