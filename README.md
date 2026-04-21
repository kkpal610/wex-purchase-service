# wex-currency-service-assignment
Wex Assignment - 
This is a Spring Boot application that:

Stores purchase transactions in USD
Retrieves transactions by ID
Converts the amount to a target currency using U.S. Treasury exchange rates
Uses an H2 in-memory database
Integrates with Treasury Fiscal Data API

# Running the Endpoints
1) Store a Purchase Transaction
   ```
   curl --location 'http://localhost:8081/api/purchases' \
    --header 'Content-Type: application/json' \
    --header 'Accept: application/json' \
    --data '{
      "description": "Mouse",
      "transactionDate": "2026-01-15",
      "amountUSD": 31.23
    }'
   ```

2) Retrieve a purchase transaction in a specified country's currency
   ```
   curl --location 'http://localhost:8081/api/purchases/52ae7ee4-3407-49f8-a5c8-9b9ee2e2f4b5/convert?country=india' \
    --header 'Content-Type: application/json'
   ```
