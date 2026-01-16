package rugbyniela.entity.pojo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = { 
    @UniqueConstraint(columnNames = { "user_id", "coalition_id" }) // Evita duplicados
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoalitionRequest {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quién quiere entrar
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // A dónde quiere entrar
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coalition_id", nullable = false)
    private Coalition coalition;

    // Cuándo lo pidió (para mostrar las más antiguas primero)
    @Column(nullable = false)
    private LocalDateTime requestedAt;
}
