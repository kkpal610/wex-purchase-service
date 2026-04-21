package com.wex.purchase.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wex.purchase.exception.CurrencyConversionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TreasuryRateService {

    @Value("${treasury.api.url}")
    private String apiUrl;

    private final WebClient webClient;

    public TreasuryRateService() {
        this.webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();
    }

    public Map<String, Object> getExchangeRate(String targetCountry, LocalDate purchaseDate) {
        try {
            LocalDate searchStartDate = purchaseDate.minusMonths(6);
            String startDateStr = searchStartDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            String endDateStr = purchaseDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

            String url = String.format("%s?filter=country:eq:%s,record_date:lte:%s,record_date:gte:%s&sort=-record_date",
                    apiUrl, targetCountry, endDateStr, startDateStr);

            JsonNode response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                            .maxBackoff(Duration.ofSeconds(5)))
                    .block();

            if (response != null && response.has("data")) {
                JsonNode data = response.get("data");
                if (data.isArray() && data.size() > 0) {
                    for (JsonNode rateNode : data) {
                        LocalDate rateDate = LocalDate.parse(rateNode.get("record_date").asText());
                        if (!rateDate.isAfter(purchaseDate)) {
                            Map<String, Object> mixedMap = new HashMap<>();
                            mixedMap.put("targetCurrency", rateNode.get("country_currency_desc").asText());
                            mixedMap.put("exchangeRate",rateNode.get("exchange_rate").asDouble());
                            return mixedMap;
                        }
                    }
                }
            }

            throw new CurrencyConversionException(
                    String.format("No exchange rate available for currency %s within 6 months before date %s",
                            targetCountry, purchaseDate));

        } catch (WebClientResponseException e) {
            throw new CurrencyConversionException("Treasury API error: " + e.getStatusCode());
        } catch (Exception e) {
            throw new CurrencyConversionException("Failed to get exchange rate: " + e.getMessage());
        }
    }
}