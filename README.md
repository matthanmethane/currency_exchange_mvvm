# Currency Exchange MVVM
Simple currency exchange MVVM
## Architecture Diagram
![mvvm_currency drawio](https://user-images.githubusercontent.com/10554125/183276094-b1110af0-90b8-436f-86ca-e160dda758c6.png)

MVVM was chosen as this is a very simple application that involves external Network call. In order to achieve separation of concerns, the application is divided into 3 main layers: Data, Business Logic(Main) and UI. 
Data layer is in charge of making network calls and retrieving information from the API response.

Business Logic Layer is where the business logic happens. In this case, it is in charge of calculating the currency rate. 

The Repository exists between the viewmodel and the data layer. The purpose of this is to let ViewModel only worry about calculating the currency rate and let data layer to be solely repsonsible for getting the required data from an external API call.


UI Layer is what is presented to the user and its only purpose it on presentation. 

## Notable Decision Points
1. MVVM: Despite being a simple application with one functionality, it was built with scability in mind. It will be relative easy to add new features as they are divided into different layers. 
2. Exception Handling: By using Sealed Classes and Kotlin Flow, exception handling such as in case Network Failure is fully managed without breaking the application.
3. Pairwise Conversion Rate: My original plan was to use an api that returns the entire conversion rate based on USD upon application launch and use that to do any conversion. However, I decided to ultimately go with Pairwise because the update on the UI was relatively due to using Kotlin Flow.
4. Live Retrieval of Currency List: Many examples on the internet had static set of currencies that was hard coded. I did not particularly like this idea and therefore, the list of currencies are dynamic and loaded from API call during launching of the application. This leads to higher maintainability of the application as it will change accordingly to chanes in API.
5. Exposing API key: I have exposed my API key in the code which is something I never do. However, I have put it in case anyone wants to run this application, they won't have to bother making their own API key. In actual production system, the key will be a varaible that will be fed during build time through local.properties in local setups. 

## Notable Use of Library
1. Retrofit2: Network Events and REST API Call
2. Dagger-Hilt: You would never want to have multiple instances of Retrofit calling the same API. Hence, Dagger-Hilt is used to keep Singleton of Retrofit and Main Repository. As the network calls are launched in Coroutines, DispatcherProvider is also kept as a Singleton.

## Video

https://user-images.githubusercontent.com/10554125/183276776-87a9fadd-ca17-4b1a-abb0-336e2993d589.mov



