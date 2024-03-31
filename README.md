Github Repo Link: https://github.com/patrickyu117/cs4520-assg5-starter-code<br><br>

What is this application:<br>
This application is a simple 2 screen application implemented using the MVVM design pattern.<br> 
Screen 1 is a login screen. It contains a username and password login field and a login button.<br> 
-To login:<br> 
--Username: admin<br> 
--Password: admin<br> 
Screen 2 displays the products from the product list from an API call.<br> 
--The product can either be food or equipment<br> 
--The product will display an image for the respective product, display the name, price, and expiry date if available<br>
--If we load the product list from the API, then close the app and turn the phone to airplane mode, and load back into the app, the app will retrieve the products that were saved to the local database when it first loaded in<br><br>

How to run the application:<br> 
--To run this app, simply import all of the files into Android Studio. Once all the files are imported, run the application using an Android phone emulator.<br> 
--Make sure that the phone is connected to the internet so that the API call can fetch the products<br><br>

Application structure:<br> 
ApiClient<br>
--Retrofit client that takes the JSON from the API call<br>
ApiService<br>
--API GET request to get the products from the ENDPOINT url provided page is an optional query that can specify a certain page to load. The API call will return a list of products<br>
ProductDao<br>
--Product Dao to create the functions to perform on the local room database<br>
ProductDatabase<br>
--The local room database that the app will fetch products from if the phone is offline<br>
MainActivity<br> 
--This is the main activity that hosts the 2 screen mentioned above <br> 
-- Contains all of the Jetpack Compose code used to make the UI of the 2 screens<br>
ProductRefreshWorker<br> 
--This is the work manager used to refresh the product list in the background every hour <br> <br> 


ProductListViewModel<br> 
--ViewModel for the product list<br>
ProductRepository<br>
--Local product repository database<br>
Product.kt<br>
--This is the class for the products. Contains all the fields that a product needs <br><br>

