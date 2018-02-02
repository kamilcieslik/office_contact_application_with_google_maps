package database.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;

@Entity
@Table(name = "trade")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Max(10)
    @Column(name = "trade")
    private String trade;

    // 'Reverse' references:
    @OneToOne(mappedBy = "trade", cascade = {CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH})
    private Contact contact;

    public Trade() {
    }

    public Trade(String trade) {
        this.trade = trade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", trade='" + trade + '\'' +
                '}';
    }
}
