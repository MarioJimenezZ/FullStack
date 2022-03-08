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

**Application Design**
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
