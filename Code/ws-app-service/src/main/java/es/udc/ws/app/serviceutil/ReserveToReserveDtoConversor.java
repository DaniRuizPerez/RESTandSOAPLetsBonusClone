package es.udc.ws.app.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.dto.ReserveDto;
import es.udc.ws.app.dto.ReserveDto;
import es.udc.ws.app.dto.ReserveDto.ReserveDtoState;
import es.udc.ws.app.model.reserve.Reserve;
import es.udc.ws.app.model.reserve.Reserve;



public class ReserveToReserveDtoConversor {
    
    public static ReserveDto toReserveDto(Reserve reserve) {
        return new ReserveDto(reserve.getReserveId(), 
        		reserve.getOfferId(), reserve.getUserMail(), 
        		reserve.getUserCreditCard(), 
        		ReserveDtoState.valueOf(reserve.getStatus().toString()), 
        		reserve.getReserveDate());
    }
    
    public static List<ReserveDto> toReserveDtos(List<Reserve> reserves) {
        List<ReserveDto> reserveDtos = new ArrayList<>(reserves.size());
        for (int i = 0; i < reserves.size(); i++) {
            Reserve reserve = reserves.get(i);
            reserveDtos.add(toReserveDto(reserve));
        }
        return reserveDtos;
    }
    
}
