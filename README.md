# CurrencyConverterSample
Sample application for currency convertion between USD and SEK

The project consists of one activity with fragment, which design is similar to Google Currency Converter.
By starting the application, the request to http://fixer.io is being sent. The USD currency is selected by default.
Other currency choice is disabled for now, because there is only SEK available for convertion.

For retreiving data from server, I used Retrofit library. 
For transfering data between classes, EventBus is used.

There is a subscribtion for events in fragment, so all fields are populated only while data has been received.
If the data couldn't be received, the toask will be shown.

