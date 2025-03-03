## Fraud Detection Service

Following contains the reasoning and thoughts behind the design implementations used for the tests case for a Fraud
Detection Service. I see this service to be intended as a service to service application.

### Architecture

In the implemented service only the basic structure have been added consisting of a controller(endpoint), a service(
business logic) and a mocked repository. As I looked at designing this service I did run in to some points where I did
have some followup questions in order to complete the task in a manor I would personally define as satisfactory.

In order to be able to complete the task given I made follow assumptions.

* The given tread score comes from a trusted source and does not need to be validated.
* The fraud score is given based on the accumulated fraud detection rules scores.
* A transaction fails based on the accumulated fraud detection rules scores. (if the score is higher than 100)
* Both rejected and approved transactions needed to be returned as a response of status 200
* Both rejected and approved transactions contains the rejection status, the rejection message and the fraud score
* The returned message needed to be ambiguous enough in order to not extract sensitive information (I was in doubt of
  was the level of detail needed for the message)

In order to make the current design more robust it is also necessary to apply correct exception handling. Any know or
unknown exceptions needed to be caught and returned as a fitting response. I would do that by having a global exception
mapper to catch the exceptions, log the information and map them to appropriate response codes.

### Security

Currently, there is very little in the form of security,

* It will be necessary to add an authentication service to validate all requests.
* As the Service grows it could also be necessary to implement an Authorization service to validate that a user that has
  authentication to the system also have rights to a given endpoint.
* Secure the communication using HTTPs.
* Add checks against potential injection attacks. The generally is centered against strings.
* Have a repeatedly running pipeline to scan the dependencies for Common Vulnerabilities and Exposures(CVE) (both direct
  and inherited)
* Apply Rate limiting to avoid things like denial-of-service attacks
* Apply rules for password: like protection from brute force (forced logout)
* Ensure not to leak unnecessary information in form of error messages.

### High Availability

Since the initial implementation is very simple and does not have many dependencies, besides a simple mocked repository
of a read DB. The best way to ensure high availability is to ensure that the system is scalable. By monitoring the
amounts of requests for the service we can have another instance spin up at a given limit to ensure continued
availability.

### Logging

Since this service does deal with personal information It's important to only log what is needed and not to log any
personal information. However, it is important to log in key positions, so it's possible to debug issues on a running 
system.

### Monitoring

For monitoring the systems health status and metrics it would make sense to apply a monitoring tool. As part of this
task I could see that Spring Actuator often was suggested.

### Choice of frameworks

The following frameworks was what was used.

* Spring boot
* Hibernate Validator
* JUnit 5
* Mockito
* RestAssured

I used spring boot, because that is what Soft-pay utilises. There is no reason to introduce new technologies, if there
is no clear benefit.
It is better to keep it simple.
The choice to use Mockito and RestAssured for unit tests and integration testing of the system was because it is
frameworks I am familiar with, and I was unaware what Soft-pay currently are using.




