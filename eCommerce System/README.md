# eCommerce System

**Description**
An eCommerce application built using Java 8, includes multi-tier user system with registration and product support.

**Technologies Used**
  *	Java 8
    * Javalin (HTTP handling)
    * JDBC (Database Connectivity)
    * JUnit (Unit Testing)
    * Logback (Logging)
  * PostgreSQL
  * Postman (HTTP messaging)
  * Gradle (Dependency Management)
*	Functionalities: 
  * Multi-tier Users - There must be at least three different types of user accounts each different functionality access. 
  * Basic mathematic manipulation of the fields of an entity object.
  * User login with passwords and logout.
    * Passwords may not be stored in their raw form in the database. They must be encrypted somehow. 
    * Stretch Goal: User registration. 
  * Validation for invalid inputs. 
  * Reasonable Logging of user interactions with the application. 

** Application Design**
  * Data persistence in a SQL database normalized to 3rd normal form. 
  * Three layered architecture of the Java application.
  * DAO design pattern.
  * User interactions simulated by HTTP messaging via Postman. 

**Application Functionalities**
*	Customers : 
	* Can Register
	* Purchase products
	* Apply to become a seller
*	Sellers :
	* Can add new products for sale
	* View orders / sales
	* Mark orders / sales as shipped or cancelled
	* All customer features
*	Administrator :
	* Can approve / deny sellers
	* All seller features
