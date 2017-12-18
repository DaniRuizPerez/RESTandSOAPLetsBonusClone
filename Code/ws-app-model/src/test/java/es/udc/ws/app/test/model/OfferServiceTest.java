package es.udc.ws.app.test.model;


import static es.udc.ws.app.model.util.ModelConstants.OFFER_DATA_SOURCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import es.udc.ws.app.model.offer.Offer;
import es.udc.ws.app.model.offer.Offer.OfferState;
import es.udc.ws.app.model.offer.SqlOfferDao;
import es.udc.ws.app.model.offer.SqlOfferDaoFactory;
import es.udc.ws.app.model.offerservice.OfferService;
import es.udc.ws.app.model.offerservice.OfferServiceFactory;
import es.udc.ws.app.model.reserve.Reserve;
import es.udc.ws.app.model.reserve.Reserve.ReserveState;
import es.udc.ws.app.model.reserve.SqlReserveDao;
import es.udc.ws.app.model.reserve.SqlReserveDaoFactory;
import es.udc.ws.app.exceptions.DuplicatedReserveException;
import es.udc.ws.app.exceptions.FullOfferException;
import es.udc.ws.app.exceptions.NotRemovableOfferException;
import es.udc.ws.app.exceptions.NotUpdatableOfferException;
import es.udc.ws.app.exceptions.OfferExpirationException;
import es.udc.ws.app.exceptions.ReserveExpirationException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;

public class OfferServiceTest {

	private final long NON_EXISTENT_RESERVE_ID = -1;
	private final long NON_EXISTENT_OFFER_ID = -1;
	private final float PRICE = 17.5F;
	private final float DISCOUNTED_PRICE = 15.4F;
	private final String USER_MAIL1 = "martin.ruiz.barbeito@udc.es";
	private final String USER_CREDIT_CARD1 = "9238203452334098";
	private final String USER_MAIL2 = "no.martin.ruiz.barbeito@udc.es";
	private final String USER_CREDIT_CARD2 = "0238203452334098";
	private final String USER_MAIL3 = "si.martin.ruiz.barbeito@udc.es";
	private final String USER_CREDIT_CARD3 = "0038203452334098";
	
	private static OfferService offerService = null;


	@BeforeClass
	public static void init() {

		/*
		 * Create a simple data source and add it to "DataSourceLocator" (this
		 * is needed to test "es.udc.ws.Offers.model.Offerservice.OfferService"
		 */
		DataSource dataSource = new SimpleDataSource();

		/* Add "dataSource" to "DataSourceLocator". */
		DataSourceLocator.addDataSource(OFFER_DATA_SOURCE, dataSource);

		offerService = OfferServiceFactory.getService();


	}

