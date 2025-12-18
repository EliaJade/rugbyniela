package rugbyniela.entity.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DivisionBet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Vinculación al Ticket Semanal (Padre)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_ticket_id", nullable = false)
    private WeeklyBetTicket weeklyBetTicket;

    // ¿Sobre qué división estás opinando?
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id", nullable = false)
    private Division division;

    // ¿Quién crees que quedará líder?
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predicted_leader_id", nullable = false)
    private Team predictedLeader;
}
