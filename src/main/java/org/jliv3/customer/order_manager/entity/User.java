package org.jliv3.customer.order_manager.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class User extends Auditor<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;
}