	private Offer getValidOffer(String name) {
		Calendar startDate = Calendar.getInstance();
		Calendar endDate =  Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 10);
		Calendar enjoyDate = Calendar.getInstance();
		enjoyDate.add(Calendar.DAY_OF_MONTH,11);
		return new Offer(name, "Description", PRICE, DISCOUNTED_PRICE, 10, 
				startDate, endDate, enjoyDate);
	}

	private Offer getValidOffer() {
		return getValidOffer("Offer name");
	}

	private Offer createOffer(Offer offer) {

		Offer addedOffer = null;
		try {
			addedOffer = offerService.addOffer(offer);
		} catch (InputValidationException e) {
			throw new RuntimeException(e);
		}
		return addedOffer;

	}

	private void removeOffer(Long OfferId) throws 
	NotRemovableOfferException {

		try {
			offerService.removeOffer(OfferId);
		} catch (InstanceNotFoundException e) {
			throw new RuntimeException(e);
		}

	}



	@Test
	public void testAddOfferAndFindOffer() throws InputValidationException,
			InstanceNotFoundException, NotRemovableOfferException {

		Offer offer = getValidOffer();
		Offer addedOffer = null;

		addedOffer = offerService.addOffer(offer);
		

		//Clear Database
		removeOffer(addedOffer.getOfferId());

	}
	@Test
	public void testAddInvalidOffer() throws NotRemovableOfferException {

		Offer offer = getValidOffer();
		Offer addedOffer = null;
		boolean exceptionCatched = false;
		Calendar ridiculousDate = Calendar.getInstance();
		ridiculousDate.add(Calendar.DAY_OF_MONTH,1000);

		try {
			//Check Offer name not null
			offer.setName(null);
			try {
				addedOffer = offerService.addOffer(offer);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			//Check Offer name not empty
			exceptionCatched = false;
			offer = getValidOffer();
			offer.setName("");
			try {
				addedOffer = offerService.addOffer(offer);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			//Check Offer description not null
			exceptionCatched = false;
			offer = getValidOffer();
			offer.setDescription(null);
			try {
				addedOffer = offerService.addOffer(offer);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			//Check Offer description not empty
			exceptionCatched = false;
			offer = getValidOffer();
			offer.setDescription("");
			try {
				addedOffer = offerService.addOffer(offer);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			//Check Offer realPrice >= 0
			exceptionCatched = false;
			offer = getValidOffer();
			offer.setRealPrice((short) -1);
			try {
				addedOffer = offerService.addOffer(offer);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			//Check Offer discountedPrice >= 0
			exceptionCatched = false;
			offer = getValidOffer();
			offer.setDiscountedPrice((short) -1);
			try {
				addedOffer = offerService.addOffer(offer);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);		
			
			//Check Offer maxPerson null
			exceptionCatched = true;
			offer = getValidOffer();
			offer.setMaxPeople(null);
			try {
				addedOffer = offerService.addOffer(offer);
			} catch (InputValidationException e) {
				exceptionCatched = false;
			}
			assertTrue(exceptionCatched);	

			//Check Offer startDate not null
			exceptionCatched = false;
			offer = getValidOffer();
			offer.setStartDate(null);
			try {
				addedOffer = offerService.addOffer(offer);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			//Check Offer endDate not null
			exceptionCatched = false;
			offer = getValidOffer();
			offer.setEndDate(null);
			try {
				addedOffer = offerService.addOffer(offer);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			//Check Offer enjoyDate not null
			exceptionCatched = false;
			offer = getValidOffer();
			offer.setEnjoyDate(null);
			try {
				addedOffer = offerService.addOffer(offer);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			/*//Check Offer endDate larger than enjoyDate
			exceptionCatched = false;
			offer = getValidOffer();
			offer.setEndDate(ridiculousDate);
			try {
				addedOffer = offerService.addOffer(offer);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);*/
			
			//Check Offer startDate larger than other dates
			exceptionCatched = false;
			offer = getValidOffer();
			offer.setStartDate(ridiculousDate);
			try {
				addedOffer = offerService.addOffer(offer);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

		} finally {
			removeOffer(addedOffer.getOfferId()); //por el maxPerson null test
			if (!exceptionCatched) {
				//Clear Database
				removeOffer(addedOffer.getOfferId());
			}
		}

	}

	@Test
	public void testUpdateOffer() throws InputValidationException,
			InstanceNotFoundException, NotRemovableOfferException {

		Offer offer = createOffer(getValidOffer());
		Calendar startDate = Calendar.getInstance();
		Calendar endDate =  Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 11);
		Calendar enjoyDate = Calendar.getInstance();
		enjoyDate.add(Calendar.DAY_OF_MONTH,12);
		try {
			offer.setName("New name");
	    	offer.setDescription("New desc");
	    	offer.setMaxPeople(50);
	    	offer.setRealPrice(10.0F);
	    	offer.setDiscountedPrice(8.1F);
	    	offer.setEndDate(endDate);
	    	offer.setEnjoyDate(enjoyDate);
	    	offer.setStartDate(startDate);
	    	offer.setStatus(OfferState.CREATED);
	    	
			offerService.updateOffer(offer);
			
			Offer updatedOffer = offerService.findOffer(offer.getOfferId());
			assertEquals(offer, updatedOffer);

		} catch (NotUpdatableOfferException e) {
			//This will never happen
			e.printStackTrace();
		} finally {
			//Clear Database
			removeOffer(offer.getOfferId());
		}

	}
	@Test(expected = InputValidationException.class)
	public void testUpdateInvalidOffer() throws InputValidationException,
			InstanceNotFoundException, NotUpdatableOfferException, 
			NotRemovableOfferException {

		Offer offer = createOffer(getValidOffer());
		try {
			//Check Offer title not null
			offer = offerService.findOffer(offer.getOfferId());
			offer.setName(null);
			offerService.updateOffer(offer);
		} finally {
			//Clear Database
			removeOffer(offer.getOfferId());
		}

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateNonExistentOffer() throws InputValidationException,
			InstanceNotFoundException, NotUpdatableOfferException {

		Offer offer = getValidOffer();
		offer.setOfferId(NON_EXISTENT_OFFER_ID);
		offerService.updateOffer(offer);

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testRemoveOffer() throws InstanceNotFoundException, 
	NotRemovableOfferException {

		Offer offer = createOffer(getValidOffer());
		boolean exceptionCatched = false;
		try {
			offerService.removeOffer(offer.getOfferId());
		} catch (InstanceNotFoundException e) {
			exceptionCatched = true;
		}
		assertTrue(!exceptionCatched);

		offerService.findOffer(offer.getOfferId());

	}
	
	@Test
	public void testRemoveReservedOffer() throws InputValidationException, 
	NotRemovableOfferException, InstanceNotFoundException, OfferExpirationException, 
	FullOfferException, ReserveExpirationException, DuplicatedReserveException {
		Offer offer = createOffer(getValidOffer());
		//El estado commited deja borrar
		boolean exceptionCatched = false;
		long reserveId = offerService.reserveOffer(offer.getOfferId(), USER_MAIL1, 
				USER_CREDIT_CARD1);
		offer= offerService.findOffer(offer.getOfferId());
		try {
			removeOffer(offer.getOfferId());
		} catch (NotRemovableOfferException e) {
			exceptionCatched = true;
		}
		//Clear DB
		offerService.claimOffer(reserveId);
		removeOffer(offer.getOfferId());
		assertTrue(exceptionCatched);
	}
	

	@Test(expected = InstanceNotFoundException.class)
	public void testRemoveNonExistentOffer() throws InstanceNotFoundException, 
	NotRemovableOfferException {

		offerService.removeOffer(NON_EXISTENT_OFFER_ID);

	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentOffer() throws InstanceNotFoundException {

		offerService.findOffer(NON_EXISTENT_OFFER_ID);

	}

	@Test
	public void testFindOffers() throws NotRemovableOfferException {
		
		//Dates
		Calendar validDate = Calendar.getInstance();
		validDate.add(Calendar.DAY_OF_MONTH,1);
		Calendar invalidDate = Calendar.getInstance();
		invalidDate.add(Calendar.DAY_OF_MONTH, 100);
		
		//Add Offers
		List<Offer> offers = new LinkedList<Offer>();
		Offer offer1 = createOffer(getValidOffer("Offer name 1"));
		offers.add(offer1);
		Offer offer2 = createOffer(getValidOffer("Offer name 2"));
		offers.add(offer2);
		Offer offer3 = createOffer(getValidOffer("Offer name 3"));
		offers.add(offer3);

		try {
			//No params: should return all offers on db
			List<Offer> foundOffers = offerService.findOffers(null,null,null);
			assertEquals(offers, foundOffers);
			
			//Empty list: should return all offers on db
			foundOffers = offerService.findOffers("",null,null);
			assertEquals(offers, foundOffers);
			
			//Word only on name: should return all offers on db
			foundOffers = offerService.findOffers("name",null,null);
			assertEquals(offers, foundOffers);
			
			//Word only on desc: should return all offers on db
			foundOffers = offerService.findOffers("Description",null,null);
			assertEquals(offers, foundOffers); 
			
			//List of words: should return all offers on db
			foundOffers = offerService.findOffers("name Description Offer",null,null);
			assertEquals(offers, foundOffers); 
			
			//Word only on offer 2
			foundOffers = offerService.findOffers("2",null,null);
			assertEquals(1, foundOffers.size());
			assertEquals(offers.get(1), foundOffers.get(0));
			
			//No found word
			foundOffers = offerService.findOffers("title 5",null,null);
			assertEquals(0, foundOffers.size());
			
			//Status CREATED: should return all offers on db
			foundOffers = offerService.findOffers(null,OfferState.CREATED,null);
			assertEquals(offers, foundOffers); 
			
			//Status COMMITED: should return empty list
			foundOffers = offerService.findOffers(null,OfferState.COMMITTED,null);
			assertEquals(0, foundOffers.size());
			
			//Valid date: should return all events
			foundOffers = offerService.findOffers(null,null,validDate);
			assertEquals(offers, foundOffers);
			
			//Invalid date: should return nothing
			foundOffers = offerService.findOffers(null,null,invalidDate);
			assertEquals(0, foundOffers.size());
			
			/*Multiple params activated*/
			
			/*A) keywords and status*/
			//A1-Keys OK, status OK
			foundOffers = offerService.findOffers("offer",OfferState.CREATED,null);
			assertEquals(offers, foundOffers);
			
			//A2-Keys WRONG, status OK
			foundOffers = offerService.findOffers("nope",OfferState.CREATED,null);
			assertEquals(0, foundOffers.size());
			
			//A3-Keys OK, status WRONG
			foundOffers = offerService.findOffers("offer",OfferState.COMMITTED,null);
			assertEquals(0, foundOffers.size());
			
			//A4-Keys WRONG, status WRONG
			foundOffers = offerService.findOffers("nope",OfferState.COMMITTED,null);
			assertEquals(0, foundOffers.size());
			
			/*B)keywords and date*/
			//B1-Keys OK, date OK
			foundOffers = offerService.findOffers("offer",null,validDate);
			assertEquals(offers, foundOffers);
			
			//B2-Keys WRONG, date OK
			foundOffers = offerService.findOffers("nope",null,validDate);
			assertEquals(0, foundOffers.size());
			
			//B3-Keys OK, date WRONG
			foundOffers = offerService.findOffers("offer",null,invalidDate);
			assertEquals(0, foundOffers.size());
			
			//B4-Keys WRONG, date WRONG
			foundOffers = offerService.findOffers("nope",null,invalidDate);
			assertEquals(0, foundOffers.size());
			
			/*C)status and date*/
			//C1-status OK, date OK
			foundOffers = offerService.findOffers(null,OfferState.CREATED,validDate);
			assertEquals(offers, foundOffers);
			
			//C2-status WRONG, date OK
			foundOffers = offerService.findOffers(null,OfferState.COMMITTED,validDate);
			assertEquals(0, foundOffers.size());
			
			//C3-status OK, date WRONG
			foundOffers = offerService.findOffers(null,OfferState.CREATED,invalidDate);
			assertEquals(0, foundOffers.size());
			
			//C4-status WRONG, date WRONG
			foundOffers = offerService.findOffers(null,OfferState.COMMITTED,invalidDate);
			assertEquals(0, foundOffers.size());
			
			/*D)all*/
			//D1-keywords OK, status OK, date OK
			foundOffers = offerService.findOffers("offer",OfferState.CREATED,validDate);
			assertEquals(offers, foundOffers);
			
			//D2-keywords WRONG, status OK, date OK
			foundOffers = offerService.findOffers("nope",OfferState.CREATED,validDate);
			assertEquals(0, foundOffers.size());
			
			//D3-keywords OK, status WRONG, date OK
			foundOffers = offerService.findOffers("offer",OfferState.COMMITTED,validDate);
			assertEquals(0, foundOffers.size());
			
			//D4-keywords OK, status OK, date WRONG
			foundOffers = offerService.findOffers("offer",OfferState.CREATED,invalidDate);
			assertEquals(0, foundOffers.size());
			
			//D5-keywords WRONG, status WRONG, date OK
			foundOffers = offerService.findOffers("nope",OfferState.COMMITTED,validDate);
			assertEquals(0, foundOffers.size());
			
			//D6-keywords WRONG, status OK, date WRONG
			foundOffers = offerService.findOffers("nope",OfferState.CREATED,invalidDate);
			assertEquals(0, foundOffers.size());
			
			//D7-keywords OK, status WRONG, date WRONG
			foundOffers = offerService.findOffers("offer",OfferState.COMMITTED,invalidDate);
			assertEquals(0, foundOffers.size());
			
			//D8-keywords WRONG, status WRONG, date WRONG
			foundOffers = offerService.findOffers("nope",OfferState.COMMITTED,invalidDate);
			assertEquals(0, foundOffers.size());

			
			
		} finally {
			//Clear Database
			for (Offer Offer : offers) {
				removeOffer(Offer.getOfferId());
			}
		}

	}


	@Test
	public void testReserveAndgetReservebyId() throws InstanceNotFoundException, 
	InputValidationException, NotRemovableOfferException, OfferExpirationException, 
	FullOfferException, ReserveExpirationException, DuplicatedReserveException {

		Offer offer = createOffer(getValidOffer());
		Long reserveId = offerService.reserveOffer(offer.getOfferId(),
				USER_MAIL1 , USER_CREDIT_CARD1);

		//Clear Database
		offerService.claimOffer(reserveId);
		removeOffer(offer.getOfferId());
	}
		
	
	@Test
	public void testReserveInvalidReserve() throws InputValidationException,  
	InstanceNotFoundException, OfferExpirationException, FullOfferException, 
	NotRemovableOfferException, DuplicatedReserveException{
		
		boolean exceptionCatched = false;
		Offer offer = createOffer(getValidOffer());
		
		//Check offerId not null
		try {
			offerService.reserveOffer(null, USER_MAIL1, USER_CREDIT_CARD1);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		}
		assertTrue(exceptionCatched);
		
		exceptionCatched = false;
		
		//Check invalid offerId
		try {
			offerService.reserveOffer(123123123L, USER_MAIL1 , USER_CREDIT_CARD1);
		} catch (InstanceNotFoundException e) {
			exceptionCatched = true;
		}
		assertTrue(exceptionCatched);	
		
		exceptionCatched = false;
		
		//Check user_mail not null
		try {
			offerService.reserveOffer(offer.getOfferId(), null , USER_CREDIT_CARD1);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		}
		assertTrue(exceptionCatched);		
		
		exceptionCatched = false;
		
		//Check user_mail not empty
		try {
			offerService.reserveOffer(offer.getOfferId(), "" , USER_CREDIT_CARD1);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		}
		assertTrue(exceptionCatched);		
		
		exceptionCatched = false;
		
		//Check user_credit_card not null
		try {
			offerService.reserveOffer(offer.getOfferId(), USER_MAIL1 , null);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		}
		assertTrue(exceptionCatched);
		
		exceptionCatched = false;
		
		//Check user_credit_card not empty
		try {
			offerService.reserveOffer(offer.getOfferId(), USER_MAIL1 , "");
		} catch (InputValidationException e) {
			exceptionCatched = true;
		}
		assertTrue(exceptionCatched);
		
		removeOffer(offer.getOfferId());
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentReserve() throws InstanceNotFoundException {

		offerService.findReserve(NON_EXISTENT_RESERVE_ID);

	}
	
	
	@Test
	public void testFindReserves() throws InstanceNotFoundException, 
	InputValidationException, OfferExpirationException, ReserveExpirationException, 
	FullOfferException, NotRemovableOfferException, DuplicatedReserveException {
		
		//Add Offers
		Offer offer1 = createOffer(getValidOffer("Offer name 1"));
		Offer offer2 = createOffer(getValidOffer("Offer name 2"));
		
		//Add Reserves
		List<Reserve> reserves1 = new LinkedList<Reserve>();
		List<Reserve> reserves2 = new LinkedList<Reserve>();
		List<Reserve> reservesTotal = new LinkedList<Reserve>();
		Reserve reserve1 = offerService.findReserve(offerService.reserveOffer
				(offer1.getOfferId(), USER_MAIL1, USER_CREDIT_CARD1));
		reserves1.add(reserve1);
		reservesTotal.add(reserve1);
		Reserve reserve2 = offerService.findReserve(offerService.reserveOffer
				(offer2.getOfferId(), USER_MAIL2, USER_CREDIT_CARD2));
		reserves2.add(reserve2);
		reservesTotal.add(reserve2);
		Reserve reserve3 = offerService.findReserve(offerService.reserveOffer
				(offer1.getOfferId(), USER_MAIL2, USER_CREDIT_CARD2));
		reserves1.add(reserve3);
		reservesTotal.add(reserve3);

		try {
			//No params: should return all reserves on db
			List<Reserve> foundReserves = offerService.findReserves(null,null);
			assertEquals(reservesTotal, foundReserves);
			
			//Reserves of offer1
			foundReserves = offerService.findReserves(offer1.getOfferId(),null);
			assertEquals(reserves1, foundReserves);
			
			//Reserves of offer2
			foundReserves = offerService.findReserves(offer2.getOfferId(),null);
			assertEquals(reserves2, foundReserves);
			
			//All Pendant reserves (all)
			foundReserves = offerService.findReserves(null,ReserveState.PENDANT);
			assertEquals(reservesTotal, foundReserves);
			
			//All Closed reserve (none)
			foundReserves = offerService.findReserves(null,ReserveState.CLOSED);
			assertEquals(0, foundReserves.size());
			
			//Reserve 1 claimed
			offerService.claimOffer(reserve1.getReserveId());
			foundReserves = offerService.findReserves(offer1.getOfferId(),
					ReserveState.CLOSED);
			reserves1.clear();
			reserves1.add(offerService.findReserve(reserve1.getReserveId()));
			assertEquals(reserves1, foundReserves);
						
		} finally {
			offerService.claimOffer(reserve2.getReserveId());
			removeOffer(offer2.getOfferId());
			offerService.claimOffer(reserve3.getReserveId());
			removeOffer(offer1.getOfferId());
			
		}
		
	}
	
	@Test(expected = FullOfferException.class)
	public void testMaxReservesOffer () throws InstanceNotFoundException, 
			InputValidationException, OfferExpirationException, FullOfferException, 
			DuplicatedReserveException {
		Offer offer = createOffer(getValidOffer());
		for (int i = 0; i < offer.getMaxPeople(); i++) 
			offerService.reserveOffer(offer.getOfferId(),
				USER_MAIL1+i , USER_CREDIT_CARD1);
		//Now, exception incomes!
		try {
			offerService.reserveOffer(offer.getOfferId(), USER_MAIL1 , USER_CREDIT_CARD1);
		} catch (FullOfferException e) {
			removeProblematicOffer(offer.getOfferId());
			throw e;
		}
	}
	
	@Test(expected = DuplicatedReserveException.class)
	public void testDuplicatedReserveOfOffer () throws InstanceNotFoundException, 
			InputValidationException, OfferExpirationException, FullOfferException, 
			DuplicatedReserveException, ReserveExpirationException,
			NotRemovableOfferException {
		Offer offer = createOffer(getValidOffer());
		Long reserveId = offerService.reserveOffer(offer.getOfferId(),
				USER_MAIL1, USER_CREDIT_CARD1);
		//Now, exception incomes!
		try {
			offerService.reserveOffer(offer.getOfferId(), USER_MAIL1, USER_CREDIT_CARD1);
		} catch (DuplicatedReserveException e) {
			offerService.claimOffer(reserveId);
			removeOffer(offer.getOfferId());
			throw e;
		}
	}
	
	
	@Test
	public void testClaimOffer() throws InputValidationException, InstanceNotFoundException,
	OfferExpirationException, FullOfferException, ReserveExpirationException, 
	NotRemovableOfferException, DuplicatedReserveException{

		Offer offer = createOffer(getValidOffer());

		Long reserveId = offerService.reserveOffer(offer.getOfferId(),
					USER_MAIL1, USER_CREDIT_CARD1);
		
		offerService.claimOffer(reserveId);
		Reserve result = offerService.findReserve(reserveId);
		assertEquals(result.getStatus(),ReserveState.CLOSED);
		
		//Clear DB
		removeOffer(offer.getOfferId());
		
	}
	
	@Test (expected = InstanceNotFoundException.class)
	public void testClaimNonExistentReserve () throws InstanceNotFoundException, 
			ReserveExpirationException {
		offerService.claimOffer(NON_EXISTENT_RESERVE_ID);
	}
	
	@Test (expected = OfferExpirationException.class)
	public void testReserveExpiredOffer () throws InstanceNotFoundException, 
			InputValidationException, OfferExpirationException, FullOfferException, 
			ReserveExpirationException, NotRemovableOfferException, DuplicatedReserveException {
		Offer offer = getValidOffer();
		Calendar c;
		c = offer.getStartDate();
		c.add(Calendar.DAY_OF_MONTH, -5);
		offer.setStartDate(c);
		offer.setEndDate(c);
		offer = createOffer(offer);

		try {
			offerService.reserveOffer(offer.getOfferId(),
					USER_MAIL1, USER_CREDIT_CARD1);
		} catch (OfferExpirationException e) {
			//Clear DB
			removeOffer(offer.getOfferId());
			throw e;
		}
	}
	
	@Test (expected = ReserveExpirationException.class)
	public void testClaimExpiredReserve () throws InstanceNotFoundException, 
			InputValidationException, OfferExpirationException, FullOfferException, 
			ReserveExpirationException, NotRemovableOfferException,
			DuplicatedReserveException {
		Offer offer = getValidOffer();
		Calendar c;
		c = offer.getStartDate();
		c.add(Calendar.DAY_OF_MONTH, -5);
		offer.setEnjoyDate(c);
		offer = createOffer(offer);

		Long reserveId = offerService.reserveOffer(offer.getOfferId(),
					USER_MAIL1, USER_CREDIT_CARD1);
		
		try {
			offerService.claimOffer(reserveId);
		}catch (ReserveExpirationException e) {
			//Clear DB
			removeProblematicOffer(offer.getOfferId());
			throw e;
		}
		
		
	}

	private void removeProblematicOffer(Long offerId) throws InstanceNotFoundException {
		/*Method created in order to clean DB*/
		DataSource dataSource = DataSourceLocator.getDataSource(OFFER_DATA_SOURCE);
		SqlOfferDao offerDao = SqlOfferDaoFactory.getDao();
		
	    try (Connection connection = dataSource.getConnection()) {
	
	         try {
	
	             /* Prepare connection. */
	             connection
	                     .setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	             connection.setAutoCommit(false);
	             
	             Offer offer = offerDao.find(connection, offerId);
	             /* Do work. */
	         	 offerDao.remove(connection, offerId);
	            
	             /* Commit. */
	             connection.commit();
	
	         } catch (InstanceNotFoundException e) {
	             connection.commit();
	             throw e;
	         } catch (SQLException e) {
	             connection.rollback();
	             throw new RuntimeException(e);
	         } catch (RuntimeException | Error e) {
	             connection.rollback();
	             throw e;
	         }
	
	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
	
	 }
		
}
	

