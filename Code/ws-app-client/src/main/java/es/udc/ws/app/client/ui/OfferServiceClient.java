package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientOfferService;
import es.udc.ws.app.client.service.ClientOfferServiceFactory;
import es.udc.ws.app.dto.OfferDto;
import es.udc.ws.app.dto.ReserveDto;
import es.udc.ws.app.format.FormatUtils;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OfferServiceClient {

    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientOfferService clientOfferService =
                ClientOfferServiceFactory.getService();
        if("-addOffer".equalsIgnoreCase(args[0])) {
            args = validateArgs(args, new int[] {8,9});

            /* [add] OfferServiceClient -addOffer <name> <description> <price>
             	<discountedPrice> <MaxReserves> <startDate> <endDate> <enjoyDate>
             	Date format: dd/mm/yy-hh:mm:ss
             */
            try {
            	Long offerId;
            	if (args.length == 8)
	                offerId = clientOfferService.addOffer(new OfferDto(
	                		null, args[1], args[2], Float.parseFloat(args[3]), 
	                		Float.parseFloat(args[4]), null, 
	                		FormatUtils.parseCalendar(args[5]), 
	                		FormatUtils.parseCalendar(args[6]),
	                		FormatUtils.parseCalendar(args[7])));
            	else
            		offerId = clientOfferService.addOffer(new OfferDto(
                    		null, args[1], args[2], Float.parseFloat(args[3]), 
                    		Float.parseFloat(args[4]), 
                    		args[5].equals("null")?null:Integer.parseInt(args[5]), 
                    		FormatUtils.parseCalendar(args[6]), 
                    		FormatUtils.parseCalendar(args[7]),
                    		FormatUtils.parseCalendar(args[8])));


                System.out.println("Offer " + offerId + " created sucessfully");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-removeOffer".equalsIgnoreCase(args[0])) {
            args = validateArgs(args, new int[] {2,2});

            // [remove] OfferServiceClient -removeOffer <offerId>

            try {
                clientOfferService.removeOffer(Long.parseLong(args[1]));

                System.out.println("Offer with id " + args[1] +
                        " removed sucessfully");

            } catch (NumberFormatException | InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-updateOffer".equalsIgnoreCase(args[0])) {
            args = validateArgs(args, new int[] {9,10});

           /* [update]  OfferServiceClient -updateOffer <offerId> <name> <description> 
            * <price> <discountedPrice> <MaxReserves> <startDate> <endDate> <enjoyDate>
             	Date format: dd/mm/yy-hh:mm:ss
		   */
           try {
        	   if (args.length == 9)
        		   clientOfferService.updateOffer(new OfferDto(
                   		Long.parseLong(args[1]), args[2], args[3], 
                   		Float.parseFloat(args[4]), Float.parseFloat(args[5]), 
                   		null,
                   		FormatUtils.parseCalendar(args[6]), 
                   		FormatUtils.parseCalendar(args[7]),
                   		FormatUtils.parseCalendar(args[8])));
        	   else
                clientOfferService.updateOffer(new OfferDto(
                		Long.parseLong(args[1]), args[2], args[3], 
                		Float.parseFloat(args[4]), Float.parseFloat(args[5]), 
                		args[6].equals("null")?null:Integer.parseInt(args[6]),
                		FormatUtils.parseCalendar(args[7]), 
                		FormatUtils.parseCalendar(args[8]),
                		FormatUtils.parseCalendar(args[9])));

                System.out.println("Offer " + args[1] + " updated sucessfully");

            } catch (NumberFormatException | InputValidationException |
                     InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-findOffer".equalsIgnoreCase(args[0])) {
            args = validateArgs(args, new int[] {2,2});

            // [find] OfferServiceClient -findOffer <offerId>

            try {
                OfferDto offerDto = clientOfferService.findOffer(Long.parseLong(args[1]));
                if (offerDto == null)
	                System.out.println("Not found offer with ID '" + args[1] + "'");
                else {
                    System.out.println(offerDto);
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        
        }else if("-findOffers".equalsIgnoreCase(args[0])) {
            args = validateArgs(args, new int[] {2,2});
            // [find] OfferServiceClient -findOffers <keywords>

            try {
                List<OfferDto> offers = clientOfferService.
                		findOffers(args[1].equals("null")?null:args[1]);
                if (offers.size() == 0) {
                	System.out.println("No offers found");
                } else
                	for (OfferDto offer:offers) 
                		System.out.println(offer + "\n\n");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            
        } else if("-addReserve".equalsIgnoreCase(args[0])) {
            args = validateArgs(args, new int[] {4,4});

            // [find] OfferServiceClient -addReserve <offerId> <userMail> <userCreditCard>

            try {
               long reserveId = clientOfferService.reserveOffer(Long.parseLong(args[1]),
                		args[2], args[3]);
	            System.out.println("You have reserved offer '"+args[1]+"'. Your code is '"+
                		reserveId+"'");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }else if("-findReserve".equalsIgnoreCase(args[0])) {
            args = validateArgs(args, new int[] {2,2});
            // [find] OfferServiceClient -findReserve <reserveId>

            try {
               ReserveDto reserve = clientOfferService.findReserve(Long.parseLong(args[1]));
	           System.out.println(reserve);
	            
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            
        }else if("-findReserves".equalsIgnoreCase(args[0])) {
            args = validateArgs(args, new int[] {3,3});

            // [find] OfferServiceClient -findReserves <offerId> <ReserveState>
            
            try {
               List<ReserveDto> reserves = clientOfferService.findReserves
            		   (args[1].equals("null")?null:Long.parseLong(args[1]),
            			args[2].equals("null")?null:ReserveDto.
            					ReserveDtoState.valueOf(args[2]));
               if (reserves.size() == 0)
            	   System.out.println("No reserves found");
               else
		           for (ReserveDto reserve: reserves)
		        	   System.out.println(reserve);
	            
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            
        }else if("-claimReserve".equalsIgnoreCase(args[0])) {
            args = validateArgs(args, new int[] {2,2});

            // [find] OfferServiceClient -claimReserve <reserveId> 

            try {
               clientOfferService.claimOffer(Long.parseLong(args[1]));
	           System.out.println("Reserve claimed. Your code is '"+args[1]+"'");
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else {
        	System.err.println("Comando no encontrado");
        	printUsageAndExit();
        }

    }

    public static String[] validateArgs(String[] args, int[] expectedArgs) {
       args = parseArgs(args);
       if(expectedArgs[0] >  args.length || expectedArgs[1] < args.length) {
    	   printUsageAndExit();
           return null;
       }
       return args;
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
        		"[add] OfferServiceClient -updateOffer <name> <description> <price>\n"+ 
        		"		<discountedPrice> <MaxReserves> <startDate> <endDate>\n"+
        		"		<enjoyDate>\n"+
        		"[update] OfferServiceClient -updateOffer <offerId> <name> <description>\n"+
        		"			<price> <discountedPrice> <MaxReserves> <startDate> <endDate>\n"+
        		"			<enjoyDate>\n" +
        		"[remove] OfferServiceClient -removeOffer <offerId>\n" +
        		"[findOffer] OfferServiceClient -findOffer <offerId>\n" +
        		"[findOffers] OfferServiceClient -findOffers <keywords>\n" +
        		"[addReserve] OfferServiceClient -reserveOffer <offerId> <userMail>\n" +
        		"		<userCreditCard>\n" +
        		"[findReserve] OfferServiceClient -findReserve <reserveId>\n" +
        		"[findReserves] OfferServiceClient -findReserves <offerId> <state>\n"+
        		"[claimOffer] OfferServiceClient -claimReserve <reserveId>\n");
    }
    
    public static String[] parseArgs (String[] args) {
    	ArrayList<String> result = new ArrayList<String>();
    	ArrayList<Integer> ini = new ArrayList <Integer>();
    	ArrayList<Integer> end = new ArrayList <Integer>();
    	
    	for(int i = 0; i < args.length; i++) {
    		if (args[i].startsWith("'"))
    			ini.add(i);
    		if (args[i].endsWith("'")) 
    			end.add(i);
    	}
    	
    	if (ini.size() == 0)
    		return args;
    	
    	if (ini.size() != end.size()) {
    		printUsageAndExit();
    	} else {
    		int listIndex = 0;
    		boolean adding = false;
    		String temp = "";
    		for (int i = 0; i < args.length; i++) {
    			if (i != ini.get(listIndex) && !adding)
    				result.add(args[i]);
    			else {
	    			temp +=args[i] + " ";
	    			adding = true;
	    			if (i == end.get(listIndex)){
	    				adding = false;
	    				temp = temp.substring(1, temp.length()-2);
	    				listIndex+=1;
		    			result.add(temp);
		    			temp = "";
	    			}
    			}
    		}
    	}
    	return result.toArray(new String[result.size()]);
    	
    } 

}
