package org.jliv3.customer.order_manager.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "imgs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer id;
    @Column(nullable = false)
    private String shortName;
    @Column(nullable = false, unique = true)
    private String fullName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileImg fileImg = (FileImg) o;
        return fullName.equals(fileImg.fullName);
    }

    @Override
    public int hashCode() {
        return shortName.hashCode() * 257 + fullName.hashCode();
    }
}
