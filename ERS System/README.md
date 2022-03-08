# Employee Reimbursement System (ERS)
#
## Executive Summary
The Expense Reimbursement System (ERS) will manage the process of reimbursing employees for expenses incurred while on company time. All employees in the company can login and submit requests for reimbursement and view their past tickets and pending requests. Finance managers can log in and view all reimbursement requests and past history for all employees in the company. Finance managers are authorized to approve and deny requests for expense reimbursement.

**State-chart Diagram (Reimbursement Statuses)** 
![](./imgs/state-chart.jpg)

**Reimbursement Types**

Employees must select the type of reimbursement as: LODGING, TRAVEL, FOOD, or OTHER.

**Logical Model**
![](./imgs/logical.jpg)

**Physical Model**
![](./imgs/physical.jpg)

**Use Case Diagram**
![](./imgs/use-case.jpg)

**Activity Diagram**
![](./imgs/activity.jpg)

## Technical Process

The back-end system uses JDBC to connect to an AWS RDS Postgres database and Javalin technology for dynamic Web application development.
The front-end view uses HTML/JavaScript to make an application that can call server-side components RESTfully. 
Passwords are encrypted in Java and securely stored in the database. 
The middle tier follows proper layered architecture, has reasonable test coverage of the service and DAO layers with JUnit and Mockito, and Logback for appropriate logging.
