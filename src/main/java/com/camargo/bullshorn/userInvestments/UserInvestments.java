package com.camargo.bullshorn.userInvestments;

import com.camargo.bullshorn.appuser.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_investiments")
public class UserInvestments {

    @Id
    @SequenceGenerator(
            name = "investment_sequence",
            sequenceName = "investment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "investment_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String symbol;
    @Column(nullable = false)
    private LocalDateTime buyingDateAndTime;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private BigDecimal buyingPrice;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;;

    public UserInvestments(String symbol, String buyingDateAndTime, int quantity) {
        this.symbol = symbol;
        this.buyingDateAndTime = LocalDateTime.parse(buyingDateAndTime);
        this.quantity = quantity;
    }
}
