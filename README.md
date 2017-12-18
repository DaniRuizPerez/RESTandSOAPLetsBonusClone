REST and SOAP LetsBonus Clone
============

Client and Server offer manager, with persistence and publicly accesible using servlets. Implemented REST and SOAP and with both Jetty and Tomcat application servers. This was a project for the Internet and Distributed Systems course in my junior year of my undergrad in computer science at UDC (Spain) and is implemented in Java by my team and me. For more information and diagrams, please check the [project report](https://github.com/DaniRuizPerez/RESTandSOAPLetsBonusClone/blob/master/LETSMALUS.pdf)


# Technology and dependencies
- Java 
- MySQL
- Maven
- Jetty
- Tomcat
- Eclipse

## How to run

- Import the project into Eclipse with Maven
- StartMySQL service
- Execute ```mvn install``` into the root of the project
- Start either Jetty or Tomcat
- Execute the client with ```exec:java Dexec.mainClass="es.udc.ws.app.client.ui.OfferServiceClient" -Dexec.args="ARGUMENTS```

The following are the supported arguments

``` 
[add] OfferServiceClient -updateOffer <name> <description> <price>
<discountedPrice> <MaxReserves> <startDate> <endDate>
<enjoyDate>
[update] OfferServiceClient -updateOffer <offerId> <name> <description>
<price> <discountedPrice> <MaxReserves> <startDate> <endDate>
<enjoyDate>
[remove] OfferServiceClient -removeOffer <offerId>
[findOffer] OfferServiceClient -findOffer <offerId>
[findOffers] OfferServiceClient -findOffers <keywords>
[addReserve] OfferServiceClient -reserveOffer <offerId> <userMail>
<userCreditCard>
[findReserve] OfferServiceClient -findReserve <reserveId>
[findReserves] OfferServiceClient -findReserves <offerId> <state>
[claimOffer] OfferServiceClient -claimReserve <reserveId>
```  


## Contact

Contact [Daniel Ruiz Perez](mailto:druiz072@fiu.edu) for requests, bug reports and good jokes.


## License

The software in this repository is available under the GNU General Public License, version 3. See the [LICENSE](https://github.com/DaniRuizPerez/RESTandSOAPLetsBonusClone/blob/master/LICENSE) file for more information.



