# user-rest-api
Demo Spring Boot REST Apis for User Entity

Please create a mysql database by name user-repo or change the database name in application.properties acordingly (src/main/resources).
Please update datasource username and password accordingly in application.properties (src/main/resources).

The test reports (xml/text format) can be found in 'target/surefire-reports/' after running the tests using command 'mvn clean test'.

Following are the Unit Test Classes -
    UsersApiControllerTest
    UserServiceTest
    UserRepositoryTest
Following is the Integration Test Class -
    UsersApiControllerIntegrationTest

h2 database will be used for running all the test cases.

swagger ui url is 'http://localhost:8080/swagger-ui.html'

Use the following command to run the application from commandline --
    mvn spring-boot:run
