package org.jliv3.customer.order_manager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jliv3.customer.order_manager.utils.MySimpleDateFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Order extends Auditor<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer id;
    private String code;
    @Column(nullable = false)
    private String name;
    private String note;
    private boolean checked;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_img_fid", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<FileImg> listImg;

    public Order() {
        this.code = "";
        this.note = "";
        this.checked = false;
        listImg = new HashSet<>();
    }

    @PrePersist
    private void updateCode() {
        if (this.code.isEmpty()) {
            this.code = "unknow" + MySimpleDateFormat.get().format(new Date());
        }
    }
}
