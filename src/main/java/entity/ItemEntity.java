package entity;

import lombok.*;

@ToString
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {
    private String code;
    private String description;
    private String packSize;
    private Double unitPrice;
    private Integer QOH;
}
