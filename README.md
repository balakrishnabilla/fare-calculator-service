# fare-calculator-service
Starting Point of the Application is AppMain.java & some Junit test cases covering all functional requirements.
Their job is to set/inject the dependencies before the start of the application and call the *getFare* method of *FareController*
Important components

1. Fare Rule Processing - 
    Fare Rule processing divided into 3 parts 
   
    1. Apply Peak OR off-peak fare for each Journey and feed the farthest Daily path to Daily cap fare processor.
    2. Apply Daily Cap fare on each journey and rollup the daily model and farthest weekly path to Weekly cap fare processor.
    3. Similarly apply/adjust Weekly Cap fare on each DailyRollup Model and roll up the weekly model.
    4. At the end loop through the Rollup model and calculate the fare.
2. Master Data store- Master Data storage will be maintained in the Caches for each table.
    1. Created 3 tables -
        1. *peak_time* DB table and their respective PeakTimeData and PeakTimeKey for all the peak and off-peak scheduled to have them availabe in the Cache
        2. Similarly, *capped_fare* table with CappedFareData and Path as data and key
        2. Similarly, *peak_off_peak_fare* table with PeakOffPeakFareData and Path

3. Basic cache framework including CacheManager, Cache and DataLoaders inspired from one of the application developed
   are in place.
4. Tried to apply the OOPS, SOLD principles and Design patters wherever applicable.    

###How to run -

Run sample as follows with the text file as input.

 ````
    mvn clean install
    
    java -jar fare-calculator-service-1.0-SNAPSHOT.jar "C:\fare-calculator-service\daily_journey.txt"
