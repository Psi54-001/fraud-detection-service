package example.com.frauddetection.controller;

import example.com.frauddetection.modal.Currency;
import example.com.frauddetection.modal.Transaction;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FraudDetectionControllerTest {
    @BeforeEach
    public void setup() {
        // This setup will ensure that RestAssured points to the correct base URI for the Spring Boot application.
        RestAssured.baseURI = "http://localhost/v1/fraud-detection";
        RestAssured.port = 8080;
    }

    @Test
    public void givenAmountIsNull_thenReturnBadRequestWithMessageMustNotBeNull() {
        Transaction transaction = new Transaction(null, Currency.USD, "unknown terminal", 40, "card number");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(transaction)
                .post("/")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].defaultMessage", equalTo("must not be null"));
    }

    @Test
    public void givenAmountLessThenZero_thenReturnBadRequestWithMessageMustBeGreaterOrEqualThanZero() {
        Transaction transaction = new Transaction(-1D, Currency.USD, "unknown terminal", 40, "card number");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(transaction)
                .post("/")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].defaultMessage", equalTo("must be greater than or equal to 0"));
    }

    @Test
    public void givenCurrencyIsNull_thenReturnBadRequestWithMessageMustNotBeNull() {
        Transaction transaction = new Transaction(0D, null, "unknown terminal", 40, "card number");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(transaction)
                .post("/")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].defaultMessage", equalTo("must not be null"));
    }

    @Test
    public void givenTerminalIdIsNull_thenReturnBadRequestWithMessageMustNotBeBlank() {
        Transaction transaction = new Transaction(0D, Currency.USD, null, 40, "card number");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(transaction)
                .post("/")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].defaultMessage", equalTo("must not be blank"));
    }

    @Test
    public void givenTerminalIdIsBlank_thenReturnBadRequestWithMessageMustNotBeBlank() {
        Transaction transaction = new Transaction(0D, Currency.USD, "", 40, "card number");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(transaction)
                .post("/")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].defaultMessage", equalTo("must not be blank"));
    }

    @Test
    public void givenThreadScoreIsNull_thenReturnBadRequestWithMessageMustNotBeNull() {
        Transaction transaction = new Transaction(0D, Currency.USD, "terminalId", null, "card number");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(transaction)
                .post("/")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].defaultMessage", equalTo("must not be null"));
    }

    @Test
    public void givenThreadScoreIsLessThenZero_thenReturnBadRequestWithMessageMustBeGreaterOrEqualThanZero() {
        Transaction transaction = new Transaction(0D, Currency.USD, "terminalId", -1, "card number");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(transaction)
                .post("/")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].defaultMessage", equalTo("must be greater than or equal to 0"));
    }

    @Test
    public void givenThreadScoreIsGreaterThen100_thenReturnBadRequestWithMessageMustBeLessOrEqualTo100() {
        Transaction transaction = new Transaction(0D, Currency.USD, "terminalId", 101, "card number");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(transaction)
                .post("/")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].defaultMessage", equalTo("must be less than or equal to 100"));
    }

    @Test
    public void givenCardNumberIsNull_thenReturnBadRequestWithMessageMustNotBeBlank() {
        Transaction transaction = new Transaction(0D, Currency.USD, "terminalId", 10, null);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(transaction)
                .post("/")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].defaultMessage", equalTo("must not be blank"));
    }

    @Test
    public void givenCardNumberIsBlank_thenReturnBadRequestWithMessageMustNotBeBlank() {
        Transaction transaction = new Transaction(0D, Currency.USD, "terminalId", 10, "");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(transaction)
                .post("/")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("errors", hasSize(1))
                .body("errors[0].defaultMessage", equalTo("must not be blank"));
    }

    @Test
    public void givenLowTreadScoreButUnknownTerminal_thenReturnApprovedFraudReportWithScoreOf50() {
        Transaction transaction = new Transaction(1900D, Currency.USD, "unknown terminal", 40, "card number");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(transaction)
                .post("/")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo("APPROVED"))
                .body("rejectionMessage", equalTo("Transaction is approved."))
                .body("fraudScore", equalTo(50));
    }

    @Test
    public void givenValidTransactionFraudCheck_thenReturnApprovedFraudReport() {
        Transaction transaction = new Transaction(1500D, Currency.USD, "T1", 40, "card number");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(transaction)
                .post("/")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo("APPROVED"))
                .body("rejectionMessage", equalTo("Transaction is approved."))
                .body("fraudScore", equalTo(0));
    }

    @Test
    public void givenInvalidAmountAgainstThreadScoreFraudCheck_thenReturnRejectedFraudReport() {
        Transaction transaction = new Transaction(1500D, Currency.USD, "T1", 85, "card number");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(transaction)
                .post("/")
                .then()
                .log().ifError()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo("REJECTED"))
                .body("rejectionMessage", equalTo("Fraud score is to high."))
                .body("fraudScore", equalTo(100));
    }
}