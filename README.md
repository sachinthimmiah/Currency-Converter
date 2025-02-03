# Currency-Converter API

## Description
Spring Boot application provides endpoints for fetching exchange rates and converting amounts from one currency to another currency using open exchange rates api

## Prerequisites 
- JDK 11 or higher
- Maven or Gradle (for dependency management and building)
- Open Exchange Rates API key

## Setup

### Step 1: Clone the repository
bash
git clone https://github.com/your-username/currency-converter.git
cd currency-converter

### Step :2 Add your Open Exchange API key to CurrencyService.java

private final String API_URL = "https://openexchangerates.org/api/latest.json?app_id=YOUR_API_KEY";


### Step 3 : Build the project

mvn clean install

### Step 4: Run the application

mvn spring-boot:run

### Step 5: Access the API

### Fetch All Exchange Rates using Postman

GET : http://localhost:8080/api/rates?base=USD

### Convert Currency using Postman

POST : http://localhost:8080/api/convert

Request Body 

{
  "from": "USD",
  "to": "EUR",
  "amount": 200
}


