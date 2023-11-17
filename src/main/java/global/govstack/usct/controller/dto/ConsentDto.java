package global.govstack.usct.controller.dto;

import global.govstack.usct.model.Consent;
import global.govstack.usct.types.ConsentStatus;
import java.time.LocalDateTime;
import lombok.Getter;

public class ConsentDto {

  private Integer id;
  private Integer candidate_id;

  @Getter private ConsentStatus status;

  @Getter private LocalDateTime dataTime;

  public ConsentDto() {}

  public ConsentDto(Consent consent) {
    this.id = consent.getId();
    this.candidate_id = consent.getCandidate().getId();
    this.status = consent.getStatus();
    this.dataTime = consent.getDate();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Integer getCandidate_id() {
    return candidate_id;
  }

  public void setCandidate_id(Integer candidate_id) {
    this.candidate_id = candidate_id;
  }

  public void setStatus(ConsentStatus status) {
    this.status = status;
  }

  public void setDataTime(LocalDateTime dataTime) {
    this.dataTime = dataTime;
  }
}
