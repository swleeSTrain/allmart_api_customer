package org.sunbong.allmart_api.order.dto;

import lombok.Data;
import java.util.List;

@Data
public class NaverChatbotOrderDTO {
    private String userId;
    private List<Bubble> bubbles;

    @Data
    public static class Bubble {
        private String type;
        private DataField data;
        private List<Slot> slot;

        @Data
        public static class DataField {
            private String description;
        }

        @Data
        public static class Slot {
            private String name;
            private String value;
        }
    }
}