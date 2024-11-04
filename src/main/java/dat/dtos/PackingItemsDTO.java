package dat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PackingItemsDTO {
    @JsonProperty("items")
    private List<PackingItem> items;

    public double getWeightInGrams() {
        return 0;
    }

    public double getQuantity() {
        return 0;
    }

    @Getter
    @NoArgsConstructor
    public static class PackingItem {
        private String name;
        private int weightInGrams;
        private int quantity;
        private String description;
        private String category;
        private List<BuyingOption> buyingOptions;

        public PackingItem(String name, int weightInGrams, int quantity, String description, String category) {
            this.name = name;
            this.weightInGrams = weightInGrams;
            this.quantity = quantity;
            this.description = description;
            this.category = category;
        }

        @Getter
        @NoArgsConstructor
        public static class BuyingOption {
            private String shopName;
            private String shopUrl;
            private double price;
        }
    }
}
