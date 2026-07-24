package com.demo.balance.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Table("balance")
public class Balance {

    @Id
    private Long id;

    @Column("account_id")
    private Long accountId;

    // guardado como texto (VARCHAR na tabela); o BalanceType vive só na borda (mapper/DTO)
    private String type;

    private BigDecimal amount;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
