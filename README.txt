MarketPlace App Installation Guide

If android studio version is below 3.5, changes to be made in the gradle files
-> build.gradle (Project: MarketPlace)
   classpath 'com.android.tools.build:gradle:3.3.1'
-> build.gradle (Module: app)
   minSdkVersion 15

As we are using firebase Database signIn using emailId, when you run the program you have to update the google services from the app store. Phone should be prompting that when you are try to sign up.


App Description

MarketPlace Application 
1) Sign up with an existing email address and any random new password
2) Choose Buy or Sell to go forward
3) Either way choose 'Profile' from Navigation Drawer Menu and set up your profile (image necessary)
4) Seller Side
	- Choose 'Add Product' from Navigation Drawer Menu to add a new product
	- Home page shows all your uploads
	- Seller can update or delete an existing item

5) Buyer Side 
	- Choose Category 
	- Home page shows all the products of the selected category
	- Click on Buy button to send request to the seller
	- Check 'Messages' from the Navigation Drawer Menu to see the status or cancel the 	  sent request.
6) Once a seller has requests from any buyers, seller can either approve or reject the request.
7) If the request is approved, buyer sees the seller Name and contact information in the messages menu option.


